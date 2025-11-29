package com.example.flashlearn

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Card?")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete") { _, _ ->
                    repository.deleteCard(deckIndex, position)
                    cards.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cards.size)
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }

    }

    override fun getItemCount() = cards.size
}
