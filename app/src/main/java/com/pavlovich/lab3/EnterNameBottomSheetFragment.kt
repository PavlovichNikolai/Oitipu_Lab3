package com.pavlovich.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pavlovich.lab3.databinding.FragmentEnterNameBinding

class EnterNameBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentEnterNameBinding

    var onSaveClick: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterNameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            onSaveClick?.invoke(binding.etUsername.text.toString())
            this.dismiss()
        }
    }

}