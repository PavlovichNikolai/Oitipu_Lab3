package com.pavlovich.lab3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.pavlovich.lab3.databinding.UserListElementBinding


class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private val differ = AsyncListDiffer<User>(this, UserListDiffUtilCallback())

    class ViewHolder(var itemBinding: UserListElementBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(user: User) {
            itemBinding.tvRank.text = user.position.toString()
            itemBinding.tvScore.text = user.score.toString()
            itemBinding.tvUsername.text = user.name.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        UserListElementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(differ.currentList[position])
    }

    fun setData(userList: MutableList<User>) {
        differ.submitList(userList)
    }

}