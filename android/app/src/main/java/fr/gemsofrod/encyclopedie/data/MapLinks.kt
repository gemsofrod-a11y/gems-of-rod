package fr.gemsofrod.encyclopedie.data

import java.net.URLEncoder

/**
 * Construit un lien de recherche Google Maps pour un nom de lieu (mine,
 * région, pays). On ne stocke volontairement pas de coordonnées GPS
 * approximatives pour éviter d'afficher une localisation erronée : Google
 * Maps résout lui-même le lieu recherché.
 */
fun googleMapsSearchUrl(placeName: String): String {
    val encoded = URLEncoder.encode(placeName, "UTF-8")
    return "https://www.google.com/maps/search/?api=1&query=$encoded"
}
