"""Météo en direct via Open-Meteo (API publique, gratuite, sans clé) : une
étape de géocodage (nom de lieu -> coordonnées) puis la météo actuelle à ces
coordonnées.
"""
import requests

_WMO_DESCRIPTIONS = {
    0: "ciel dégagé", 1: "plutôt dégagé", 2: "partiellement nuageux", 3: "couvert",
    45: "brouillard", 48: "brouillard givrant",
    51: "bruine légère", 53: "bruine modérée", 55: "bruine forte",
    56: "bruine verglaçante légère", 57: "bruine verglaçante forte",
    61: "pluie légère", 63: "pluie modérée", 65: "forte pluie",
    66: "pluie verglaçante légère", 67: "pluie verglaçante forte",
    71: "neige légère", 73: "neige modérée", 75: "forte neige", 77: "grains de neige",
    80: "averses légères", 81: "averses modérées", 82: "fortes averses",
    85: "averses de neige légères", 86: "averses de neige fortes",
    95: "orage", 96: "orage avec grêle légère", 99: "orage avec forte grêle",
}


def get_weather(location: str) -> dict:
    # Toute panne réseau/API est convertie ici en {"error": ...} propre plutôt
    # que de laisser une exception requests brute remonter jusqu'au dispatch
    # de l'agent (message générique et peu naturel à relire à voix haute).
    try:
        geo_resp = requests.get(
            "https://geocoding-api.open-meteo.com/v1/search",
            params={"name": location, "count": 1, "language": "fr"},
            timeout=10,
        )
        geo_resp.raise_for_status()
        geo = geo_resp.json()
    except requests.exceptions.RequestException:
        return {"error": "Service de géolocalisation météo indisponible pour le moment."}

    results = geo.get("results")
    if not results:
        return {"error": f"Lieu introuvable : « {location} »."}
    place = results[0]
    lat, lon = place.get("latitude"), place.get("longitude")
    if lat is None or lon is None:
        return {"error": f"Coordonnées introuvables pour « {location} »."}
    name = place.get("name", location)
    country = place.get("country", "")

    try:
        forecast_resp = requests.get(
            "https://api.open-meteo.com/v1/forecast",
            params={
                "latitude": lat,
                "longitude": lon,
                "current": "temperature_2m,apparent_temperature,weather_code,"
                           "wind_speed_10m,relative_humidity_2m",
                "timezone": "auto",
            },
            timeout=10,
        )
        forecast_resp.raise_for_status()
        forecast = forecast_resp.json()
    except requests.exceptions.RequestException:
        return {"error": "Service météo indisponible pour le moment."}

    current = forecast.get("current", {})
    code = current.get("weather_code")
    return {
        "location": f"{name}, {country}".strip(", "),
        "temperature_c": current.get("temperature_2m"),
        "feels_like_c": current.get("apparent_temperature"),
        "condition": _WMO_DESCRIPTIONS.get(code, "conditions inconnues"),
        "humidity_pct": current.get("relative_humidity_2m"),
        "wind_kmh": current.get("wind_speed_10m"),
    }
