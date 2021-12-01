package com.kmozcan1.bunqpaymentapp.presentation.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.HomeFragmentBinding
import com.kmozcan1.bunqpaymentapp.presentation.adapter.PaymentListAdapter
import com.kmozcan1.bunqpaymentapp.presentation.setRecyclerView
import com.kmozcan1.bunqpaymentapp.presentation.viewmodel.HomeViewModel
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.HomeViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeViewModel>() {

    override val layoutId: Int = R.layout.home_fragment

    override val viewModelClass: Class<HomeViewModel> = HomeViewModel::class.java

    override val isActionBarVisible = false

    // RecyclerView Adapter
    private val paymentListAdapter: PaymentListAdapter by lazy {
        PaymentListAdapter(requireContext(), mutableListOf()) { payment ->
            onPaymentListItemClick(payment)
        }
    }

    override fun onViewBound() {
        setInitializationLayoutVisibility(isVisible = true)
        setPaymentList()
    }

    override fun observeLiveData() {
        viewModel.viewState.observe(this, observeViewState())
        viewModel.getBunqApiContext()
    }

    /** Observes HomeViewState */
    private fun observeViewState() = Observer<HomeViewState> { viewState ->
        when (viewState) {
            is HomeViewState.BunqApiContext -> {
                setInitializationLayoutVisibility(isVisible = false)
                setActionBarVisibility(isVisible = true)
                setProgressBarVisibility(isVisible = true)
                viewModel.getPaymentsList()
            }
            is HomeViewState.PaymentList -> {
                setProgressBarVisibility(isVisible = false)
                paymentListAdapter.addPayments(viewState.paymentList)
            }
        }
    }

    private fun setPaymentList() {
        binding.paymentListRecyclerView.setRecyclerView(
            LinearLayoutManager(context),
            paymentListAdapter)
    }

    /** Sets the visibility of progress bar and the remaining components */
    private fun setProgressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.run {
                paymentListRecyclerView.visibility = View.GONE
                makePaymentButton.visibility = View.GONE
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

    }
}