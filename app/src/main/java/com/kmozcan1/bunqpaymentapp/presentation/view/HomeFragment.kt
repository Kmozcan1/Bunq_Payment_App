package com.kmozcan1.bunqpaymentapp.presentation.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
            is HomeViewState.BunqApiContext -> {
                bunqPaymentApp.isApiContextInitialized = true
                displayPayments()
            }
            is HomeViewState.PaymentList -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    paymentListAdapter.submitData(lifecycle, viewState.pagingData)
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

        binding.paymentListRecyclerView.adapter = paymentListAdapter
    }

    private fun displayPayments() {
        setInitializationLayoutVisibility(isVisible = false)
        setActionBarVisibility(isVisible = true)
        setProgressBarVisibility(isVisible = true)
        //viewModel.getPaymentsList()
        viewModel.getPaymentsList()
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

    /** Called when an item from payment list is clicked */
    private fun onPaymentListItemClick(payment: Payment) {

    }

    fun onMakePaymentButtonClick() {
        FragmentNavigation().navigateFromHomeToPaymentFragment()
    }
}