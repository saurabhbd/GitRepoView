package com.example.gitrepoview.adapter

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitrepoview.databinding.RepoListItemBinding
import com.example.gitrepoview.model.RepoItem

class RepoAdapter(private val context: Context, private val repolist: ArrayList<RepoItem>) :
    RecyclerView.Adapter<RepoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RepoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvRepoName.text = repolist[position].name
        holder.binding.tvRepoUrl.text = repolist[position].url
        holder.binding.tvRepoUrl.movementMethod = LinkMovementMethod.getInstance();
    }

    override fun getItemCount(): Int {
        return repolist.size
    }

    class ViewHolder(val binding: RepoListItemBinding) : RecyclerView.ViewHolder(binding.root)
}