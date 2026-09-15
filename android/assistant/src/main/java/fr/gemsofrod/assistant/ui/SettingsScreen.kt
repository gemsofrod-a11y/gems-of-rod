@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package fr.gemsofrod.assistant.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    currentBaseUrl: String,
    currentApiToken: String,
    onBack: () -> Unit,
    onSave: (baseUrl: String, apiToken: String) -> Unit,
) {
    var baseUrl by remember(currentBaseUrl) { mutableStateOf(currentBaseUrl) }
    var apiToken by remember(currentApiToken) { mutableStateOf(currentApiToken) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Réglages") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Adresse du serveur assistant (backend/), par ex. https://mon-serveur.exemple.com")
            OutlinedTextField(
                value = baseUrl,
                onValueChange = { baseUrl = it },
                label = { Text("URL du serveur") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
                singleLine = true,
            )
            Text("Jeton d'accès (ASSISTANT_API_TOKEN configuré côté serveur)")
            OutlinedTextField(
                value = apiToken,
                onValueChange = { apiToken = it },
                label = { Text("Jeton d'accès") },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
                singleLine = true,
            )
            Button(onClick = { onSave(baseUrl, apiToken) }) {
                Text("Enregistrer")
            }
        }
    }
}
