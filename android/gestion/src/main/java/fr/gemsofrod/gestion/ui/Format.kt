package fr.gemsofrod.gestion.ui

import fr.gemsofrod.gestion.data.StockUnit
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val euroFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE).apply { maximumFractionDigits = 2 }
private val euroRound = NumberFormat.getCurrencyInstance(Locale.FRANCE).apply { maximumFractionDigits = 0 }
private val numberFormat = NumberFormat.getNumberInstance(Locale.FRANCE).apply { maximumFractionDigits = 2 }
private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE)

fun euros(value: Double): String = euroFormat.format(value)

/** Montant arrondi à l'euro (tuiles du tableau de bord). */
fun eurosRound(value: Double): String = euroRound.format(value)

/** Montant court pour les axes de graphiques : 1 250 € → « 1,3 k€ ». */
fun eurosShort(value: Double): String = when {
    value >= 1_000_000 -> numberFormat.format(value / 1_000_000).let { "$it M€" }
    value >= 1_000 -> NumberFormat.getNumberInstance(Locale.FRANCE)
        .apply { maximumFractionDigits = 1 }.format(value / 1_000) + " k€"
    else -> euroRound.format(value)
}

fun qty(value: Double): String = numberFormat.format(value)

fun qty(value: Double, unit: StockUnit): String = when (unit) {
    StockUnit.PIECE -> "${qty(value)} ${if (value > 1) "pièces" else "pièce"}"
    else -> "${qty(value)} ${unit.label}"
}

fun date(value: LocalDate): String = value.format(dateFormat)

/** Accepte la virgule française comme séparateur décimal. */
fun parseNumber(text: String): Double? = text.filterNot { it.isWhitespace() || it == '\u202F' || it == '\u00A0' }.replace(',', '.').toDoubleOrNull()

/** Affiche un nombre dans un champ de saisie, sans « ,0 » inutile. */
fun editable(value: Double): String =
    if (value == 0.0) "" else if (value % 1.0 == 0.0) value.toLong().toString() else value.toString().replace('.', ',')
