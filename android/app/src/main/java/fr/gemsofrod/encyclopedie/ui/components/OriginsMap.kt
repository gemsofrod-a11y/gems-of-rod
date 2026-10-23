package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.GeoPoint

/**
 * Carte Google Maps intégrée montrant un repère par point fourni.
 *
 * Les coordonnées viennent de [fr.gemsofrod.encyclopedie.data.CountryCoordinates]
 * et [fr.gemsofrod.encyclopedie.data.LocalityCoordinates] : des positions
 * approximatives (centre du pays ou localité connue), pas des coordonnées
 * GPS relevées sur site — suffisant pour situer une origine à l'échelle
 * d'une carte du monde ou d'une région, jamais pour une précision métrique
 * (voir [ApproximateLocationCaption], à afficher sous toute carte issue de
 * ces données).
 *
 * N'affiche la carte que si Google Play Services est disponible sur
 * l'appareil ([isGoogleMapsAvailable]) : sans lui (appareil sans Play
 * Services, ou exécution sous Robolectric dans les tests de capture
 * d'écran — ni l'un ni l'autre ne l'exposent), `GoogleMap` échouerait au
 * lieu d'afficher une carte vide ; on retombe alors sur un simple message,
 * la liste textuelle des origines au-dessus restant de toute façon
 * consultable et cliquable (ouverture dans l'app Maps du système).
 */
@Composable
fun OriginsMap(
    points: List<GeoPoint>,
    modifier: Modifier = Modifier,
    mapHeight: Dp = 200.dp,
    onMarkerClick: ((GeoPoint) -> Unit)? = null
) {
    if (points.isEmpty()) return

    val context = LocalContext.current
    val mapsAvailable = remember { isGoogleMapsAvailable(context) }

    if (!mapsAvailable) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(mapHeight)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.map_unavailable),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(points[0].lat, points[0].lng), if (points.size == 1) 4f else 1f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(mapHeight)
            .clip(RoundedCornerShape(16.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(mapHeight),
            cameraPositionState = cameraPositionState
        ) {
            points.forEach { point ->
                Marker(
                    state = MarkerState(position = LatLng(point.lat, point.lng)),
                    title = point.label,
                    onClick = {
                        onMarkerClick?.invoke(point)
                        false
                    }
                )
            }
        }
    }

    if (points.size > 1) {
        // Recadre la caméra sur l'ensemble des repères une fois la carte
        // prête : un simple centre+zoom fixe (ci-dessus) n'englobe pas des
        // origines éloignées (ex. Birmanie + Madagascar).
        FitBoundsEffect(points, cameraPositionState)
    }
}

private fun isGoogleMapsAvailable(context: android.content.Context): Boolean {
    // Robolectric (ScreenshotTest) tourne sur la JVM sans Play Services réel ;
    // la classe GoogleApiAvailability est présente mais son vrai comportement
    // ne l'est pas, donc on l'évite complètement plutôt que de dépendre d'un
    // code de retour non garanti dans cet environnement.
    if (android.os.Build.FINGERPRINT.contains("robolectric", ignoreCase = true)) return false
    return runCatching {
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
    }.getOrDefault(false)
}

@Composable
private fun FitBoundsEffect(points: List<GeoPoint>, cameraPositionState: CameraPositionState) {
    LaunchedEffect(points) {
        val builder = LatLngBounds.Builder()
        points.forEach { builder.include(LatLng(it.lat, it.lng)) }
        runCatching {
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(builder.build(), 48))
        }
    }
}

/**
 * Légende courte à afficher sous une [OriginsMap] : rappelle que les
 * positions sont approximatives (centre de pays/région, pas un relevé GPS).
 */
@Composable
fun ApproximateLocationCaption(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.map_approximate_location),
        style = MaterialTheme.typography.labelSmall,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(top = 4.dp)
    )
}
