package com.gemsofrod.app.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
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
            binding.productName.text = product.variety
            binding.productCategory.text = product.category
            binding.productPrice.text = product.formattedPrice
            binding.productImage.load(productImageFor(product)) { crossfade(true) }
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
            binding.productName.text = product.variety
            binding.productCategory.text = product.category
            binding.productOrigin.text = product.weightCarats?.let { "${it} ct" } ?: ""
            binding.productPrice.text = product.formattedPrice
            binding.productImage.load(productImageFor(product)) { crossfade(true) }
            binding.root.setOnClickListener { onProductClick(product) }
        }
    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}

fun productImageFor(product: Product): Int {
    val real = when (product.id) {
        "PIE-002" -> R.drawable.gem_pie002
        "PIE-003" -> R.drawable.gem_pie003
        "PIE-005" -> R.drawable.gem_pie005
        "PIE-006" -> R.drawable.gem_pie006
        "PIE-007" -> R.drawable.gem_pie007
        "PIE-014" -> R.drawable.gem_pie014
        "PIE-015" -> R.drawable.gem_pie015
        "PIE-016" -> R.drawable.gem_pie016
        "PIE-022" -> R.drawable.gem_pie022
        "PIE-032" -> R.drawable.gem_pie032
        else -> null
    }
    return real ?: gemImageFor(product)
}

fun gemImageFor(product: Product): Int = when (product.name.lowercase()) {
    "saphir" -> when {
        product.variety.contains("Padparadscha", ignoreCase = true) -> R.drawable.gem_placeholder_ruby
        else -> R.drawable.gem_placeholder_sapphire
    }
    "rubis" -> R.drawable.gem_placeholder_ruby
    "diamant" -> R.drawable.gem_placeholder_diamond
    "topaze" -> R.drawable.gem_placeholder_topaz
    "grenat" -> R.drawable.gem_placeholder_ruby
    "héliodore" -> R.drawable.gem_placeholder_topaz
    "spinelle" -> when {
        product.variety.contains("Bleu", ignoreCase = true) -> R.drawable.gem_placeholder_sapphire
        product.variety.contains("Rose", ignoreCase = true) -> R.drawable.gem_placeholder_ruby
        product.variety.contains("Rouge", ignoreCase = true) -> R.drawable.gem_placeholder_ruby
        product.variety.contains("Violet", ignoreCase = true) -> R.drawable.gem_placeholder_amethyst
        product.variety.contains("Lavande", ignoreCase = true) -> R.drawable.gem_placeholder_amethyst
        else -> R.drawable.gem_placeholder_amethyst
    }
    "péridot" -> R.drawable.gem_placeholder_emerald
    "zircon" -> R.drawable.gem_placeholder_diamond
    "iolite" -> R.drawable.gem_placeholder_sapphire
    "aigue-marine" -> R.drawable.gem_placeholder_sapphire
    "citrine" -> R.drawable.gem_placeholder_topaz
    "apatite" -> R.drawable.gem_placeholder_sapphire
    "bague", "pendentif" -> R.drawable.gem_placeholder_amethyst
    else -> R.drawable.gem_placeholder_sapphire
}
