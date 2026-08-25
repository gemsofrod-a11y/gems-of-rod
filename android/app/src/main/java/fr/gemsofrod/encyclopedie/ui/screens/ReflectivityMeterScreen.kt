package fr.gemsofrod.encyclopedie.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.hardware.camera2.CaptureRequest
import android.view.ViewGroup
import androidx.camera.camera2.interop.Camera2CameraControl
import androidx.camera.camera2.interop.CaptureRequestOptions
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.CalibrationPoint
import fr.gemsofrod.encyclopedie.data.ReflectivityCalibrationRepository
import fr.gemsofrod.encyclopedie.data.ReflectivityEstimate
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Valeurs de départ pour le verrouillage manuel de l'exposition — à ajuster
// lors de la validation sur device réel (Phase F du plan), aucun moyen de
// les calibrer depuis ce sandbox de développement sans caméra.
private const val FIXED_EXPOSURE_NANOS = 16_666_666L // ~1/60s
private const val FIXED_ISO = 100

/**
 * Mode réflectivité (voir ReflectivityMeter.kt) : mesure de l'indice de
 * réfraction par comparaison de brillance réfléchie, sans hémicylindre ni
 * liquide de contact. Précision indicative — un disclaimer accompagne
 * chaque résultat, jamais présenté comme une mesure de laboratoire.
 *
 * Exposition verrouillée manuellement (Camera2CameraControl) et torche
 * continue allumée pendant toute la session, pour que la brillance mesurée
 * ne dépende que de la pierre et non de l'auto-exposition du téléphone —
 * sans capot sombre pour bloquer la lumière ambiante, la mesure reste peu
 * fiable (voir l'avertissement affiché à l'écran).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectivityMeterScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraReady by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }
    var captureError by remember { mutableStateOf<String?>(null) }

    var calibrationRecord by remember { mutableStateOf(ReflectivityCalibrationRepository.current()) }
    var recalibrating by remember { mutableStateOf(calibrationRecord == null) }

    var pointARi by remember { mutableStateOf("") }
    var pointABrightness by remember { mutableStateOf<Double?>(null) }
    var pointBRi by remember { mutableStateOf("") }
    var pointBBrightness by remember { mutableStateOf<Double?>(null) }

    var lastEstimate by remember { mutableStateOf<ReflectivityEstimate?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.reflectivity_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.reflectivity_disclaimer),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )

            if (!hasCameraPermission) {
                Text(
                    text = stringResource(R.string.reflectivity_permission_rationale),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text(stringResource(R.string.reflectivity_permission_request))
                }
                return@Column
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CameraPreview(
                        onImageCaptureReady = { capture -> imageCapture = capture },
                        onCameraReady = { cameraReady = true }
                    )
                    if (!cameraReady) {
                        CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                    }
                }
            }

            captureError?.let { message ->
                Text(text = message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            val calibration = calibrationRecord
            if (recalibrating || calibration == null) {
                Text(
                    text = stringResource(R.string.reflectivity_calibration_intro),
                    style = MaterialTheme.typography.bodyMedium
                )
                CalibrationPointRow(
                    labelRes = R.string.reflectivity_point_a_label,
                    knownRi = pointARi,
                    onKnownRiChange = { pointARi = it },
                    brightness = pointABrightness,
                    capturing = isCapturing,
                    captureEnabled = cameraReady && imageCapture != null && pointARi.toDoubleOrNull() != null,
                    onCaptureClick = {
                        val capture = imageCapture ?: return@CalibrationPointRow
                        isCapturing = true
                        captureError = null
                        coroutineScope.launch {
                            runCatching { captureBrightness(context, capture) }
                                .onSuccess { pointABrightness = it }
                                .onFailure { captureError = it.message ?: context.getString(R.string.reflectivity_capture_error) }
                            isCapturing = false
                        }
                    }
                )
                CalibrationPointRow(
                    labelRes = R.string.reflectivity_point_b_label,
                    knownRi = pointBRi,
                    onKnownRiChange = { pointBRi = it },
                    brightness = pointBBrightness,
                    capturing = isCapturing,
                    captureEnabled = cameraReady && imageCapture != null && pointBRi.toDoubleOrNull() != null,
                    onCaptureClick = {
                        val capture = imageCapture ?: return@CalibrationPointRow
                        isCapturing = true
                        captureError = null
                        coroutineScope.launch {
                            runCatching { captureBrightness(context, capture) }
                                .onSuccess { pointBBrightness = it }
                                .onFailure { captureError = it.message ?: context.getString(R.string.reflectivity_capture_error) }
                            isCapturing = false
                        }
                    }
                )

                val riA = pointARi.toDoubleOrNull()
                val riB = pointBRi.toDoubleOrNull()
                val canSave = riA != null && riB != null && pointABrightness != null && pointBBrightness != null

                Button(
                    onClick = {
                        val brightnessA = pointABrightness ?: return@Button
                        val brightnessB = pointBBrightness ?: return@Button
                        val a = riA ?: return@Button
                        val b = riB ?: return@Button
                        runCatching {
                            ReflectivityCalibrationRepository.save(
                                pointA = CalibrationPoint(a, brightnessA),
                                pointB = CalibrationPoint(b, brightnessB),
                                calibratedAtEpochMillis = System.currentTimeMillis()
                            )
                        }.onSuccess {
                            calibrationRecord = ReflectivityCalibrationRepository.current()
                            recalibrating = false
                            pointARi = ""
                            pointBRi = ""
                            pointABrightness = null
                            pointBBrightness = null
                        }.onFailure {
                            captureError = it.message ?: context.getString(R.string.reflectivity_capture_error)
                        }
                    },
                    enabled = canSave,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.reflectivity_save_calibration))
                }
            } else {
                Text(
                    text = stringResource(R.string.reflectivity_calibrated_status),
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = {
                        val capture = imageCapture ?: return@Button
                        isCapturing = true
                        captureError = null
                        coroutineScope.launch {
                            runCatching { captureBrightness(context, capture) }
                                .onSuccess { brightness -> lastEstimate = calibration.calibration.estimate(brightness) }
                                .onFailure { captureError = it.message ?: context.getString(R.string.reflectivity_capture_error) }
                            isCapturing = false
                        }
                    },
                    enabled = cameraReady && imageCapture != null && !isCapturing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.reflectivity_measure))
                }

                lastEstimate?.let { estimate ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.reflectivity_result_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "%.2f – %.2f".format(estimate.refractiveIndexLow, estimate.refractiveIndexHigh),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        recalibrating = true
                        lastEstimate = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.reflectivity_recalibrate))
                }
            }
        }
    }
}

@Composable
private fun CalibrationPointRow(
    labelRes: Int,
    knownRi: String,
    onKnownRiChange: (String) -> Unit,
    brightness: Double?,
    capturing: Boolean,
    captureEnabled: Boolean,
    onCaptureClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = stringResource(labelRes), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = knownRi,
            onValueChange = onKnownRiChange,
            label = { Text(stringResource(R.string.reflectivity_known_ri_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onCaptureClick, enabled = captureEnabled && !capturing, modifier = Modifier.fillMaxWidth()) {
            Text(
                if (brightness != null) stringResource(R.string.reflectivity_recapture)
                else stringResource(R.string.reflectivity_capture)
            )
        }
    }
}

@Composable
private fun CameraPreview(
    onImageCaptureReady: (ImageCapture) -> Unit,
    onCameraReady: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    DisposableEffect(previewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
            val imageCapture = ImageCapture.Builder().build()

            cameraProvider.unbindAll()
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )

            // Éclairage constant pendant toute la session plutôt qu'un flash
            // ponctuel : évite les écarts de synchronisation entre torche et
            // capture qui fausseraient la comparaison de brillance.
            camera.cameraControl.enableTorch(true)

            // Verrouillage manuel de l'exposition : sans cela, l'auto-exposition
            // du téléphone compenserait les écarts de brillance entre pierres,
            // ce qui invaliderait toute la mesure par réflectivité.
            val captureRequestOptions = CaptureRequestOptions.Builder()
                .setCaptureRequestOption(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF)
                .setCaptureRequestOption(CaptureRequest.SENSOR_EXPOSURE_TIME, FIXED_EXPOSURE_NANOS)
                .setCaptureRequestOption(CaptureRequest.SENSOR_SENSITIVITY, FIXED_ISO)
                .build()
            Camera2CameraControl.from(camera.cameraControl).setCaptureRequestOptions(captureRequestOptions)

            onImageCaptureReady(imageCapture)
            onCameraReady()
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            runCatching { ProcessCameraProvider.getInstance(context).get().unbindAll() }
        }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}

/** Capture une photo et retourne la brillance médiane (0..255) d'une zone
 * centrale de l'image — la médiane plutôt que la moyenne pour rester robuste
 * aux quelques pixels de reflet ponctuel (sparkle) que la facette peut
 * produire, sans fausser la lecture d'ensemble. */
