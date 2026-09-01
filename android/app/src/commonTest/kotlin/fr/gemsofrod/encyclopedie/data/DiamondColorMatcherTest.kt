package fr.gemsofrod.encyclopedie.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DiamondColorMatcherTest {

    private val grades = listOf(
        DiamondColorGrade("D", "Incolore", 0f),
        DiamondColorGrade("H", "Presque incolore", 0.18f),
        DiamondColorGrade("M", "Légèrement teinté", 0.41f),
        DiamondColorGrade("Z", "Jauni", 1f)
    )

    @Test
    fun exactMatch_returnsSameGrade() {
        assertEquals("H", DiamondColorMatcher.nearest(0.18f, grades)?.letter)
    }

    @Test
    fun closeValue_returnsNearestGrade() {
        assertEquals("D", DiamondColorMatcher.nearest(0.05f, grades)?.letter)
        assertEquals("Z", DiamondColorMatcher.nearest(0.95f, grades)?.letter)
    }

    @Test
    fun midpoint_prefersLowerIndexOnTie() {
        // 0.09 est à égale distance de D (0) et H (0.18) ; minByOrNull garde
        // le premier trouvé en cas d'égalité, donc D (l'ordre de la liste).
        assertEquals("D", DiamondColorMatcher.nearest(0.09f, grades)?.letter)
    }

    @Test
    fun emptyGrades_returnsNull() {
        assertNull(DiamondColorMatcher.nearest(0.5f, emptyList()))
    }
}
