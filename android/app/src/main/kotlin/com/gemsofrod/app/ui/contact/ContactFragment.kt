package com.gemsofrod.app.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.gemsofrod.app.R
import com.gemsofrod.app.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private var _binding: FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            if (validateForm()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.contact_success),
                    Snackbar.LENGTH_LONG
                ).show()
                clearForm()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.inputName.text.isNullOrBlank()) {
            binding.inputName.error = "Veuillez entrer votre nom"
            isValid = false
        }

        val email = binding.inputEmail.text?.toString() ?: ""
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.error = "Adresse e-mail invalide"
            isValid = false
        }

        if (binding.inputMessage.text.isNullOrBlank()) {
            binding.inputMessage.error = "Veuillez entrer votre message"
            isValid = false
        }

        return isValid
    }

    private fun clearForm() {
        binding.inputName.text = null
        binding.inputEmail.text = null
        binding.inputSubject.text = null
        binding.inputMessage.text = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
