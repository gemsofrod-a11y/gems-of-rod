package fr.gemsofrod.assistant.triage

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/** Programme/arrête le cycle de triage automatique périodique — 15 minutes
 * est le minimum imposé par Android pour un travail périodique (contre 5
 * minutes en continu côté serveur ; Android impose ce plancher pour
 * préserver la batterie). */
object TriageScheduler {
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<TriageWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TriageWorker.UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(TriageWorker.UNIQUE_WORK_NAME)
    }
}
