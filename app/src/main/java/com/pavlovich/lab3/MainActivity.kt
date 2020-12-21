package com.pavlovich.lab3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pavlovich.lab3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var currentQuestion = 0
    private var currentScore = 0
    private var unsavedScore = 0

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()

        firebaseAnalytics = Firebase.analytics

        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewAd.loadAd(adRequest)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(createQuizQuestionFragment(), R.id.fragment_layout)
        }


        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            var fragment: Fragment? = null

            when (it.itemId) {
                R.id.play -> {
                    fragment = createQuizQuestionFragment()
                }
                R.id.profile -> {
                    fragment = UserListFragment()
                }
            }

            fragment?.let { fr ->
                replaceFragment(fr, R.id.fragment_layout)
            }

            true
        }
    }

    private fun createQuizQuestionFragment() = QuizQuestionFragment().apply {
        currentQuestionNumber = currentQuestion
        currentScore = this@MainActivity.currentScore
        unsavedScore = this@MainActivity.unsavedScore
        saveScore = { current, unsaved ->
            currentScore = current
            unsavedScore = unsaved
        }
        saveCurrentQuestion = {
            currentQuestion = it
        }
    }

}