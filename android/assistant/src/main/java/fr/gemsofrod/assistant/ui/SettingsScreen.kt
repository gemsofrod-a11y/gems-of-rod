@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package fr.gemsofrod.assistant.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
    isSignedIn: Boolean,
    currentApiKey: String,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onSaveApiKey: (apiKey: String) -> Unit,
    onBack: () -> Unit,
) {
    var apiKey by remember(currentApiKey) { mutableStateOf(currentApiKey) }

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
            Text("Compte Gmail", style = MaterialTheme.typography.titleMedium)
            Text(
                if (isSignedIn) "Connecté à gemsofrod@gmail.com." else "Pas encore connecté.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )
            if (isSignedIn) {
                OutlinedButton(onClick = onSignOut) { Text("Se déconnecter") }
            } else {
                Button(onClick = onSignIn) { Text("Se connecter avec Google") }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

            Text("Clé API Anthropic", style = MaterialTheme.typography.titleMedium)
            Text(
                "Nécessaire pour que Saphir fonctionne (conversation, tri automatique des " +
                    "emails, devis). Trouvable sur console.anthropic.com.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("Clé API Anthropic") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true,
            )
            Button(onClick = { onSaveApiKey(apiKey) }) {
                Text("Enregistrer")
            }
        }
    }
}
