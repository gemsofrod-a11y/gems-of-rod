package com.gemsofrod.app.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.gemsofrod.app.databinding.FragmentShopBinding
import com.gemsofrod.app.model.Product
import com.gemsofrod.app.model.ProductRepository

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private lateinit var gridAdapter: ProductGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGrid()
        setupFilters()
    }

    private fun setupGrid() {
        gridAdapter = ProductGridAdapter { product ->
            navigateToDetail(product)
        }
        binding.shopRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gridAdapter
        }
        gridAdapter.submitList(ProductRepository.products)
    }

    private fun setupFilters() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val filtered = when (group.checkedChipId) {
                binding.chipCat4.id -> ProductRepository.getByCategory("Précieuse")
                binding.chipCat3.id -> ProductRepository.getByCategory("Fine noble")
                binding.chipCat2.id -> ProductRepository.getByCategory("Fine courante")
                binding.chipCat1.id -> ProductRepository.getByCategory("Commune")
                binding.chipBijou.id -> ProductRepository.getByCategory("Bijou")
                else -> ProductRepository.products
            }
            gridAdapter.submitList(filtered)
        }
    }

    private fun navigateToDetail(product: Product) {
        val action = ShopFragmentDirections.actionShopToProductDetail(product.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
