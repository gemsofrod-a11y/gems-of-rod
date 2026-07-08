package com.gemsofrod.app.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.gemsofrod.app.R
import com.gemsofrod.app.databinding.FragmentCartBinding
import com.gemsofrod.app.model.CartManager

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        observeCart()

        binding.btnDiscover.setOnClickListener {
            findNavController().navigate(R.id.shopFragment)
        }

        binding.btnCheckout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirmer la commande")
                .setMessage("Votre demande va être transmise à notre équipe qui vous contactera pour finaliser la commande.")
                .setPositiveButton("Confirmer") { _, _ ->
                    CartManager.clear()
                    Snackbar.make(
                        binding.root,
                        "Votre demande a bien été enregistrée. Nous vous contacterons très prochainement.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton("Annuler", null)
                .show()
        }
    }

    private fun setupRecycler() {
        cartAdapter = CartAdapter { productId ->
            CartManager.remove(productId)
        }
        binding.cartRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun observeCart() {
        CartManager.items.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items.toList())
            val isEmpty = items.isEmpty()
            binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.cartRecycler.visibility = if (isEmpty) View.GONE else View.VISIBLE
            binding.checkoutBar.visibility = if (isEmpty) View.GONE else View.VISIBLE
            if (!isEmpty) {
                binding.cartTotal.text = CartManager.formattedTotal
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
