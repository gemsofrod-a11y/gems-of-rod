package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NuancierColorMatcherTest {

    private val palette = listOf(
        NuancierCouleur(nom = "Pigeon Blood", espece = "Rubis", hueDeg = 350f, saturation = 0.85f, valeur = 0.55f),
        NuancierCouleur(nom = "Cornflower Blue", espece = "Saphir", hueDeg = 220f, saturation = 0.55f, valeur = 0.75f),
        NuancierCouleur(nom = "Paraíba", espece = "Tourmaline", hueDeg = 175f, saturation = 0.85f, valeur = 0.85f)
    )

    @Test
    fun exactMatch_returnsSameColor() {
        val result = NuancierColorMatcher.match(hueDeg = 175f, saturation = 0.85f, valeur = 0.85f, colors = palette)
        assertEquals("Paraíba", result?.nom)
    }

    @Test
    fun closeMatch_returnsNearestColor() {
        // Légèrement décalé par rapport à Cornflower Blue, mais bien plus proche que les deux autres.
        val result = NuancierColorMatcher.match(hueDeg = 225f, saturation = 0.5f, valeur = 0.7f, colors = palette)
        assertEquals("Cornflower Blue", result?.nom)
    }

    @Test
    fun lowSaturation_isTreatedAsAchromatic() {
        val result = NuancierColorMatcher.match(hueDeg = 220f, saturation = 0.05f, valeur = 0.8f, colors = palette)
        assertNull(result)
    }

    @Test
    fun farColor_returnsNoMatch() {
        // Jaune vif clair : loin de toutes les entrées de la palette de test.
        val result = NuancierColorMatcher.match(hueDeg = 55f, saturation = 0.4f, valeur = 0.95f, colors = palette)
        assertNull(result)
    }

    @Test
    fun emptyPalette_returnsNoMatch() {
        val result = NuancierColorMatcher.match(hueDeg = 175f, saturation = 0.85f, valeur = 0.85f, colors = emptyList())
        assertNull(result)
    }

    @Test
    fun hueDistance_wrapsAroundColorWheel() {
        // 350° et 10° sont proches de 20° sur le cercle chromatique, pas de 340°.
        assertEquals(20f, NuancierColorMatcher.hueDistance(350f, 10f), 0.001f)
        assertEquals(20f, NuancierColorMatcher.hueDistance(10f, 350f), 0.001f)
        assertEquals(0f, NuancierColorMatcher.hueDistance(180f, 180f), 0.001f)
        assertEquals(180f, NuancierColorMatcher.hueDistance(0f, 180f), 0.001f)
    }
}
