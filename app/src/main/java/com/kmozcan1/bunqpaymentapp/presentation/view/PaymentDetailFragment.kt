package com.kmozcan1.bunqpaymentapp.presentation.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.PaymentDetailFragmentBinding
import com.kmozcan1.bunqpaymentapp.presentation.getFormattedDateTime
import com.kmozcan1.bunqpaymentapp.presentation.viewmodel.PaymentDetailViewModel
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.PaymentDetailViewState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PaymentDetailFragment : BaseFragment<PaymentDetailFragmentBinding, PaymentDetailViewModel>() {

    override val layoutId = R.layout.payment_detail_fragment

    override val viewModelClass: Class<PaymentDetailViewModel> = PaymentDetailViewModel::class.java

    override val isActionBarVisible = true

    private val args: PaymentDetailFragmentArgs by navArgs()

    override fun onViewBound() {
        setPageContentVisibility(false)
    }

    override fun observeLiveData() {
        viewModel.viewState.observe(this, observeViewState())
        viewModel.getPaymentDetail(args.paymentId)
    }

    private fun observeViewState() = Observer<PaymentDetailViewState> { viewState ->
        when (viewState) {
            PaymentDetailViewState.PaymentDetailNetworkError -> {
                createNetworkErrorDialog()
            }
            PaymentDetailViewState.PaymentDetailLoading -> {
                setProgressBarVisibility(true)
            }
            is PaymentDetailViewState.PaymentDetailReady -> {
                setPageContent(viewState.paymentDetail)
                setProgressBarVisibility(false)
                setPageContentVisibility(true)
            }
        }
    }

    /** Toggles progress bar visibility, also calls setPageContentVisibility */
    private fun setProgressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.paymentDetailProgressBar.visibility = View.VISIBLE
        } else {
            binding.paymentDetailProgressBar.visibility = View.GONE
        }
    }

    /** Toggles page content visibility */
    private fun setPageContentVisibility(isVisible: Boolean) {
        val visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.run {
            beneficiaryNameTextView.visibility = visibility
            paymentDetailAmountTextView.visibility = visibility
            paymentDateTextView.visibility = visibility
            paymentDetailsContainerLabelTextView.visibility = visibility
            paymentDetailsContainerLayout.visibility = visibility
        }
    }

    /** Sets the textViews */
    private fun setPageContent(paymentDetail: Payment) {
        binding.run {
            beneficiaryNameTextView.text = paymentDetail.counterpartyAlias.displayName
            paymentDetailAmountTextView.text = getString(R.string.payment_amount,
                paymentDetail.amount.value,
                Currency.getInstance(paymentDetail.amount.currency).symbol)
            paymentDateTextView.text = getFormattedDateTime(paymentDetail.updated)
            descriptionDetailTextView.text = paymentDetail.description
            paymentTypeTextView.text = paymentDetail.type
            paymentSubtypeTextView.text = paymentDetail.subType
        }
    }

    override fun onNetworkErrorDialogRetryButtonClicked() {
        viewModel.getPaymentDetail(args.paymentId)
    }

}