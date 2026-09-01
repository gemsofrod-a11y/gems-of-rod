package fr.gemsofrod.encyclopedie.data

/**
 * Un cran de l'échelle de pureté du diamant (FL à I3, GIA et équivalents).
 * [code] et [expansion] sont des termes commerciaux internationaux, utilisés
 * tels quels dans toutes les langues du commerce du diamant (comme les
 * appellations de couleur du nuancier) ; seule [description] est localisée.
 */
data class DiamondClarityGrade(
    val code: String,
    val expansion: String,
    val description: String
)

data class DiamondGradingPage(
    val title: String,
    val intro: String,
    val disclaimerTitle: String,
    val disclaimerBody: String,
    val colorSectionTitle: String,
    val colorSectionIntro: String,
    val whiteSquareLabel: String,
    val tintLabel: String,
    val colorResultFormat: String,
    val colorGrades: List<DiamondColorGrade>,
    val claritySectionTitle: String,
    val claritySectionIntro: String,
    val clarityGrades: List<DiamondClarityGrade>
)

/**
 * Couleur et pureté du diamant : deux échelles de référence du commerce du
 * diamant blanc (hors diamants de couleur fantaisie, qui suivent un système
 * distinct). La couleur (D à Z) est approchée par comparaison visuelle via
 * un curseur, comme le nuancier de couleur ([NuancierInfo]) — un seul axe
 * ici (intensité de teinte jaune), pas un espace HSV complet. La pureté
 * (FL à I3) s'évalue à la loupe ×10 par un œil entraîné : elle est
 * présentée en repère descriptif uniquement, sans curseur.
 */
