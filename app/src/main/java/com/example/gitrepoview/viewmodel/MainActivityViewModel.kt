package com.example.gitrepoview.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStore
import androidx.work.*
import com.example.gitrepoview.database.AppDatabase
import com.example.gitrepoview.model.RepoItem
import com.example.gitrepoview.model.RepoModel
import com.example.gitrepoview.network.API
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.http.QueryMap
import com.example.gitrepoview.workmanager.DataSyncWorker
import java.util.concurrent.TimeUnit


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var repositoriesListMutable = MutableLiveData<ArrayList<RepoItem>>()

    private val db = AppDatabase.getInstance(getApplication())!!.repoDao()

    fun getReposAndInsertInDB() {

        val workManager: WorkManager = WorkManager.getInstance(getApplication())

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

        getRepos()
    }

    fun getRepos() {
        CoroutineScope(Dispatchers.IO).launch {
            repositoriesListMutable.postValue(db.getAllRepos() as ArrayList<RepoItem>?)
        }
    }
}