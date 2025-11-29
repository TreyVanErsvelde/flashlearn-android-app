package com.example.flashlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flashlearn.databinding.ActivityTriviaResultsBinding

class TriviaResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTriviaResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriviaResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val correct = intent.getIntExtra("correct", 0)
        val total = intent.getIntExtra("total", 0)

        binding.textScore.text = "Score: $correct / $total"

        binding.btnDone.setOnClickListener {
            finish()
        }
    }
}
