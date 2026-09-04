package fr.gemsofrod.encyclopedie.ui.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Client
import fr.gemsofrod.encyclopedie.data.ClientRepository

/** Ajout ou édition d'une fiche client — même patron que [StockFormScreen], en plus simple (pas de photo). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientFormScreen(clientId: String?, onSaveComplete: () -> Unit, onBackClick: () -> Unit) {
    val existing = remember(clientId) { clientId?.let { ClientRepository.clientById(it) } }

    var nom by remember { mutableStateOf(existing?.nom ?: "") }
    var telephone by remember { mutableStateOf(existing?.telephone ?: "") }
    var email by remember { mutableStateOf(existing?.email ?: "") }
    var adresse by remember { mutableStateOf(existing?.adresse ?: "") }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }
    var estVip by remember { mutableStateOf(existing?.estVip ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(if (existing == null) R.string.client_form_title_add else R.string.client_form_title_edit)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(
                        enabled = nom.isNotBlank(),
                        onClick = {
                            val client = Client(
                                id = existing?.id ?: "",
                                nom = nom.trim(),
                                telephone = telephone.trim(),
                                email = email.trim(),
                                adresse = adresse.trim(),
                                notes = notes.trim(),
                                createdAtMillis = existing?.createdAtMillis ?: 0L,
                                estVip = estVip,
                                dernierContactMillis = existing?.dernierContactMillis
                            )
                            if (existing == null) ClientRepository.addClient(client) else ClientRepository.updateClient(client)
                            onSaveComplete()
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.client_save_button))
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
            ClientField(label = stringResource(R.string.client_field_nom_label), value = nom, onValueChange = { nom = it })
            ClientField(
                label = stringResource(R.string.client_field_telephone_label),
                value = telephone,
                onValueChange = { telephone = it },
                keyboardType = KeyboardType.Phone
            )
            ClientField(
                label = stringResource(R.string.client_field_email_label),
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )
            ClientField(
                label = stringResource(R.string.client_field_adresse_label),
                value = adresse,
                onValueChange = { adresse = it },
                singleLine = false
            )
            ClientField(
                label = stringResource(R.string.client_field_notes_label),
                value = notes,
                onValueChange = { notes = it },
                singleLine = false
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.client_field_vip_label), style = MaterialTheme.typography.bodyLarge)
                Switch(checked = estVip, onCheckedChange = { estVip = it })
            }
        }
    }
}

@Composable
private fun ClientField(
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
