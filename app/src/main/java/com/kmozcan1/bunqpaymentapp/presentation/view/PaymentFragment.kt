package com.kmozcan1.bunqpaymentapp.presentation.view

import android.view.View
import androidx.lifecycle.Observer
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.PaymentFragmentBinding
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
    }

    override fun observeLiveData() {
        viewModel.viewState.observe(this, observeViewState())
    }

    /** Observes the state of the fragment */
    private fun observeViewState() = Observer<PaymentViewState> { viewState ->
        when (viewState) {
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
    private fun showPaymentResult(isSuccess: Boolean) {
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
                paymentResultTextView.text = getString(R.string.payment_error)
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
        if (!paymentSubmitted) {
            paymentSubmitted = true
            binding.submitPaymentButton.isEnabled = false
            if (validatePayment()) {
                viewModel.submitPayment(
                    binding.paymentEmailEditText.text.toString(),
                    binding.paymentAmountEditText.text.toString(),
                    binding.paymentDescriptionEditText.text.toString()
                )
            }
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

    //region Validation
    /** Validates the fields */
    private fun validatePayment() : Boolean {
        var isValid = true
        val email = binding.paymentEmailEditText.text.toString()
        val amount = binding.paymentAmountEditText.text.toString()
        val description = binding.paymentDescriptionEditText.text.toString()

        // Validate email
        if (email == "") {
            binding.paymentEmailTextView.error = getString(R.string.empty_email_warning)
            isValid = false
        } else {
            binding.paymentEmailTextView.error = null
            binding.paymentEmailTextView.isErrorEnabled = false
        }

        // Validate amount
        if (amount == "") {
            binding.paymentAmountTextView.error = getString(R.string.empty_amount_warning)
            isValid = false
        } else if (!amount.all { it in '0'..'9' || it == '.' || it == ',' } &&
            (amount.count { it == '.' } + amount.count { it == ',' } > 1)) {
            binding.paymentAmountTextView.error = getString(R.string.invalid_amount_warning)
            isValid = false
        } else {
            binding.paymentAmountTextView.error = null
            binding.paymentAmountTextView.isErrorEnabled = false
        }

        // Validate description
        if (description == "") {
            binding.paymentDescriptionTextView.error = getString(R.string.empty_description_warning)
            isValid = false
        } else {
            binding.paymentDescriptionTextView.error = null
            binding.paymentDescriptionTextView.isErrorEnabled = false
        }

        return isValid
    }
    //endregion

}