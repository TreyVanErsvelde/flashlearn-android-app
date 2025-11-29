package com.example.flashlearn

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.flashlearn.databinding.ActivityTriviaModeBinding
import kotlin.math.abs

class TriviaModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTriviaModeBinding

    private lateinit var deck: Deck
    private var cards = mutableListOf<Card>()

    private var currentIndex = 0
    private var isBackVisible = false

    private var touchX = 0f
    private var touchY = 0f
    private val SWIPE_THRESHOLD = 120f
    private val TAP_THRESHOLD = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriviaModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deckIndex = intent.getIntExtra("deckIndex", -1)
        if (deckIndex == -1) {
            Toast.makeText(this, "Invalid deck!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        deck = FlashRepository.getInstance(this).decks[deckIndex]
        cards = deck.cards.shuffled().toMutableList()

        if (cards.isEmpty()) {
            Toast.makeText(this, "This deck has no cards!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        showCard()

        // SWIPE + TAP COMBINED LISTENER
        binding.flashcardContainer.setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }
    }

    private fun showCard() {
        val card = cards[currentIndex]

        binding.tvFront.text = card.question
        binding.tvBack.text = card.answer

        binding.tvFront.visibility = View.VISIBLE
        binding.tvBack.visibility = View.GONE
        isBackVisible = false

        binding.tvProgress.text = "${currentIndex + 1}/${cards.size}"
    }

    private fun flipCard() {
        if (!isBackVisible) {
            binding.tvFront.visibility = View.GONE
            binding.tvBack.visibility = View.VISIBLE
        } else {
            binding.tvFront.visibility = View.VISIBLE
            binding.tvBack.visibility = View.GONE
        }
        isBackVisible = !isBackVisible
    }

    // ⭐ Combined tap + swipe detector
    private fun handleTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - touchX
                val deltaY = event.y - touchY

                // Detect tap
                if (abs(deltaX) < TAP_THRESHOLD && abs(deltaY) < TAP_THRESHOLD) {
                    flipCard()
                    return
                }

                // Detect swipe
                if (abs(deltaX) > SWIPE_THRESHOLD) {
                    if (deltaX < 0) nextCard()
                    else prevCard()
                }
            }
        }
    }

    private fun nextCard() {
        if (currentIndex < cards.size - 1) {
            currentIndex++
            showCard()
        } else {
            Toast.makeText(this, "End of deck!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prevCard() {
        if (currentIndex > 0) {
            currentIndex--
            showCard()
        } else {
            Toast.makeText(this, "Start of deck!", Toast.LENGTH_SHORT).show()
        }
    }
}
