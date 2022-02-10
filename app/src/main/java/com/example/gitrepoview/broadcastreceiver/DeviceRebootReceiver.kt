package com.example.gitrepoview.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.gitrepoview.viewmodel.MainActivityViewModel
import com.example.gitrepoview.workmanager.DataSyncWorker
import java.util.concurrent.TimeUnit

class DeviceRebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val workManager: WorkManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(DataSyncWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        workManager.enqueueUniquePeriodicWork(
            "unique_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}