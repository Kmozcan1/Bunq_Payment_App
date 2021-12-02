package com.kmozcan1.bunqpaymentapp.presentation

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */

// SetAdapter extension for better code readability
fun RecyclerView.setRecyclerView(
    layoutManager: RecyclerView.LayoutManager,
    adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
) {
    this.layoutManager = layoutManager
    this.adapter = adapter
}