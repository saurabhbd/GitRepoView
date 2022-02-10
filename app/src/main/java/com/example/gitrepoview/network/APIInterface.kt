package com.example.gitrepoview.network

import com.example.gitrepoview.model.RepoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIInterface {

    @GET("search/repositories")
    suspend fun getRepos(@QueryMap filter: Map<String, String>): Response<RepoModel>
}