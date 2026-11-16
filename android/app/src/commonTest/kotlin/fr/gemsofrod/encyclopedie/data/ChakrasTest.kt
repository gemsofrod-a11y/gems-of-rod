package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertTrue

/** Le classement par chakra doit toujours suivre le ou les chakras indiqués sur la fiche de chaque pierre. */
class ChakrasTest {
    @Test
    fun everyListedStoneMatchesItsOwnSheet() {
        for (chakra in Chakras.labels) {
            for (gem in Chakras.gemsFor(chakra)) {
                val chakras = LithoGuideData.entries[gem.id]?.chakras.orEmpty()
                assertTrue(chakra in chakras || "ALL" in chakras, "${gem.id} classée en $chakra alors que sa fiche dit $chakras")
            }
        }
    }

    @Test
    fun everyStoneIsClassifiedUnderItsChakras() {
        for (gem in GemsRepository.gems) {
            val chakras = LithoGuideData.entries[gem.id]?.chakras.orEmpty().filter { it != "ALL" }
            assertTrue(chakras.isNotEmpty(), "${gem.id} sans chakra")
            for (chakra in chakras) assertTrue(gem in Chakras.gemsFor(chakra), "${gem.id} absente de $chakra")
        }
    }
}
