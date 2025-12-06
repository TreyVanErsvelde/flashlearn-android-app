package com.example.flashlearn

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val cards: MutableList<Card>,
    private val deckIndex: Int,
    private val repository: FlashRepository
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQ: TextView = itemView.findViewById(R.id.tvCardQuestion)
        val tvA: TextView = itemView.findViewById(R.id.tvCardAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]

        holder.tvQ.text = card.question
        holder.tvA.text = card.answer

        // 👇 Tap to EDIT card
        holder.itemView.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener
            showEditCardDialog(holder, pos)
        }

        // 👇 Long press to DELETE card
        holder.itemView.setOnLongClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnLongClickListener true

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Card?")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete") { _, _ ->
                    // delete ONCE from repository, which already holds the same list
                    repository.deleteCard(deckIndex, pos)

                    // cards points to the same list, so it's already updated
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, cards.size)
                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }
    }

    // ✏️ Edit dialog
    private fun showEditCardDialog(holder: CardViewHolder, position: Int) {
        val context = holder.itemView.context
        val card = cards[position]

        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_card, null)

        val etQ = dialogView.findViewById<EditText>(R.id.etEditQuestion)
        val etA = dialogView.findViewById<EditText>(R.id.etEditAnswer)

        // Pre-fill
        etQ.setText(card.question)
        etA.setText(card.answer)

        AlertDialog.Builder(context)
            .setTitle("Edit Card")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                val newQ = etQ.text.toString().trim()
                val newA = etA.text.toString().trim()

                if (newQ.isEmpty() || newA.isEmpty()) {
                    Toast.makeText(context, "Both fields required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Update repository (persists + same list)
                repository.updateCard(deckIndex, position, newQ, newA)

                // Update list item for UI
                card.question = newQ
                card.answer = newA
                notifyItemChanged(position)

                Toast.makeText(context, "Card updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun getItemCount() = cards.size
}
