package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Supplier
import fr.gemsofrod.encyclopedie.data.SupplierRepository
import fr.gemsofrod.encyclopedie.data.SupplierType

/** Ajout ou édition d'une fiche fournisseur — même patron que [ClientFormScreen], avec un sélecteur de type en plus. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierFormScreen(supplierId: String?, onSaveComplete: () -> Unit, onBackClick: () -> Unit) {
    val existing = remember(supplierId) { supplierId?.let { SupplierRepository.supplierById(it) } }

    var nom by remember { mutableStateOf(existing?.nom ?: "") }
    var type by remember { mutableStateOf(existing?.type ?: SupplierType.NEGOCIANT) }
    var telephone by remember { mutableStateOf(existing?.telephone ?: "") }
    var email by remember { mutableStateOf(existing?.email ?: "") }
    var pays by remember { mutableStateOf(existing?.pays ?: "") }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(if (existing == null) R.string.supplier_form_title_add else R.string.supplier_form_title_edit)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(
                        enabled = nom.isNotBlank(),
                        onClick = {
                            val supplier = Supplier(
                                id = existing?.id ?: "",
                                nom = nom.trim(),
                                type = type,
                                telephone = telephone.trim(),
                                email = email.trim(),
                                pays = pays.trim(),
                                notes = notes.trim(),
                                createdAtMillis = existing?.createdAtMillis ?: 0L
                            )
                            if (existing == null) SupplierRepository.addSupplier(supplier) else SupplierRepository.updateSupplier(supplier)
                            onSaveComplete()
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.supplier_save_button))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SupplierField(label = stringResource(R.string.client_field_nom_label), value = nom, onValueChange = { nom = it })

            Text(
                text = stringResource(R.string.supplier_field_type_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SupplierType.entries.forEach { option ->
                    FilterChip(
                        selected = option == type,
                        onClick = { type = option },
                        label = { Text(stringResource(option.labelRes)) }
                    )
                }
            }

            SupplierField(
                label = stringResource(R.string.client_field_telephone_label),
                value = telephone,
                onValueChange = { telephone = it },
                keyboardType = KeyboardType.Phone
            )
            SupplierField(
                label = stringResource(R.string.client_field_email_label),
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )
            SupplierField(
                label = stringResource(R.string.supplier_field_pays_label),
                value = pays,
                onValueChange = { pays = it }
            )
            SupplierField(
                label = stringResource(R.string.client_field_notes_label),
                value = notes,
                onValueChange = { notes = it },
                singleLine = false
            )
        }
    }
}

@Composable
private fun SupplierField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}
