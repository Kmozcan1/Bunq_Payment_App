package com.kmozcan1.bunqpaymentapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunq.sdk.model.generated.endpoint.Payment
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.PaymentListItemBinding

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 *
 * PagingDataAdapter class for the payment_list_recycle_view
 */
class PaymentListAdapter constructor(
    private val listener: (Payment) -> Unit
) : PagingDataAdapter<Payment,
        PaymentListAdapter.PaymentListItemViewHolder>(PaymentListDiffCallback()) {


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
        val payment = getItem(position)
        payment?.let { holder.bind(it) }
    }

    class PaymentListDiffCallback : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class PaymentListItemViewHolder(
        private val binding: PaymentListItemBinding,
        val listener: (Payment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: Payment) {
            binding.run {
                recipientListItemTextView.text = payment.counterpartyAlias.displayName
                amountListItemTextView.text = root.context.getString(R.string.payment_amount,
                    payment.amount.value, payment.amount.currency)
                descriptionListItemTextView.text = payment.description
                root.setOnClickListener{
                    listener(payment)
                }
            }
        }
    }
}