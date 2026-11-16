package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertTrue

/** Le classement par signe doit toujours suivre le signe indiqué sur la fiche de chaque pierre. */
class ZodiacSignsTest {
    @Test
    fun everyListedStoneMatchesItsOwnSheet() {
        for (sign in ZodiacSigns.labels) {
            for (gem in ZodiacSigns.gemsFor(sign)) {
                val signs = LithoGuideData.entries[gem.id]?.signs.orEmpty()
                assertTrue(sign in signs || "ALL" in signs, "${gem.id} classée en $sign alors que sa fiche dit $signs")
            }
        }
    }

    @Test
    fun everyStoneWithASignIsClassified() {
        for (gem in GemsRepository.gems) {
            val signs = LithoGuideData.entries[gem.id]?.signs.orEmpty().filter { it != "ALL" }
            for (sign in signs) {
                assertTrue(gem in ZodiacSigns.gemsFor(sign), "${gem.id} absente de $sign")
            }
        }
    }

    @Test
    fun everySignHasStones() {
        for (sign in ZodiacSigns.labels) assertTrue(ZodiacSigns.gemsFor(sign).isNotEmpty(), "$sign vide")
    }
}
