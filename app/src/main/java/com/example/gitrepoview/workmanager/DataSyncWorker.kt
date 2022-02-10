package com.example.gitrepoview.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gitrepoview.database.AppDatabase
import com.example.gitrepoview.network.API
import com.example.gitrepoview.network.APIInterface
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DataSyncWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {

    var apiService = API.getApiService()
    var repoDAO = AppDatabase.getInstance(context)!!.repoDao()
    var pageNo : Int = 1
    var perPage : Int = 10

    override fun doWork(): Result {

        pageNo = repoDAO.getPageNo()

        var isSuccess = false
        val map = HashMap<String, String>()
        map["q"] = "android";
        map["sort"] = "stars";
        map["order"] = "desc";
        map["per_page"] = perPage.toString();
        map["page"] = pageNo++.toString();

        try {
            CoroutineScope(Dispatchers.IO).launch {
                val response = API.getApiService().getRepos(map)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()!!.items.forEach {
                            it.pageNo = pageNo
                        }
                        withContext(Dispatchers.IO) {
                            repoDAO.insertRepo(response.body()!!.items)
                        }

                        isSuccess = true
                        Log.v("RESPONSE_Success", response.body()!!.items.size.toString())
                    } else {
                        isSuccess = false
                        Log.v("RESPONSE_ERROR", Gson().toJson(response.message()))
                    }
                }
            }

            Log.v("PageNo",pageNo.toString())

            return if (isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }

    }

}