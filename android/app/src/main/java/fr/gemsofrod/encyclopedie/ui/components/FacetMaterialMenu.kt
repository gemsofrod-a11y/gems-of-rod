package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.hslToRgb
import java.text.Collator
import java.util.Locale

/** Nom de la pierre dans la langue de l'app. */
@Composable
fun facetMaterialName(m: FacetMaterial): String = stringResource(
    when (m) {
        FacetMaterial.DIAMANT -> R.string.facet_mat_diamant
        FacetMaterial.MOISSANITE -> R.string.facet_mat_moissanite
        FacetMaterial.ZIRCONE_CUBIQUE -> R.string.facet_mat_zircone_cubique
        FacetMaterial.ZIRCON -> R.string.facet_mat_zircon
        FacetMaterial.GRENAT -> R.string.facet_mat_grenat
        FacetMaterial.CORINDON -> R.string.facet_mat_corindon
        FacetMaterial.CHRYSOBERYL -> R.string.facet_mat_chrysoberyl
        FacetMaterial.SPINELLE -> R.string.facet_mat_spinelle
        FacetMaterial.TANZANITE -> R.string.facet_mat_tanzanite
        FacetMaterial.PERIDOT -> R.string.facet_mat_peridot
        FacetMaterial.TOURMALINE -> R.string.facet_mat_tourmaline
        FacetMaterial.TOPAZE -> R.string.facet_mat_topaze
        FacetMaterial.BERYL -> R.string.facet_mat_beryl
        FacetMaterial.QUARTZ -> R.string.facet_mat_quartz
        FacetMaterial.RUBIS -> R.string.facet_mat_rubis
        FacetMaterial.PADPARADSCHA -> R.string.facet_mat_padparadscha
        FacetMaterial.EMERAUDE -> R.string.facet_mat_emeraude
        FacetMaterial.MORGANITE -> R.string.facet_mat_morganite
        FacetMaterial.HELIODORE -> R.string.facet_mat_heliodore
        FacetMaterial.AMETHYSTE -> R.string.facet_mat_amethyste
        FacetMaterial.CRISTAL_DE_ROCHE -> R.string.facet_mat_cristal_de_roche
        FacetMaterial.QUARTZ_FUME -> R.string.facet_mat_quartz_fume
        FacetMaterial.ALEXANDRITE -> R.string.facet_mat_alexandrite
        FacetMaterial.DEMANTOIDE -> R.string.facet_mat_demantoide
        FacetMaterial.TSAVORITE -> R.string.facet_mat_tsavorite
        FacetMaterial.SPESSARTITE -> R.string.facet_mat_spessartite
        FacetMaterial.PYROPE -> R.string.facet_mat_pyrope
        FacetMaterial.RHODOLITE -> R.string.facet_mat_rhodolite
        FacetMaterial.RUBELLITE -> R.string.facet_mat_rubellite
        FacetMaterial.TOPAZE_IMPERIALE -> R.string.facet_mat_topaze_imperiale
        FacetMaterial.KUNZITE -> R.string.facet_mat_kunzite
        FacetMaterial.APATITE -> R.string.facet_mat_apatite
        FacetMaterial.IOLITE -> R.string.facet_mat_iolite
        FacetMaterial.SPHENE -> R.string.facet_mat_sphene
        FacetMaterial.FLUORITE -> R.string.facet_mat_fluorite
        FacetMaterial.SCAPOLITE -> R.string.facet_mat_scapolite
        FacetMaterial.ANDALOUSITE -> R.string.facet_mat_andalousite
        FacetMaterial.KYANITE -> R.string.facet_mat_kyanite
        FacetMaterial.DIOPSIDE -> R.string.facet_mat_diopside
        FacetMaterial.SINHALITE -> R.string.facet_mat_sinhalite
        FacetMaterial.BENITOITE -> R.string.facet_mat_benitoite
        FacetMaterial.EUCLASE -> R.string.facet_mat_euclase
        FacetMaterial.ORTHOCLASE -> R.string.facet_mat_orthoclase
        FacetMaterial.OPALE_DE_FEU -> R.string.facet_mat_opale_de_feu
        FacetMaterial.CERUSITE -> R.string.facet_mat_cerusite
        FacetMaterial.CASSITERITE -> R.string.facet_mat_cassiterite
        FacetMaterial.YAG -> R.string.facet_mat_yag
        FacetMaterial.GGG -> R.string.facet_mat_ggg
        FacetMaterial.VERRE -> R.string.facet_mat_verre
    }
)

/** Couleur typique de la pierre (pastille, 3D). Pierre incolore : blanc. */
fun facetMaterialSwatch(m: FacetMaterial): Color =
    hslToRgb(m.hue, m.saturation, m.lightness).let { Color(it[0].toFloat(), it[1].toFloat(), it[2].toFloat()) }

private fun num(v: Double, d: Int) = String.format(Locale.getDefault(), "%.${d}f", v)

/**
 * Liste déroulante de toutes les pierres facettables, triées par nom dans la langue
 * de l'app, avec indice de réfraction, densité et dispersion : choisir une pierre
 * règle automatiquement ses constantes. À placer dans le même Box que son bouton.
 */
@Composable
fun FacetMaterialMenu(expanded: Boolean, current: FacetMaterial, onDismiss: () -> Unit, onPick: (FacetMaterial) -> Unit) {
    val labels = FacetMaterial.entries.associateWith { facetMaterialName(it) }
    val collator = Collator.getInstance(Locale.getDefault())
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        labels.entries.sortedWith { a, b -> collator.compare(a.value, b.value) }.forEach { (m, label) ->
            DropdownMenuItem(
                leadingIcon = {
                    Box(Modifier.size(14.dp).clip(CircleShape).background(facetMaterialSwatch(m)))
                },
                text = {
                    Column {
                        Text(label, fontWeight = if (m == current) FontWeight.Bold else FontWeight.Normal)
                        Text(
                            stringResource(R.string.facet_material_constants, num(m.ri, 3), num(m.sg, 2), num(m.dispersion, 3)),
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                onClick = { onPick(m) }
            )
        }
    }
}
