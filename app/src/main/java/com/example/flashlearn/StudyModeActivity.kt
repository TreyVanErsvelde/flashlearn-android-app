package com.example.flashlearn

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudyModeActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var tvProgress: TextView
    private lateinit var btnPrev: Button
    private lateinit var btnFlip: Button
    private lateinit var btnNext: Button
    private lateinit var btnShuffle: Button
    private lateinit var btnRestart: Button

    private lateinit var repository: FlashRepository
    private var deckIndex = 0
    private var currentCardIndex = 0
    private var isShowingAnswer = false
    private var currentCards = mutableListOf<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_mode)

        repository = FlashRepository.getInstance(this)


        // Get deck index from intent
        deckIndex = intent.getIntExtra("deckIndex", 0)

        // Check if we have decks and cards
        if (repository.decks.isEmpty()) {
            Toast.makeText(this, "No decks available!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (deckIndex >= repository.decks.size) {
            Toast.makeText(this, "Invalid deck selected!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val selectedDeck = repository.decks[deckIndex]
        currentCards = selectedDeck.cards.toMutableList()

        if (currentCards.isEmpty()) {
            Toast.makeText(this, "No cards in this deck!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        initViews()
        updateCardDisplay()
    }

    private fun initViews() {
        tvQuestion = findViewById(R.id.tvQuestion)
        tvAnswer = findViewById(R.id.tvAnswer)
        tvProgress = findViewById(R.id.tvProgress)
        btnPrev = findViewById(R.id.btnPrev)
        btnFlip = findViewById(R.id.btnFlip)
        btnNext = findViewById(R.id.btnNext)
        btnShuffle = findViewById(R.id.btnShuffle)
        btnRestart = findViewById(R.id.btnRestart)

        btnPrev.setOnClickListener { showPreviousCard() }
        btnNext.setOnClickListener { showNextCard() }
        btnFlip.setOnClickListener { flipCard() }
        btnShuffle.setOnClickListener { shuffleCards() }
        btnRestart.setOnClickListener { restartDeck() }
    }

    private fun updateCardDisplay() {
        if (currentCards.isEmpty()) {
            tvQuestion.text = "No cards available"
            tvAnswer.text = ""
            tvProgress.text = "0/0"
            return
        }

        val currentCard = currentCards[currentCardIndex]
        tvQuestion.text = currentCard.question ?: "No question"

        if (isShowingAnswer) {
            tvAnswer.text = currentCard.answer ?: "No answer"
            tvAnswer.visibility = TextView.VISIBLE
        } else {
            tvAnswer.visibility = TextView.GONE
        }

        tvProgress.text = "${currentCardIndex + 1}/${currentCards.size}"
    }

    private fun showNextCard() {
        if (currentCards.isEmpty()) return

        isShowingAnswer = false
        currentCardIndex = (currentCardIndex + 1) % currentCards.size
        updateCardDisplay()
    }

    private fun showPreviousCard() {
        if (currentCards.isEmpty()) return

        isShowingAnswer = false
        currentCardIndex = if (currentCardIndex - 1 < 0) {
            currentCards.size - 1
        } else {
            currentCardIndex - 1
        }
        updateCardDisplay()
    }

    private fun flipCard() {
        if (currentCards.isEmpty()) return

        isShowingAnswer = !isShowingAnswer
        updateCardDisplay()


        btnFlip.text = "Show"
    }


    private fun shuffleCards() {
        if (currentCards.isEmpty()) return

        currentCards.shuffle()
        currentCardIndex = 0
        isShowingAnswer = false
        updateCardDisplay()
        Toast.makeText(this, "Deck shuffled!", Toast.LENGTH_SHORT).show()
    }

    private fun restartDeck() {
        if (currentCards.isEmpty()) return

        currentCardIndex = 0
        isShowingAnswer = false
        updateCardDisplay()
        Toast.makeText(this, "Deck restarted!", Toast.LENGTH_SHORT).show()
    }
}