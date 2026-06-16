package com.gemsofrod.app.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gemsofrod.app.R
import com.gemsofrod.app.databinding.ItemProductCardBinding
import com.gemsofrod.app.databinding.ItemProductGridBinding
import com.gemsofrod.app.model.Product

class ProductCardAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductCardAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemProductCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productCategory.text = product.category
            binding.productPrice.text = product.formattedPrice
            binding.productImage.setImageResource(gemImageFor(product.id))
            binding.root.setOnClickListener { onProductClick(product) }
        }
    }
}

class ProductGridAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductGridAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemProductGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productName.text = product.name
            binding.productCategory.text = product.category
            binding.productOrigin.text = product.origin
            binding.productPrice.text = product.formattedPrice
            binding.productImage.setImageResource(gemImageFor(product.id))
            binding.root.setOnClickListener { onProductClick(product) }
        }
    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}

fun gemImageFor(productId: String): Int = when {
    productId.startsWith("SAP") -> R.drawable.gem_placeholder_sapphire
    productId.startsWith("EME") -> R.drawable.gem_placeholder_emerald
    productId.startsWith("RUB") -> R.drawable.gem_placeholder_ruby
    productId.startsWith("AME") -> R.drawable.gem_placeholder_amethyst
    productId.startsWith("TOP") -> R.drawable.gem_placeholder_topaz
    productId.startsWith("DIA") -> R.drawable.gem_placeholder_diamond
    else -> R.drawable.gem_placeholder_sapphire
}
