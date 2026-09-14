package fr.gemsofrod.encyclopedie.data

/**
 * Types de sertissage proposés par le compositeur de bague. Les 6
 * premiers sont proposés à la fois pour la pierre centrale et les
 * pierres annexes ([availableForCentral]) ; les 4 derniers (pavage
 * en ligne continue) n'ont de sens que pour une rangée de pierres
 * annexes et ne sont donc proposés que pour celles-ci.
 */
enum class RingSertissage(val label: String, val labelKey: String, val availableForCentral: Boolean) {
    GRIFFES("Griffes", "ring_sertissage_griffes", availableForCentral = true),
    CLOS("Clos", "ring_sertissage_clos", availableForCentral = true),
    DEMI_CLOS("Demi-clos", "ring_sertissage_demiclos", availableForCentral = true),
    PAVE("Pavé", "ring_sertissage_pave", availableForCentral = true),
    RAIL("Rail (chenal)", "ring_sertissage_rail", availableForCentral = true),
    RAS("Ras (affleurant)", "ring_sertissage_ras", availableForCentral = true),
    BRIGHT_CUT("Bright-cut", "ring_sertissage_brightcut", availableForCentral = false),
    BARRETTE("Barrette", "ring_sertissage_barrette", availableForCentral = false),
    CLUSTER("Grappe (cluster)", "ring_sertissage_cluster", availableForCentral = false),
    GRAIN("Grain", "ring_sertissage_grain", availableForCentral = false)
}
