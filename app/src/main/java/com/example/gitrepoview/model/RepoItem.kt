package com.example.gitrepoview.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_repo")
data class RepoItem(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var name : String,
    var url : String,
    var pageNo : Int
)
