package com.gemsofrod.app.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.gemsofrod.app.R
import com.gemsofrod.app.databinding.FragmentProductDetailBinding
import com.gemsofrod.app.model.CartManager
import com.gemsofrod.app.model.ProductRepository

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = ProductRepository.getById(args.productId) ?: run {
            findNavController().popBackStack()
            return
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.collapsingToolbar.title = product.variety
        binding.detailCategory.text = product.category
        binding.detailName.text = product.variety
        binding.detailPrice.text = product.formattedPrice
        binding.detailDescription.text = product.description
        binding.detailImage.setImageResource(gemImageFor(product))

        binding.specOriginValue.text = product.origin ?: "—"
        binding.specWeightValue.text = product.weightCarats?.let { "$it ct" } ?: "—"
        binding.specTreatmentValue.text = product.cut ?: "—"
        binding.specCertValue.text = product.color ?: product.clarity ?: "—"

        binding.btnAddToCart.setOnClickListener {
            CartManager.add(product)
            Snackbar.make(
                binding.root,
                "« ${product.name} » ajouté au panier",
                Snackbar.LENGTH_LONG
            ).setAction("Voir le panier") {
                findNavController().navigate(R.id.cartFragment)
            }.show()
        }

        binding.btnContactExpert.setOnClickListener {
            findNavController().navigate(R.id.contactFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
