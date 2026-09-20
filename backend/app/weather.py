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
    geo = requests.get(
        "https://geocoding-api.open-meteo.com/v1/search",
        params={"name": location, "count": 1, "language": "fr"},
        timeout=10,
    ).json()
    results = geo.get("results")
    if not results:
        return {"error": f"Lieu introuvable : « {location} »."}
    place = results[0]
    lat, lon = place["latitude"], place["longitude"]
    name = place.get("name", location)
    country = place.get("country", "")

    forecast = requests.get(
        "https://api.open-meteo.com/v1/forecast",
        params={
            "latitude": lat,
            "longitude": lon,
            "current": "temperature_2m,apparent_temperature,weather_code,"
                       "wind_speed_10m,relative_humidity_2m",
            "timezone": "auto",
        },
        timeout=10,
    ).json()
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
