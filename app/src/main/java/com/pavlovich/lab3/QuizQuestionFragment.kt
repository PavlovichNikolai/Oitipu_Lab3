package com.pavlovich.lab3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pavlovich.lab3.databinding.LayoutQuizQuestionBinding
import es.dmoral.toasty.Toasty


class QuizQuestionFragment : Fragment() {

    private lateinit var binding: LayoutQuizQuestionBinding
    private var questions: MutableList<QuizQuestion>? = null

    var currentQuestionNumber = 0
    var currentScore = 0
    var unsavedScore = 0
    var saveCurrentQuestion: ((Int) -> Unit)? = null
    var saveScore: ((Int, Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutQuizQuestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getQuestions()
    }


    fun setQuestionData(quizQuestion: QuizQuestion, currentScore: Int) {
        binding.btnFirst.apply {
            setOnClickListener {
                onAnswerClick(this.text.toString())
            }
            text = quizQuestion.first
        }

        binding.btnSecond.apply {
            setOnClickListener {
                onAnswerClick(this.text.toString())
            }
            text = quizQuestion.second
        }

        binding.btnThird.apply {
            setOnClickListener {
                onAnswerClick(this.text.toString())
            }
            text = quizQuestion.third
        }

        binding.btnForth.apply {
            setOnClickListener {
                onAnswerClick(this.text.toString())
            }
            text = quizQuestion.fourth
        }

        binding.btnSave.setOnClickListener {
            fragmentManager?.let { it1 ->
                EnterNameBottomSheetFragment().apply {
                    onSaveClick = {
                        saveResult(it)
                        Toasty.success(requireContext(), "Saved").show()
                    }
                }.show(it1, "SAVE_DLG")
            }
        }

        binding.tvQuestion.text = quizQuestion.question
        binding.tvScore.text = currentScore.toString()
    }

    private fun updateFragmentData(quizQuestion: QuizQuestion, currentScore: Int) {
        binding.btnFirst.text = quizQuestion.first
        binding.btnSecond.text = quizQuestion.second
        binding.btnThird.text = quizQuestion.third
        binding.btnForth.text = quizQuestion.fourth
        binding.tvQuestion.text = quizQuestion.question
        binding.tvScore.text = currentScore.toString()
    }

    private fun onAnswerClick(answer: String) {
        if (answer == questions?.get(currentQuestionNumber)?.right) {
            currentScore++
            unsavedScore++
            saveScore?.invoke(currentScore, unsavedScore)
            Toasty.success(requireContext(), "Right!", Toast.LENGTH_SHORT).show()
        } else {
            Toasty.error(requireContext(), "Wrong!", Toast.LENGTH_SHORT).show()
        }

        currentQuestionNumber++

        saveCurrentQuestion?.invoke(currentQuestionNumber)

        if (currentQuestionNumber >= (questions?.size ?: 0)) {
            currentQuestionNumber = 0
        }

        questions?.get(currentQuestionNumber)?.let { question ->
            updateFragmentData(question, currentScore)
        }

    }

    private fun getQuestions() {
        val ref = Firebase.database.reference
        ref.child("questions").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.e(QuizQuestionFragment::class.simpleName, error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                questions = snapshot.getValue<MutableList<QuizQuestion>>()

                questions?.get(currentQuestionNumber)?.let {
                    setQuestionData(it, currentScore)
                }
            }

        })
    }

    private fun saveResult(name: String) {
        val ref = Firebase.database.reference
        ref.child("users").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.e(QuizQuestionFragment::class.simpleName, error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val key = it.key
                    val user = it.getValue<User>()
                    if (user?.name == name) {
                        ref.child("users").child(key ?: "")
                            .updateChildren(mapOf(Pair("score", (user.score ?: 0) + unsavedScore)))
                        unsavedScore = 0
                        saveScore?.invoke(currentScore, unsavedScore)
                        return
                    }
                }
                ref.child("users").push().setValue(User(name = name, score = currentScore))
            }

        })
    }

}