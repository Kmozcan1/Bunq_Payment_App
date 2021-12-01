package com.kmozcan1.bunqpaymentapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.PaymentListItemBinding

/**
 * Created by Kadir Mert Özcan on 11/28/2021.
 */
class PaymentListAdapter constructor(
    private val context: Context,
    private val paymentList: MutableList<Payment>,
    private val listener: (Payment) -> Unit
) :
    RecyclerView.Adapter<PaymentListAdapter.PaymentListItemViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentListAdapter.PaymentListItemViewHolder {
        val binding = PaymentListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentListItemViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: PaymentListAdapter.PaymentListItemViewHolder,
        position: Int
    ) {
        holder.bind(paymentList[position])
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    inner class PaymentListItemViewHolder(
        private val binding: PaymentListItemBinding,
        val listener: (Payment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: Payment) {
            binding.run {
                recipientListItemTextView.text = payment.counterpartyAlias.displayName
                amountListItemTextView.text = context.getString(R.string.payment_amount,
                    payment.amount.value, payment.amount.currency)
                root.setOnClickListener{
                    listener(payment)
                }
            }
        }
    }

    /** Adds a new batch of payments to the RecyclerView */
    fun addPayments(repositories: List<Payment>) {
        val startPosition = itemCount
        paymentList.addAll(repositories)
        notifyItemRangeInserted(startPosition, paymentList.size)
    }


}