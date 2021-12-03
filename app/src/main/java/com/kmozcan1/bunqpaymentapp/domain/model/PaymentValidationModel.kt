package com.kmozcan1.bunqpaymentapp.domain.model

/**
 * Created by Kadir Mert Özcan on 03-Dec-21.
 *
 * Data class
 */
data class PaymentValidationModel (
    var isEmailValid: Boolean = false,
    var emailErrorText: String? = null,
    var isAmountValid: Boolean = false,
    var amountErrorText: String? = null,
    var isDescriptionValid: Boolean = false,
    var descriptionErrorText: String? = null
) {
    companion object {
        const val EMPTY_EMAIL_ERROR_TEXT = "Email cannot be empty"
        const val INVALID_EMAIL_ERROR_TEXT = "Invalid email"
        const val EMPTY_AMOUNT_ERROR_TEXT = "Amount cannot be empty"
        const val INVALID_AMOUNT_ERROR_TEXT = "Invalid amount"
        const val EMPTY_DESCRIPTION_ERROR_TEXT = "Description cannot be empty"

    }

    fun validatePaymentRequest(payment: PaymentModel) : PaymentValidationModel {
        // Validate email
        if (payment.email == "") {
            isEmailValid = false
            emailErrorText = EMPTY_EMAIL_ERROR_TEXT
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(payment.email).matches()) {
            isEmailValid = false
            emailErrorText = INVALID_EMAIL_ERROR_TEXT
        } else {
            isEmailValid = true
        }

        // Validate amount
        if (payment.amount == "") {
            isAmountValid = false
            amountErrorText = EMPTY_AMOUNT_ERROR_TEXT
        } else if (!payment.amount.all { it in '0'..'9' || it == '.' } &&
            (payment.amount.count { it == '.' } > 1)) {
                isAmountValid = false
                amountErrorText = INVALID_AMOUNT_ERROR_TEXT
        } else {
            isAmountValid = true
        }

        // Validate description
        if (payment.description == "") {
            isDescriptionValid = false
            descriptionErrorText = EMPTY_DESCRIPTION_ERROR_TEXT
        } else {
            isDescriptionValid = true
        }

        return this
    }
}