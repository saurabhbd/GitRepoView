package com.example.gitrepoview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitrepoview.adapter.RepoAdapter
import com.example.gitrepoview.broadcastreceiver.DeviceRebootReceiver
import com.example.gitrepoview.databinding.ActivityMainBinding
import com.example.gitrepoview.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        insertReposInDB()
        loadRepos()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getRepos()
            binding.swipeRefresh.isRefreshing = false
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(DeviceRebootReceiver(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))
    }

    private fun insertReposInDB() {
        viewModel.getReposAndInsertInDB()
    }

    private fun loadRepos() {
        viewModel.repositoriesListMutable.observe(this, { repoList ->
            if (!repoList.isNullOrEmpty()) {
                val adapter = RepoAdapter(this, repoList)
                val layoutManager = LinearLayoutManager(this)
                binding.rvRepositories.layoutManager = layoutManager
                binding.rvRepositories.adapter = adapter
            }
        })
    }
}