package com.kmozcan1.bunqpaymentapp.presentation.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.HomeFragmentBinding
import com.kmozcan1.bunqpaymentapp.presentation.adapter.PaymentListAdapter
import com.kmozcan1.bunqpaymentapp.presentation.viewmodel.HomeViewModel
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.HomeViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeViewModel>() {

    override val layoutId: Int = R.layout.home_fragment

    override val viewModelClass: Class<HomeViewModel> = HomeViewModel::class.java

    override val isActionBarVisible = false

    var errorState: HomeViewState? = null

    // RecyclerView Adapter
    private val paymentListAdapter: PaymentListAdapter by lazy {
        PaymentListAdapter { payment ->
            onPaymentListItemClick(payment)
        }
    }

    override fun onViewBound() {
        binding.homeFragment = this
        setInitializationLayoutVisibility(isVisible = true)
        setRecyclerView()
    }

    override fun observeLiveData() {
        viewModel.viewState.observe(this, observeViewState())
        if (!bunqPaymentApp.isApiContextInitialized) {
            viewModel.getBunqApiContext()
        } else {
            displayPayments()
        }

    }

    /** Observes HomeViewState */
    private fun observeViewState() = Observer<HomeViewState> { viewState ->
        when (viewState) {
            is HomeViewState.BunqApiContextSuccess -> {
                bunqPaymentApp.isApiContextInitialized = true
                displayPayments()
            }
            is HomeViewState.BunqApiContextNetworkError -> {
                errorState = HomeViewState.BunqApiContextNetworkError
                createNetworkErrorDialog()
            }
            is HomeViewState.PaymentList -> {
                viewModel.getHasRetainedListState()?.let {
                    if (!it) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            paymentListAdapter.submitData(lifecycle, viewState.pagingData)
                        }
                    }
                }
            }
        }
    }

    private fun setRecyclerView() {
        // This prevents paging 3 from loading all items at once when recyclerview height is 0dp
        // I spent like 3 hours trying to figure this out
        binding.paymentListRecyclerView.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
            paymentListAdapter.addLoadStateListener { loadState ->
                if ( loadState.prepend.endOfPaginationReached )
                {
                    // show the list after the first batch of payments are loaded
                    setProgressBarVisibility(false)
                    // stutters te page if I don't cancel here
                    this.cancel()
                }
            }
        }

        paymentListAdapter.addLoadStateListener { loadState ->
            if (loadState.prepend is LoadState.Error ||
                loadState.append is LoadState.Error ||
                loadState.refresh is LoadState.Error) {
                    errorState = HomeViewState.PaymentListNetworkError
                    createNetworkErrorDialog()
            }

        }

        binding.paymentListRecyclerView.adapter = paymentListAdapter
    }

    private fun displayPayments() {
        val hasRetained = viewModel.getHasRetainedListState()

        setInitializationLayoutVisibility(isVisible = false)

        if (hasRetained == null || !hasRetained) {
            setActionBarVisibility(isVisible = true)
            setProgressBarVisibility(isVisible = true)
            viewModel.getPaymentsList()
        }
    }

    /** Sets the visibility of progress bar and the remaining components */
    private fun setProgressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.run {
                paymentListRecyclerView.visibility = View.INVISIBLE
                makePaymentButton.visibility = View.INVISIBLE
                homeProgressBar.visibility = View.VISIBLE
            }
        } else {
            binding.run {
                paymentListRecyclerView.visibility = View.VISIBLE
                makePaymentButton.visibility = View.VISIBLE
                homeProgressBar.visibility = View.GONE
            }
        }
    }

    private fun setInitializationLayoutVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.initializingLayout.visibility = View.VISIBLE
        } else {
            binding.initializingLayout.visibility = View.GONE
        }
    }

    /** Called when an item from payment list is clicked, navigates to PaymentDetailFragment */
    private fun onPaymentListItemClick(payment: Payment) {
        viewModel.setHasRetainedListState(true)
        FragmentNavigation().navigateFromHomeToPaymentDetailFragment(payment.id)
    }

    /** Called when the make_payment_button is clicked, navigates to PaymentFragment */
    fun onMakePaymentButtonClick() {
        viewModel.setHasRetainedListState(true)
        FragmentNavigation().navigateFromHomeToPaymentFragment()
    }

    override fun onNetworkErrorDialogRetryButtonClicked() {
        errorState?.let { errorState ->
            if (errorState is HomeViewState.PaymentListNetworkError) {
                viewModel.run {
                    setHasRetainedListState(false)
                    getPaymentsList()
                }
            } else if (errorState is HomeViewState.BunqApiContextNetworkError) {
                viewModel.getBunqApiContext()
            }
        }
    }
}