package com.gemsofrod.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gemsofrod.app.R
import com.gemsofrod.app.databinding.FragmentHomeBinding
import com.gemsofrod.app.model.Product
import com.gemsofrod.app.model.ProductRepository
import com.gemsofrod.app.ui.shop.ProductCardAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var featuredAdapter: ProductCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFeaturedProducts()
        setupClickListeners()
    }

    private fun setupFeaturedProducts() {
        featuredAdapter = ProductCardAdapter { product ->
            navigateToProductDetail(product)
        }

        binding.featuredRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = featuredAdapter
        }

        featuredAdapter.submitList(ProductRepository.getFeatured())
    }

    private fun setupClickListeners() {
        binding.heroCtaButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_shop)
        }
    }

    private fun navigateToProductDetail(product: Product) {
        val action = HomeFragmentDirections.actionHomeToProductDetail(product.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
