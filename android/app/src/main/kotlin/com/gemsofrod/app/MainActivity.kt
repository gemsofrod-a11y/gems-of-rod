package com.gemsofrod.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.gemsofrod.app.databinding.ActivityMainBinding
import com.gemsofrod.app.model.CartManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cartBadge: BadgeDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = destination.id != R.id.productDetailFragment
            binding.bottomNavigation.visibility = if (showBottomNav) View.VISIBLE else View.GONE
        }

        setupCartBadge()
    }

    private fun setupCartBadge() {
        cartBadge = binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment)
        cartBadge?.isVisible = false

        CartManager.items.observe(this) { items ->
            val count = items.sumOf { it.quantity }
            if (count > 0) {
                cartBadge?.isVisible = true
                cartBadge?.number = count
            } else {
                cartBadge?.isVisible = false
            }
        }
    }
}