object DiamondGradingInfo {
    private val fr = DiamondGradingPage(
        title = "Couleur et pureté du diamant",
        intro = "Repères de référence pour situer approximativement la couleur et la pureté d'un diamant sur les échelles utilisées dans le commerce.",
        disclaimerTitle = "Un repère de lecture, pas une certification",
        disclaimerBody = "La couleur et la pureté officielles d'un diamant sont déterminées en laboratoire (GIA, HRD, etc.) par comparaison à des pierres étalons sous éclairage normalisé et par examen à la loupe ×10. Le rendu de l'écran de votre téléphone et l'éclairage ambiant influencent fortement la perception — utilisez cette page comme repère pédagogique, jamais comme certification.",
        colorSectionTitle = "Échelle de couleur (D à Z)",
        colorSectionIntro = "Posez le diamant, table vers le bas, sur le carré blanc, sous une lumière naturelle indirecte. Déplacez le curseur jusqu'à retrouver la teinte la plus proche.",
        whiteSquareLabel = "Posez le diamant ici",
        tintLabel = "Intensité de la teinte jaune",
        colorResultFormat = "Grade %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Incolore", 0f),
            DiamondColorGrade("E", "Incolore", 0.0455f),
            DiamondColorGrade("F", "Incolore", 0.0909f),
            DiamondColorGrade("G", "Presque incolore", 0.1364f),
            DiamondColorGrade("H", "Presque incolore", 0.1818f),
            DiamondColorGrade("I", "Presque incolore", 0.2273f),
            DiamondColorGrade("J", "Presque incolore", 0.2727f),
            DiamondColorGrade("K", "Légèrement teinté", 0.3182f),
            DiamondColorGrade("L", "Légèrement teinté", 0.3636f),
            DiamondColorGrade("M", "Légèrement teinté", 0.4091f),
            DiamondColorGrade("N", "Très légèrement jauni", 0.4545f),
            DiamondColorGrade("O", "Très légèrement jauni", 0.5f),
            DiamondColorGrade("P", "Très légèrement jauni", 0.5455f),
            DiamondColorGrade("Q", "Très légèrement jauni", 0.5909f),
            DiamondColorGrade("R", "Très légèrement jauni", 0.6364f),
            DiamondColorGrade("S", "Jauni", 0.6818f),
            DiamondColorGrade("T", "Jauni", 0.7273f),
            DiamondColorGrade("U", "Jauni", 0.7727f),
            DiamondColorGrade("V", "Jauni", 0.8182f),
            DiamondColorGrade("W", "Jauni", 0.8636f),
            DiamondColorGrade("X", "Jauni", 0.9091f),
            DiamondColorGrade("Y", "Jauni", 0.9545f),
            DiamondColorGrade("Z", "Jauni", 1f)
        ),
        claritySectionTitle = "Échelle de pureté (FL à I3)",
        claritySectionIntro = "La pureté s'évalue à la loupe ×10 par un œil entraîné — un repère de lecture, pas un diagnostic.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Sans inclusion — Aucune inclusion ni imperfection visible sous grossissement ×10 par un gemmologue expérimenté."),
            DiamondClarityGrade("IF", "Internally Flawless", "Pur à l'intérieur — Aucune inclusion interne visible sous ×10 ; seulement de très légères imperfections de surface, pouvant être polies."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Inclusions minuscules, extrêmement difficiles à repérer sous ×10, même pour un œil expérimenté."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Inclusions minuscules, très difficiles à repérer sous ×10."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Inclusions mineures, difficiles à repérer sous ×10."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Inclusions mineures, assez faciles à repérer sous ×10."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Inclusions clairement visibles sous ×10."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Inclusions clairement visibles sous ×10, parfois discernables à l'œil nu selon la pierre."),
            DiamondClarityGrade("I1", "Included 1", "Inclusions visibles à l'œil nu, effet limité sur la transparence."),
            DiamondClarityGrade("I2", "Included 2", "Inclusions visibles à l'œil nu, affectant sensiblement la transparence et l'éclat."),
            DiamondClarityGrade("I3", "Included 3", "Inclusions très visibles à l'œil nu, affectant fortement la transparence et l'éclat.")
        )
    )

    private val en = DiamondGradingPage(
        title = "Diamond color and clarity",
        intro = "Reference points for approximately situating a diamond's color and clarity on the scales used in the trade.",
        disclaimerTitle = "A reading guide, not a certification",
        disclaimerBody = "A diamond's official color and clarity are determined in a lab (GIA, HRD, etc.) by comparison to master stones under standardized lighting and examination under 10x magnification. Your phone screen's rendering and ambient lighting strongly influence perception — use this page as an educational reference, never as a certification.",
        colorSectionTitle = "Color scale (D to Z)",
        colorSectionIntro = "Place the diamond table-down on the white square, under indirect natural light. Move the slider until you reach the closest tint.",
        whiteSquareLabel = "Place the diamond here",
        tintLabel = "Yellow tint intensity",
        colorResultFormat = "Grade %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Colorless", 0f),
            DiamondColorGrade("E", "Colorless", 0.0455f),
            DiamondColorGrade("F", "Colorless", 0.0909f),
            DiamondColorGrade("G", "Near colorless", 0.1364f),
            DiamondColorGrade("H", "Near colorless", 0.1818f),
            DiamondColorGrade("I", "Near colorless", 0.2273f),
            DiamondColorGrade("J", "Near colorless", 0.2727f),
            DiamondColorGrade("K", "Faintly tinted", 0.3182f),
            DiamondColorGrade("L", "Faintly tinted", 0.3636f),
            DiamondColorGrade("M", "Faintly tinted", 0.4091f),
            DiamondColorGrade("N", "Very lightly tinted", 0.4545f),
            DiamondColorGrade("O", "Very lightly tinted", 0.5f),
            DiamondColorGrade("P", "Very lightly tinted", 0.5455f),
            DiamondColorGrade("Q", "Very lightly tinted", 0.5909f),
            DiamondColorGrade("R", "Very lightly tinted", 0.6364f),
            DiamondColorGrade("S", "Tinted", 0.6818f),
            DiamondColorGrade("T", "Tinted", 0.7273f),
            DiamondColorGrade("U", "Tinted", 0.7727f),
            DiamondColorGrade("V", "Tinted", 0.8182f),
            DiamondColorGrade("W", "Tinted", 0.8636f),
            DiamondColorGrade("X", "Tinted", 0.9091f),
            DiamondColorGrade("Y", "Tinted", 0.9545f),
            DiamondColorGrade("Z", "Tinted", 1f)
        ),
        claritySectionTitle = "Clarity scale (FL to I3)",
        claritySectionIntro = "Clarity is assessed under 10x magnification by a trained eye — a reading guide, not a diagnosis.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "No inclusions or blemishes visible under 10x magnification to an experienced gemologist."),
            DiamondClarityGrade("IF", "Internally Flawless", "No internal inclusions visible under 10x; only very minor surface blemishes, which can be polished away."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Tiny inclusions, extremely difficult to spot under 10x, even for an experienced eye."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Tiny inclusions, very difficult to spot under 10x."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Minor inclusions, difficult to spot under 10x."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Minor inclusions, fairly easy to spot under 10x."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Inclusions clearly visible under 10x."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Inclusions clearly visible under 10x, sometimes noticeable to the naked eye depending on the stone."),
            DiamondClarityGrade("I1", "Included 1", "Inclusions visible to the naked eye, limited effect on transparency."),
            DiamondClarityGrade("I2", "Included 2", "Inclusions visible to the naked eye, noticeably affecting transparency and brilliance."),
            DiamondClarityGrade("I3", "Included 3", "Inclusions very visible to the naked eye, strongly affecting transparency and brilliance.")
        )
    )

    private val es = DiamondGradingPage(
        title = "Color y pureza del diamante",
        intro = "Referencias para situar aproximadamente el color y la pureza de un diamante en las escalas usadas en el comercio.",
        disclaimerTitle = "Una guía de lectura, no una certificación",
        disclaimerBody = "El color y la pureza oficiales de un diamante se determinan en laboratorio (GIA, HRD, etc.) por comparación con piedras patrón bajo iluminación normalizada y examen con lupa ×10. El renderizado de la pantalla de tu teléfono y la luz ambiente influyen mucho en la percepción — usa esta página como referencia educativa, nunca como certificación.",
        colorSectionTitle = "Escala de color (D a Z)",
        colorSectionIntro = "Coloca el diamante con la mesa hacia abajo sobre el cuadrado blanco, bajo luz natural indirecta. Mueve el deslizador hasta encontrar el tono más parecido.",
        whiteSquareLabel = "Coloca el diamante aquí",
        tintLabel = "Intensidad del tono amarillo",
        colorResultFormat = "Grado %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Incoloro", 0f),
            DiamondColorGrade("E", "Incoloro", 0.0455f),
            DiamondColorGrade("F", "Incoloro", 0.0909f),
            DiamondColorGrade("G", "Casi incoloro", 0.1364f),
            DiamondColorGrade("H", "Casi incoloro", 0.1818f),
            DiamondColorGrade("I", "Casi incoloro", 0.2273f),
            DiamondColorGrade("J", "Casi incoloro", 0.2727f),
            DiamondColorGrade("K", "Ligeramente teñido", 0.3182f),
            DiamondColorGrade("L", "Ligeramente teñido", 0.3636f),
            DiamondColorGrade("M", "Ligeramente teñido", 0.4091f),
            DiamondColorGrade("N", "Muy ligeramente amarillento", 0.4545f),
            DiamondColorGrade("O", "Muy ligeramente amarillento", 0.5f),
            DiamondColorGrade("P", "Muy ligeramente amarillento", 0.5455f),
            DiamondColorGrade("Q", "Muy ligeramente amarillento", 0.5909f),
            DiamondColorGrade("R", "Muy ligeramente amarillento", 0.6364f),
            DiamondColorGrade("S", "Amarillento", 0.6818f),
            DiamondColorGrade("T", "Amarillento", 0.7273f),
            DiamondColorGrade("U", "Amarillento", 0.7727f),
            DiamondColorGrade("V", "Amarillento", 0.8182f),
            DiamondColorGrade("W", "Amarillento", 0.8636f),
            DiamondColorGrade("X", "Amarillento", 0.9091f),
            DiamondColorGrade("Y", "Amarillento", 0.9545f),
            DiamondColorGrade("Z", "Amarillento", 1f)
        ),
        claritySectionTitle = "Escala de pureza (FL a I3)",
        claritySectionIntro = "La pureza se evalúa con lupa ×10 y un ojo entrenado — una guía de lectura, no un diagnóstico.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Sin inclusiones — Ninguna inclusión ni imperfección visible con aumento de ×10 para un gemólogo experimentado."),
            DiamondClarityGrade("IF", "Internally Flawless", "Interior puro — Ninguna inclusión interna visible con ×10; solo imperfecciones superficiales muy leves, que pueden pulirse."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Inclusiones minúsculas, extremadamente difíciles de detectar con ×10, incluso para un ojo experimentado."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Inclusiones minúsculas, muy difíciles de detectar con ×10."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Inclusiones menores, difíciles de detectar con ×10."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Inclusiones menores, bastante fáciles de detectar con ×10."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Inclusiones claramente visibles con ×10."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Inclusiones claramente visibles con ×10, a veces perceptibles a simple vista según la piedra."),
            DiamondClarityGrade("I1", "Included 1", "Inclusiones visibles a simple vista, efecto limitado en la transparencia."),
            DiamondClarityGrade("I2", "Included 2", "Inclusiones visibles a simple vista, que afectan notablemente la transparencia y el brillo."),
            DiamondClarityGrade("I3", "Included 3", "Inclusiones muy visibles a simple vista, que afectan fuertemente la transparencia y el brillo.")
        )
    )

    private val it = DiamondGradingPage(
        title = "Colore e purezza del diamante",
        intro = "Riferimenti per situare approssimativamente il colore e la purezza di un diamante sulle scale usate nel commercio.",
        disclaimerTitle = "Una guida di lettura, non una certificazione",
        disclaimerBody = "Il colore e la purezza ufficiali di un diamante sono determinati in laboratorio (GIA, HRD, ecc.) per confronto con pietre campione sotto illuminazione standardizzata ed esame con lente ×10. La resa dello schermo del telefono e la luce ambientale influenzano molto la percezione — usa questa pagina come riferimento didattico, mai come certificazione.",
        colorSectionTitle = "Scala del colore (D a Z)",
        colorSectionIntro = "Posa il diamante con il tavolo verso il basso sul quadrato bianco, sotto luce naturale indiretta. Sposta il cursore fino a trovare la tonalità più simile.",
        whiteSquareLabel = "Posa il diamante qui",
        tintLabel = "Intensità della tonalità gialla",
        colorResultFormat = "Grado %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Incolore", 0f),
            DiamondColorGrade("E", "Incolore", 0.0455f),
            DiamondColorGrade("F", "Incolore", 0.0909f),
            DiamondColorGrade("G", "Quasi incolore", 0.1364f),
            DiamondColorGrade("H", "Quasi incolore", 0.1818f),
            DiamondColorGrade("I", "Quasi incolore", 0.2273f),
            DiamondColorGrade("J", "Quasi incolore", 0.2727f),
            DiamondColorGrade("K", "Leggermente colorato", 0.3182f),
            DiamondColorGrade("L", "Leggermente colorato", 0.3636f),
            DiamondColorGrade("M", "Leggermente colorato", 0.4091f),
            DiamondColorGrade("N", "Molto leggermente ingiallito", 0.4545f),
            DiamondColorGrade("O", "Molto leggermente ingiallito", 0.5f),
            DiamondColorGrade("P", "Molto leggermente ingiallito", 0.5455f),
            DiamondColorGrade("Q", "Molto leggermente ingiallito", 0.5909f),
            DiamondColorGrade("R", "Molto leggermente ingiallito", 0.6364f),
            DiamondColorGrade("S", "Ingiallito", 0.6818f),
            DiamondColorGrade("T", "Ingiallito", 0.7273f),
            DiamondColorGrade("U", "Ingiallito", 0.7727f),
            DiamondColorGrade("V", "Ingiallito", 0.8182f),
            DiamondColorGrade("W", "Ingiallito", 0.8636f),
            DiamondColorGrade("X", "Ingiallito", 0.9091f),
            DiamondColorGrade("Y", "Ingiallito", 0.9545f),
            DiamondColorGrade("Z", "Ingiallito", 1f)
        ),
        claritySectionTitle = "Scala di purezza (FL a I3)",
        claritySectionIntro = "La purezza si valuta con lente ×10 e occhio esperto — una guida di lettura, non una diagnosi.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Senza inclusioni — Nessuna inclusione o imperfezione visibile con ingrandimento ×10 per un gemmologo esperto."),
            DiamondClarityGrade("IF", "Internally Flawless", "Puro all'interno — Nessuna inclusione interna visibile con ×10; solo lievissime imperfezioni superficiali, eliminabili con la lucidatura."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Inclusioni minuscole, estremamente difficili da individuare con ×10, anche per un occhio esperto."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Inclusioni minuscole, molto difficili da individuare con ×10."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Inclusioni minori, difficili da individuare con ×10."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Inclusioni minori, abbastanza facili da individuare con ×10."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Inclusioni chiaramente visibili con ×10."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Inclusioni chiaramente visibili con ×10, talvolta percepibili a occhio nudo a seconda della pietra."),
            DiamondClarityGrade("I1", "Included 1", "Inclusioni visibili a occhio nudo, effetto limitato sulla trasparenza."),
            DiamondClarityGrade("I2", "Included 2", "Inclusioni visibili a occhio nudo, che compromettono sensibilmente trasparenza e brillantezza."),
            DiamondClarityGrade("I3", "Included 3", "Inclusioni molto visibili a occhio nudo, che compromettono fortemente trasparenza e brillantezza.")
        )
    )

    private val de = DiamondGradingPage(
        title = "Farbe und Reinheit des Diamanten",
        intro = "Anhaltspunkte, um Farbe und Reinheit eines Diamanten ungefähr auf den im Handel verwendeten Skalen einzuordnen.",
        disclaimerTitle = "Ein Lesehinweis, keine Zertifizierung",
        disclaimerBody = "Die offizielle Farbe und Reinheit eines Diamanten werden im Labor (GIA, HRD usw.) durch Vergleich mit Referenzsteinen unter genormter Beleuchtung und Untersuchung unter 10-facher Vergrößerung bestimmt. Die Wiedergabe des Bildschirms Ihres Telefons und das Umgebungslicht beeinflussen die Wahrnehmung stark — verwenden Sie diese Seite als pädagogischen Anhaltspunkt, niemals als Zertifizierung.",
        colorSectionTitle = "Farbskala (D bis Z)",
        colorSectionIntro = "Legen Sie den Diamanten mit der Tafel nach unten auf das weiße Quadrat, bei indirektem Tageslicht. Bewegen Sie den Regler, bis Sie den ähnlichsten Farbton finden.",
        whiteSquareLabel = "Diamant hier ablegen",
        tintLabel = "Intensität des Gelbtons",
        colorResultFormat = "Grad %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Farblos", 0f),
            DiamondColorGrade("E", "Farblos", 0.0455f),
            DiamondColorGrade("F", "Farblos", 0.0909f),
            DiamondColorGrade("G", "Fast farblos", 0.1364f),
            DiamondColorGrade("H", "Fast farblos", 0.1818f),
            DiamondColorGrade("I", "Fast farblos", 0.2273f),
            DiamondColorGrade("J", "Fast farblos", 0.2727f),
            DiamondColorGrade("K", "Leicht getönt", 0.3182f),
            DiamondColorGrade("L", "Leicht getönt", 0.3636f),
            DiamondColorGrade("M", "Leicht getönt", 0.4091f),
            DiamondColorGrade("N", "Sehr leicht gelblich", 0.4545f),
            DiamondColorGrade("O", "Sehr leicht gelblich", 0.5f),
            DiamondColorGrade("P", "Sehr leicht gelblich", 0.5455f),
            DiamondColorGrade("Q", "Sehr leicht gelblich", 0.5909f),
            DiamondColorGrade("R", "Sehr leicht gelblich", 0.6364f),
            DiamondColorGrade("S", "Gelblich", 0.6818f),
            DiamondColorGrade("T", "Gelblich", 0.7273f),
            DiamondColorGrade("U", "Gelblich", 0.7727f),
            DiamondColorGrade("V", "Gelblich", 0.8182f),
            DiamondColorGrade("W", "Gelblich", 0.8636f),
            DiamondColorGrade("X", "Gelblich", 0.9091f),
            DiamondColorGrade("Y", "Gelblich", 0.9545f),
            DiamondColorGrade("Z", "Gelblich", 1f)
        ),
        claritySectionTitle = "Reinheitsskala (FL bis I3)",
        claritySectionIntro = "Die Reinheit wird unter 10-facher Vergrößerung von einem geübten Auge beurteilt — ein Lesehinweis, keine Diagnose.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Lupenrein — Keine Einschlüsse oder Oberflächenmerkmale unter 10-facher Vergrößerung für einen erfahrenen Gemmologen sichtbar."),
            DiamondClarityGrade("IF", "Internally Flawless", "Innerlich rein — Keine inneren Einschlüsse unter ×10 sichtbar; nur sehr leichte Oberflächenmerkmale, die wegpoliert werden können."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Winzige Einschlüsse, unter ×10 extrem schwer zu erkennen, selbst für ein geübtes Auge."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Winzige Einschlüsse, unter ×10 sehr schwer zu erkennen."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Kleinere Einschlüsse, unter ×10 schwer zu erkennen."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Kleinere Einschlüsse, unter ×10 recht leicht zu erkennen."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Einschlüsse unter ×10 deutlich sichtbar."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Einschlüsse unter ×10 deutlich sichtbar, je nach Stein manchmal auch mit bloßem Auge erkennbar."),
            DiamondClarityGrade("I1", "Included 1", "Einschlüsse mit bloßem Auge sichtbar, begrenzte Auswirkung auf die Transparenz."),
            DiamondClarityGrade("I2", "Included 2", "Einschlüsse mit bloßem Auge sichtbar, mit spürbarer Auswirkung auf Transparenz und Brillanz."),
            DiamondClarityGrade("I3", "Included 3", "Einschlüsse mit bloßem Auge deutlich sichtbar, mit starker Auswirkung auf Transparenz und Brillanz.")
        )
    )

    private val pt = DiamondGradingPage(
        title = "Cor e pureza do diamante",
        intro = "Referências para situar aproximadamente a cor e a pureza de um diamante nas escalas usadas no comércio.",
        disclaimerTitle = "Um guia de leitura, não uma certificação",
        disclaimerBody = "A cor e a pureza oficiais de um diamante são determinadas em laboratório (GIA, HRD, etc.) por comparação com pedras padrão sob iluminação normalizada e exame com lupa ×10. A renderização do ecrã do seu telemóvel e a luz ambiente influenciam muito a perceção — use esta página como referência educativa, nunca como certificação.",
        colorSectionTitle = "Escala de cor (D a Z)",
        colorSectionIntro = "Coloque o diamante com a mesa virada para baixo sobre o quadrado branco, sob luz natural indireta. Mova o cursor até encontrar o tom mais parecido.",
        whiteSquareLabel = "Coloque o diamante aqui",
        tintLabel = "Intensidade do tom amarelo",
        colorResultFormat = "Grau %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Incolor", 0f),
            DiamondColorGrade("E", "Incolor", 0.0455f),
            DiamondColorGrade("F", "Incolor", 0.0909f),
            DiamondColorGrade("G", "Quase incolor", 0.1364f),
            DiamondColorGrade("H", "Quase incolor", 0.1818f),
            DiamondColorGrade("I", "Quase incolor", 0.2273f),
            DiamondColorGrade("J", "Quase incolor", 0.2727f),
            DiamondColorGrade("K", "Levemente tingido", 0.3182f),
            DiamondColorGrade("L", "Levemente tingido", 0.3636f),
            DiamondColorGrade("M", "Levemente tingido", 0.4091f),
            DiamondColorGrade("N", "Muito levemente amarelado", 0.4545f),
            DiamondColorGrade("O", "Muito levemente amarelado", 0.5f),
            DiamondColorGrade("P", "Muito levemente amarelado", 0.5455f),
            DiamondColorGrade("Q", "Muito levemente amarelado", 0.5909f),
            DiamondColorGrade("R", "Muito levemente amarelado", 0.6364f),
            DiamondColorGrade("S", "Amarelado", 0.6818f),
            DiamondColorGrade("T", "Amarelado", 0.7273f),
            DiamondColorGrade("U", "Amarelado", 0.7727f),
            DiamondColorGrade("V", "Amarelado", 0.8182f),
            DiamondColorGrade("W", "Amarelado", 0.8636f),
            DiamondColorGrade("X", "Amarelado", 0.9091f),
            DiamondColorGrade("Y", "Amarelado", 0.9545f),
            DiamondColorGrade("Z", "Amarelado", 1f)
        ),
        claritySectionTitle = "Escala de pureza (FL a I3)",
        claritySectionIntro = "A pureza é avaliada com lupa ×10 por um olho treinado — um guia de leitura, não um diagnóstico.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Sem inclusões — Nenhuma inclusão ou imperfeição visível com ampliação de ×10 para um gemólogo experiente."),
            DiamondClarityGrade("IF", "Internally Flawless", "Puro por dentro — Nenhuma inclusão interna visível com ×10; apenas imperfeições superficiais muito leves, que podem ser polidas."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Inclusões minúsculas, extremamente difíceis de detetar com ×10, mesmo para um olho experiente."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Inclusões minúsculas, muito difíceis de detetar com ×10."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Inclusões menores, difíceis de detetar com ×10."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Inclusões menores, bastante fáceis de detetar com ×10."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Inclusões claramente visíveis com ×10."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Inclusões claramente visíveis com ×10, por vezes percetíveis a olho nu consoante a pedra."),
            DiamondClarityGrade("I1", "Included 1", "Inclusões visíveis a olho nu, efeito limitado na transparência."),
            DiamondClarityGrade("I2", "Included 2", "Inclusões visíveis a olho nu, afetando sensivelmente a transparência e o brilho."),
            DiamondClarityGrade("I3", "Included 3", "Inclusões muito visíveis a olho nu, afetando fortemente a transparência e o brilho.")
        )
    )

    private val ru = DiamondGradingPage(
        title = "Цвет и чистота бриллианта",
        intro = "Ориентиры для приблизительного определения цвета и чистоты бриллианта по шкалам, принятым в торговле.",
        disclaimerTitle = "Ориентир для чтения, а не сертификация",
        disclaimerBody = "Официальные цвет и чистота бриллианта определяются в лаборатории (GIA, HRD и др.) путём сравнения с эталонными камнями при стандартизированном освещении и осмотра под 10-кратным увеличением. Отображение экрана вашего телефона и окружающее освещение сильно влияют на восприятие — используйте эту страницу как справочный ориентир, а не как сертификацию.",
        colorSectionTitle = "Цветовая шкала (от D до Z)",
        colorSectionIntro = "Положите бриллиант площадкой вниз на белый квадрат при непрямом естественном освещении. Двигайте ползунок, пока не найдёте наиболее близкий оттенок.",
        whiteSquareLabel = "Положите бриллиант сюда",
        tintLabel = "Интенсивность жёлтого оттенка",
        colorResultFormat = "Цвет %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Бесцветный", 0f),
            DiamondColorGrade("E", "Бесцветный", 0.0455f),
            DiamondColorGrade("F", "Бесцветный", 0.0909f),
            DiamondColorGrade("G", "Почти бесцветный", 0.1364f),
            DiamondColorGrade("H", "Почти бесцветный", 0.1818f),
            DiamondColorGrade("I", "Почти бесцветный", 0.2273f),
            DiamondColorGrade("J", "Почти бесцветный", 0.2727f),
            DiamondColorGrade("K", "Слегка тонированный", 0.3182f),
            DiamondColorGrade("L", "Слегка тонированный", 0.3636f),
            DiamondColorGrade("M", "Слегка тонированный", 0.4091f),
            DiamondColorGrade("N", "Очень слегка желтоватый", 0.4545f),
            DiamondColorGrade("O", "Очень слегка желтоватый", 0.5f),
            DiamondColorGrade("P", "Очень слегка желтоватый", 0.5455f),
            DiamondColorGrade("Q", "Очень слегка желтоватый", 0.5909f),
            DiamondColorGrade("R", "Очень слегка желтоватый", 0.6364f),
            DiamondColorGrade("S", "Желтоватый", 0.6818f),
            DiamondColorGrade("T", "Желтоватый", 0.7273f),
            DiamondColorGrade("U", "Желтоватый", 0.7727f),
            DiamondColorGrade("V", "Желтоватый", 0.8182f),
            DiamondColorGrade("W", "Желтоватый", 0.8636f),
            DiamondColorGrade("X", "Желтоватый", 0.9091f),
            DiamondColorGrade("Y", "Желтоватый", 0.9545f),
            DiamondColorGrade("Z", "Желтоватый", 1f)
        ),
        claritySectionTitle = "Шкала чистоты (от FL до I3)",
        claritySectionIntro = "Чистота оценивается под 10-кратным увеличением опытным глазом — ориентир для чтения, а не диагноз.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Без включений — Ни включений, ни дефектов, видимых при 10-кратном увеличении опытным геммологом."),
            DiamondClarityGrade("IF", "Internally Flawless", "Внутренне чистый — Нет видимых внутренних включений при ×10; только очень незначительные поверхностные дефекты, которые можно отполировать."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Крошечные включения, крайне трудно различимые при ×10 даже опытным глазом."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Крошечные включения, очень трудно различимые при ×10."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Незначительные включения, трудно различимые при ×10."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Незначительные включения, довольно легко различимые при ×10."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Включения хорошо видны при ×10."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Включения хорошо видны при ×10, иногда заметны невооружённым глазом в зависимости от камня."),
            DiamondClarityGrade("I1", "Included 1", "Включения видны невооружённым глазом, ограниченное влияние на прозрачность."),
            DiamondClarityGrade("I2", "Included 2", "Включения видны невооружённым глазом, заметно влияя на прозрачность и блеск."),
            DiamondClarityGrade("I3", "Included 3", "Включения хорошо видны невооружённым глазом, сильно влияя на прозрачность и блеск.")
        )
    )

    private val nl = DiamondGradingPage(
        title = "Kleur en zuiverheid van diamant",
        intro = "Referentiepunten om de kleur en zuiverheid van een diamant bij benadering te plaatsen op de schalen die in de handel worden gebruikt.",
        disclaimerTitle = "Een leeshulpmiddel, geen certificering",
        disclaimerBody = "De officiële kleur en zuiverheid van een diamant worden in een laboratorium (GIA, HRD, enz.) bepaald door vergelijking met referentiestenen onder gestandaardiseerde verlichting en onderzoek onder 10x vergroting. De weergave van uw telefoonscherm en het omgevingslicht beïnvloeden de waarneming sterk — gebruik deze pagina als educatieve referentie, nooit als certificering.",
        colorSectionTitle = "Kleurenschaal (D tot Z)",
        colorSectionIntro = "Leg de diamant met de tafel naar beneden op het witte vierkant, bij indirect daglicht. Verplaats de schuifregelaar tot u de meest gelijkende tint vindt.",
        whiteSquareLabel = "Leg de diamant hier",
        tintLabel = "Intensiteit van de gele tint",
        colorResultFormat = "Kleur %1\$s — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "Kleurloos", 0f),
            DiamondColorGrade("E", "Kleurloos", 0.0455f),
            DiamondColorGrade("F", "Kleurloos", 0.0909f),
            DiamondColorGrade("G", "Bijna kleurloos", 0.1364f),
            DiamondColorGrade("H", "Bijna kleurloos", 0.1818f),
            DiamondColorGrade("I", "Bijna kleurloos", 0.2273f),
            DiamondColorGrade("J", "Bijna kleurloos", 0.2727f),
            DiamondColorGrade("K", "Licht getint", 0.3182f),
            DiamondColorGrade("L", "Licht getint", 0.3636f),
            DiamondColorGrade("M", "Licht getint", 0.4091f),
            DiamondColorGrade("N", "Zeer licht geel", 0.4545f),
            DiamondColorGrade("O", "Zeer licht geel", 0.5f),
            DiamondColorGrade("P", "Zeer licht geel", 0.5455f),
            DiamondColorGrade("Q", "Zeer licht geel", 0.5909f),
            DiamondColorGrade("R", "Zeer licht geel", 0.6364f),
            DiamondColorGrade("S", "Geel getint", 0.6818f),
            DiamondColorGrade("T", "Geel getint", 0.7273f),
            DiamondColorGrade("U", "Geel getint", 0.7727f),
            DiamondColorGrade("V", "Geel getint", 0.8182f),
            DiamondColorGrade("W", "Geel getint", 0.8636f),
            DiamondColorGrade("X", "Geel getint", 0.9091f),
            DiamondColorGrade("Y", "Geel getint", 0.9545f),
            DiamondColorGrade("Z", "Geel getint", 1f)
        ),
        claritySectionTitle = "Zuiverheidsschaal (FL tot I3)",
        claritySectionIntro = "Zuiverheid wordt beoordeeld onder 10x vergroting door een geoefend oog — een leeshulpmiddel, geen diagnose.",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "Zonder inclusies — Geen inclusies of oppervlaktekenmerken zichtbaar onder 10x vergroting voor een ervaren gemmoloog."),
            DiamondClarityGrade("IF", "Internally Flawless", "Intern zuiver — Geen interne inclusies zichtbaar onder ×10; alleen zeer lichte oppervlaktekenmerken, die wegpolijstbaar zijn."),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "Piepkleine inclusies, uiterst moeilijk te vinden onder ×10, zelfs voor een geoefend oog."),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "Piepkleine inclusies, zeer moeilijk te vinden onder ×10."),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "Kleine inclusies, moeilijk te vinden onder ×10."),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "Kleine inclusies, vrij gemakkelijk te vinden onder ×10."),
            DiamondClarityGrade("SI1", "Slightly Included 1", "Inclusies duidelijk zichtbaar onder ×10."),
            DiamondClarityGrade("SI2", "Slightly Included 2", "Inclusies duidelijk zichtbaar onder ×10, afhankelijk van de steen soms met het blote oog zichtbaar."),
            DiamondClarityGrade("I1", "Included 1", "Inclusies zichtbaar met het blote oog, beperkt effect op de transparantie."),
            DiamondClarityGrade("I2", "Included 2", "Inclusies zichtbaar met het blote oog, met merkbaar effect op transparantie en schittering."),
            DiamondClarityGrade("I3", "Included 3", "Inclusies duidelijk zichtbaar met het blote oog, met sterk effect op transparantie en schittering.")
        )
    )

    private val zh = DiamondGradingPage(
        title = "钻石颜色与净度",
        intro = "用于在业内通行的等级表上大致定位钻石颜色和净度的参考指南。",
        disclaimerTitle = "阅读参考，非鉴定证书",
        disclaimerBody = "钻石的官方颜色和净度由实验室（GIA、HRD 等）通过与标准样石在标准化光源下比对、并在10倍放大镜下检验后确定。手机屏幕的显示效果和环境光线会严重影响观感——请将此页面作为教学参考，切勿作为鉴定依据。",
        colorSectionTitle = "颜色等级表（D 至 Z）",
        colorSectionIntro = "将钻石台面朝下放在白色方块上，在间接自然光下观察。移动滑块，直到找到最接近的色调。",
        whiteSquareLabel = "将钻石放在这里",
        tintLabel = "黄色调强度",
        colorResultFormat = "%1\$s 色 — %2\$s",
        colorGrades = listOf(
            DiamondColorGrade("D", "无色", 0f),
            DiamondColorGrade("E", "无色", 0.0455f),
            DiamondColorGrade("F", "无色", 0.0909f),
            DiamondColorGrade("G", "近无色", 0.1364f),
            DiamondColorGrade("H", "近无色", 0.1818f),
            DiamondColorGrade("I", "近无色", 0.2273f),
            DiamondColorGrade("J", "近无色", 0.2727f),
            DiamondColorGrade("K", "微带色", 0.3182f),
            DiamondColorGrade("L", "微带色", 0.3636f),
            DiamondColorGrade("M", "微带色", 0.4091f),
            DiamondColorGrade("N", "极浅黄色", 0.4545f),
            DiamondColorGrade("O", "极浅黄色", 0.5f),
            DiamondColorGrade("P", "极浅黄色", 0.5455f),
            DiamondColorGrade("Q", "极浅黄色", 0.5909f),
            DiamondColorGrade("R", "极浅黄色", 0.6364f),
            DiamondColorGrade("S", "浅黄色", 0.6818f),
            DiamondColorGrade("T", "浅黄色", 0.7273f),
            DiamondColorGrade("U", "浅黄色", 0.7727f),
            DiamondColorGrade("V", "浅黄色", 0.8182f),
            DiamondColorGrade("W", "浅黄色", 0.8636f),
            DiamondColorGrade("X", "浅黄色", 0.9091f),
            DiamondColorGrade("Y", "浅黄色", 0.9545f),
            DiamondColorGrade("Z", "浅黄色", 1f)
        ),
        claritySectionTitle = "净度等级表（FL 至 I3）",
        claritySectionIntro = "净度由训练有素的鉴定师在10倍放大镜下评估——仅供阅读参考，并非诊断结论。",
        clarityGrades = listOf(
            DiamondClarityGrade("FL", "Flawless", "无瑕——经验丰富的宝石学家在10倍放大镜下看不到任何内含物或瑕疵。"),
            DiamondClarityGrade("IF", "Internally Flawless", "内部无瑕——10倍放大镜下看不到任何内部内含物；仅有可抛光去除的极轻微表面瑕疵。"),
            DiamondClarityGrade("VVS1", "Very Very Slightly Included 1", "极其微小的内含物，即使经验丰富也极难在10倍放大镜下发现。"),
            DiamondClarityGrade("VVS2", "Very Very Slightly Included 2", "极其微小的内含物，很难在10倍放大镜下发现。"),
            DiamondClarityGrade("VS1", "Very Slightly Included 1", "较小的内含物，在10倍放大镜下较难发现。"),
            DiamondClarityGrade("VS2", "Very Slightly Included 2", "较小的内含物，在10倍放大镜下相对容易发现。"),
            DiamondClarityGrade("SI1", "Slightly Included 1", "在10倍放大镜下清晰可见的内含物。"),
            DiamondClarityGrade("SI2", "Slightly Included 2", "在10倍放大镜下清晰可见的内含物，视具体宝石有时肉眼也能看到。"),
            DiamondClarityGrade("I1", "Included 1", "肉眼可见的内含物，对透明度影响有限。"),
            DiamondClarityGrade("I2", "Included 2", "肉眼可见的内含物，明显影响透明度和光泽。"),
            DiamondClarityGrade("I3", "Included 3", "肉眼非常明显的内含物，严重影响透明度和光泽。")
        )
    )

    private val byLanguage: Map<String, DiamondGradingPage> = mapOf(
        "fr" to fr,
        "en" to en,
        "es" to es,
        "it" to it,
        "de" to de,
        "pt" to pt,
        "ru" to ru,
        "nl" to nl,
        "zh" to zh
    )

    fun page(languageCode: String): DiamondGradingPage = byLanguage[languageCode] ?: fr
}
