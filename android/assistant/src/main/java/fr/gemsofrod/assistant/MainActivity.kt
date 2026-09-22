package fr.gemsofrod.assistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.gemsofrod.assistant.ui.HomeScreen
import fr.gemsofrod.assistant.ui.PendingScreen
import fr.gemsofrod.assistant.ui.SettingsScreen
import fr.gemsofrod.assistant.voice.TtsController
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: AssistantViewModel by viewModels()
    private var tts: TtsController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TtsController(this)
        viewModel.onSpeakReply = { text -> tts?.speak(text) }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AssistantApp(viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        tts?.shutdown()
        super.onDestroy()
    }
}

@androidx.compose.runtime.Composable
private fun AssistantApp(viewModel: AssistantViewModel) {
    val navController: NavHostController = rememberNavController()
    val state by viewModel.state.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    var hasRecordPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasRecordPermission = granted }

    // Notifications (email en attente trouvé par le tri automatique) —
    // permission requise à partir d'Android 13.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* rien à faire, best-effort */ }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val signInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> viewModel.handleSignInResult(result.data) }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spoken = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
        if (!spoken.isNullOrBlank()) {
            viewModel.sendVoiceText(spoken)
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    fun startListening() {
        if (!hasRecordPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }
        val intent = android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Dites votre demande…")
        }
        try {
            speechLauncher.launch(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            Toast.makeText(context, "Reconnaissance vocale indisponible sur cet appareil.", Toast.LENGTH_LONG).show()
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                state = state,
                onMicClick = { startListening() },
                onOpenPending = { navController.navigate("pending") },
                onOpenSettings = { navController.navigate("settings") },
            )
        }
        composable("pending") {
            PendingScreen(
                pendingItems = state.pendingItems,
                onBack = { navController.popBackStack() },
                onApprove = { id, edited -> viewModel.approvePending(id, edited) },
                onReject = { id -> viewModel.rejectPending(id) },
            )
        }
        composable("settings") {
            SettingsScreen(
                isSignedIn = state.isSignedIn,
                currentApiKey = state.anthropicApiKey,
                onSignIn = { signInLauncher.launch(viewModel.buildSignInIntent()) },
                onSignOut = { viewModel.signOut() },
                onSaveApiKey = { key -> viewModel.saveApiKey(key) },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