private suspend fun captureBrightness(context: android.content.Context, imageCapture: ImageCapture): Double {
    val outputFile = File(context.cacheDir, "reflectivity_capture.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
    val executor: Executor = ContextCompat.getMainExecutor(context)

    suspendCancellableCoroutine<Unit> { continuation ->
        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    continuation.resume(Unit)
                }

                override fun onError(exception: ImageCaptureException) {
                    continuation.resumeWithException(exception)
                }
            }
        )
    }

    val brightness = medianCentralBrightness(outputFile)
    runCatching { outputFile.delete() }
    return brightness
}

private fun medianCentralBrightness(file: File): Double {
    // Décodage réduit : on n'a besoin que d'une estimation de brillance, pas
    // de la pleine résolution — évite de charger une image potentiellement
    // volumineuse en mémoire pour un simple calcul de médiane.
    val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, boundsOptions)
    val targetMaxDimension = 200
    var sampleSize = 1
    while (boundsOptions.outWidth / sampleSize > targetMaxDimension || boundsOptions.outHeight / sampleSize > targetMaxDimension) {
        sampleSize *= 2
    }

    val bitmap = BitmapFactory.decodeFile(
        file.absolutePath,
        BitmapFactory.Options().apply { inSampleSize = sampleSize }
    ) ?: throw IllegalStateException("Impossible de décoder la photo capturée.")

    val cropSize = (minOf(bitmap.width, bitmap.height) * 0.5).toInt().coerceAtLeast(1)
    val left = (bitmap.width - cropSize) / 2
    val top = (bitmap.height - cropSize) / 2

    val lumaValues = ArrayList<Int>(cropSize * cropSize)
    for (y in top until top + cropSize) {
        for (x in left until left + cropSize) {
            val pixel = bitmap.getPixel(x, y)
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            lumaValues.add((0.299 * r + 0.587 * g + 0.114 * b).toInt())
        }
    }
    bitmap.recycle()

    lumaValues.sort()
    return lumaValues[lumaValues.size / 2].toDouble()
}
