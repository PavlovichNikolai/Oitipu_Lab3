package com.pavlovich.lab3

import androidx.recyclerview.widget.DiffUtil

class UserListDiffUtilCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: User, newItem: User) =
        oldItem.name == oldItem.name && oldItem.score == newItem.score

}