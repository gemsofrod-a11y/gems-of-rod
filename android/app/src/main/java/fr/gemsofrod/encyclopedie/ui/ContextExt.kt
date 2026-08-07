package fr.gemsofrod.encyclopedie.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Retrouve l'Activity sous-jacente d'un [Context] Compose. `LocalContext.current`
 * n'est pas toujours l'Activity elle-même : Compose peut fournir un
 * `ContextWrapper` (ex. thème, decor view) autour de celle-ci. Un simple
 * `context as? Activity` échoue alors silencieusement — notamment pour
 * `Activity.recreate()`, nécessaire au rechargement des ressources après un
 * changement de langue.
 */
tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
