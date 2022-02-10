package com.example.gitrepoview.database

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitrepoview.model.RepoItem

@Dao
interface RepositoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepo(repo: ArrayList<RepoItem>)

    @Query("select * from tbl_repo order by id desc limit 10")
    fun getAllRepos(): List<RepoItem>

    @Query("select MAX(pageNo) from tbl_repo")
    fun getPageNo():Int
}