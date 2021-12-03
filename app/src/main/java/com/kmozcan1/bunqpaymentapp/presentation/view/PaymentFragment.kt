package com.kmozcan1.bunqpaymentapp.presentation.view

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.PaymentFragmentBinding
import com.kmozcan1.bunqpaymentapp.domain.model.PaymentValidationModel
import com.kmozcan1.bunqpaymentapp.presentation.hideKeyboard
import com.kmozcan1.bunqpaymentapp.presentation.viewmodel.PaymentViewModel
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.PaymentViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : BaseFragment<PaymentFragmentBinding, PaymentViewModel>() {

    override val layoutId = R.layout.payment_fragment

    override val viewModelClass: Class<PaymentViewModel> = PaymentViewModel::class.java

    override val isActionBarVisible = true

    // to prevent multiple button clicks
    private var paymentSubmitted = false

    override fun onViewBound() {
        binding.paymentFragment = this
        setBackPressCallback()
    }

    override fun observeLiveData() {
        viewModel.viewState.observe(this, observeViewState())
    }

    /** Observes the state of the fragment */
    private fun observeViewState() = Observer<PaymentViewState> { viewState ->
        when (viewState) {
            is PaymentViewState.PaymentValidation -> {
                applyPaymentValidation(viewState.paymentValidationResult)
                // Submit the payment if the fields are valid
                if (isPaymentValid(viewState.paymentValidationResult)) {
                    viewModel.submitPayment(
                        binding.paymentEmailEditText.text.toString(),
                        binding.paymentAmountEditText.text.toString(),
                        binding.paymentDescriptionEditText.text.toString()
                    )
                } else {
                    paymentSubmitted = false
                    binding.submitPaymentButton.isEnabled = true
                }
            }
            PaymentViewState.PaymentNetworkError -> {
                showPaymentResult(isSuccess = false, isNetworkError = true)
            }
            PaymentViewState.PaymentError -> {
                showPaymentResult(isSuccess = false)
            }
            PaymentViewState.PaymentProcessing -> {
                binding.submitPaymentButton.text = getString(R.string.payment_processing)
            }
            PaymentViewState.PaymentSuccessful -> {
                showPaymentResult(isSuccess = true)
            }
        }
    }

    /** Shows the payment_result_layout after its contents depending on the result */
    private fun showPaymentResult(isSuccess: Boolean, isNetworkError: Boolean = false) {
        if (isSuccess) {
            binding.run {
                paymentResultImageView.setImageResource(R.drawable.ic_baseline_check_circle_24)
                paymentResultTextView.text = getString(R.string.payment_successful)
                retryPaymentButton.visibility = View.GONE
                navigateToPaymentListButton.visibility = View.VISIBLE
            }
        } else {
            binding.run {
                paymentResultImageView.setImageResource(R.drawable.ic_baseline_error_24)
                if (isNetworkError) {
                    paymentResultTextView.text = getString(R.string.network_alert_dialog_message)
                } else {
                    paymentResultTextView.text = getString(R.string.payment_error)
                }

                navigateToPaymentListButton.visibility = View.GONE
                retryPaymentButton.visibility = View.VISIBLE
            }

        }
        binding.paymentResultLayout.visibility = View.VISIBLE
    }

    /**
     * Invoked when submit_payment_button is clicked.
     * Calls the viewModel method to make the payment
     * */
    fun onSubmitPaymentButtonClick() {
        hideKeyboard()

        if (!paymentSubmitted) {
            paymentSubmitted = true
            binding.submitPaymentButton.isEnabled = false
            viewModel.validatePayment(
                binding.paymentEmailEditText.text.toString(),
                binding.paymentAmountEditText.text.toString(),
                binding.paymentDescriptionEditText.text.toString()
            )
        }
    }

    /**
     * Invoked when retry_payment_button is clicked. Hides the payment_result_layout
     * */
    fun onRetryPaymentButtonClick() {
        paymentSubmitted = false
        binding.run {
            submitPaymentButton.isEnabled = true
            submitPaymentButton.text = getString(R.string.submit_payment)
            paymentResultLayout.visibility = View.GONE
        }

    }

    /**
     * Invoked when navigate_to_payment_list_button is clicked. Navigates to the PaymentFragment.
     * */
    fun onNavigateToPaymentListButtonClick() {
        FragmentNavigation().navigateFromPaymentToHomeFragment()
    }

    /** Returns true if all fields are valid, false otherwise*/
    private fun isPaymentValid(paymentValidationResult: PaymentValidationModel): Boolean {
        return paymentValidationResult.isAmountValid &&
                paymentValidationResult.isDescriptionValid &&
                paymentValidationResult.isEmailValid
    }

    /** Applies validation results to the fields */
    private fun applyPaymentValidation(paymentValidationResult: PaymentValidationModel) {
        paymentValidationResult.let {
            if (!it.isEmailValid) {
                binding.paymentEmailTextView.error = it.emailErrorText
            } else {
                binding.paymentEmailTextView.error = null
                binding.paymentEmailTextView.isErrorEnabled = false
            }

            if (!it.isAmountValid) {
                binding.paymentAmountTextView.error = it.amountErrorText
            } else {
                binding.paymentAmountTextView.error = null
                binding.paymentAmountTextView.isErrorEnabled = false
            }

            if (!it.isDescriptionValid) {
                binding.paymentDescriptionTextView.error = it.descriptionErrorText
            } else {
                binding.paymentDescriptionTextView.error = null
                binding.paymentDescriptionTextView.isErrorEnabled = false
            }
        }
    }

    /**
     * If payment has failed, pressing the back button will close the layer
     * but will not navigate to the previous fragment
     * */
    private fun setBackPressCallback() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true
            ) {
            override fun handleOnBackPressed() {
                if (binding.paymentResultLayout.visibility == View.VISIBLE &&
                    binding.retryPaymentButton.visibility == View.VISIBLE) {
                    onRetryPaymentButtonClick()
                } else {
                    FragmentNavigation().navigateToBack()
                }
            }
        })
    }


}