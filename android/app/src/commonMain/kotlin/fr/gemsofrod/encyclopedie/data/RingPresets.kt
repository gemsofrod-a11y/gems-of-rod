package fr.gemsofrod.encyclopedie.data

/**
 * Modèles rapides du compositeur de bague : trois compositions de départ
 * représentatives, à affiner ensuite dans le formulaire. Le grammage de
 * métal par défaut varie selon le modèle (un pavage complet consomme
 * davantage de métal en chaton clos et en rail qu'un solitaire simple).
 */
object RingPresets {

    val SOLITAIRE = RingConfiguration(
        metal = RingMetal.PLATINE,
        gramsMetal = 3.5,
        central = RingStoneSelection(
            species = RingGemSpecies.DIAMANT,
            color = RingGemSpecies.DIAMANT.defaultColor,
            sertissage = RingSertissage.GRIFFES,
            cut = RingCutShape.OVALE,
            widthMm = 8.0,
            heightMm = 6.0,
            orientationDeg = 0f
        ),
        cote = RingSideStoneSelection(
            stone = RingStoneSelection(
                species = RingGemSpecies.SAPHIR,
                color = RingGemSpecies.SAPHIR.defaultColor,
                sertissage = RingSertissage.PAVE,
                cut = RingCutShape.RONDE,
                widthMm = 1.8,
                heightMm = 1.8,
                orientationDeg = 0f
            ),
            nombre = 0,
            espacement = 0.15f,
            orientationOffsetDeg = 0f
        )
    )

    val TRILOGIE = RingConfiguration(
        metal = RingMetal.OR_BLANC,
        gramsMetal = 4.5,
        central = RingStoneSelection(
            species = RingGemSpecies.DIAMANT,
            color = RingGemSpecies.DIAMANT.defaultColor,
            sertissage = RingSertissage.GRIFFES,
            cut = RingCutShape.RONDE,
            widthMm = 6.5,
            heightMm = 6.5,
            orientationDeg = 0f
        ),
        cote = RingSideStoneSelection(
            stone = RingStoneSelection(
                species = RingGemSpecies.DIAMANT,
                color = RingGemSpecies.DIAMANT.defaultColor,
                sertissage = RingSertissage.GRIFFES,
                cut = RingCutShape.RONDE,
                widthMm = 4.5,
                heightMm = 4.5,
                orientationDeg = 0f
            ),
            nombre = 1,
            espacement = 0.5f,
            orientationOffsetDeg = 0f
        )
    )

    val PAVE_COMPLET = RingConfiguration(
        metal = RingMetal.OR_JAUNE,
        gramsMetal = 6.0,
        central = RingStoneSelection(
            species = RingGemSpecies.EMERAUDE,
            color = RingGemSpecies.EMERAUDE.defaultColor,
            sertissage = RingSertissage.CLOS,
            cut = RingCutShape.COUSSIN,
            widthMm = 7.5,
            heightMm = 7.5,
            orientationDeg = 0f
        ),
        cote = RingSideStoneSelection(
            stone = RingStoneSelection(
                species = RingGemSpecies.DIAMANT,
                color = RingGemSpecies.DIAMANT.defaultColor,
                sertissage = RingSertissage.PAVE,
                cut = RingCutShape.RONDE,
                widthMm = 1.6,
                heightMm = 1.6,
                orientationDeg = 0f
            ),
            nombre = 6,
            espacement = 0.15f,
            orientationOffsetDeg = 0f
        )
    )
}
