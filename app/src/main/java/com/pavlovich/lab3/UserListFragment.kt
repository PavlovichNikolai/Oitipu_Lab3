package com.pavlovich.lab3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pavlovich.lab3.databinding.FragmentUserListBinding


class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private var adapter: UserListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserListAdapter()

        binding.rvLeaders.apply {
            adapter = this@UserListFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        getUsers()
    }

    private fun getUsers() {
        Firebase.database.reference.child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    Log.e(UserListFragment::class.simpleName, error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = mutableListOf<User>()

                    snapshot.children.forEach {
                        val user = it.getValue(User::class.java)
                        user?.let { it1 -> users.add(it1) }
                    }

                    setUsers(users)

                }
            })
    }

    private fun setUsers(users: MutableList<User>) {
        users.sortByDescending { it.score }
        users.forEachIndexed { pos, it ->
            it.position = pos + 1
        }
        adapter?.setData(users)
    }

}