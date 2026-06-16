package com.gemsofrod.app.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gemsofrod.app.databinding.ItemCartBinding
import com.gemsofrod.app.model.CartItem
import com.gemsofrod.app.ui.shop.gemImageFor

class CartAdapter(
    private val onRemove: (String) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.cartItemName.text = item.product.name
            binding.cartItemPrice.text = if (item.quantity > 1)
                "${item.quantity} × ${item.product.formattedPrice}"
            else
                item.product.formattedPrice
            binding.cartItemImage.setImageResource(gemImageFor(item.product))
            binding.cartItemRemove.setOnClickListener { onRemove(item.product.id) }
        }
    }

    private class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) =
            oldItem.product.id == newItem.product.id
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) =
            oldItem == newItem
    }
}
