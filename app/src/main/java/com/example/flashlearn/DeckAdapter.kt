package com.example.flashlearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeckAdapter(
    private val decks: List<Deck>,
    private val onDeckClick: (Int) -> Unit      // callback to MainActivity
) : RecyclerView.Adapter<DeckAdapter.DeckViewHolder>() {

    class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvDeckName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDeckDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = decks[position]

        holder.tvName.text = deck.name
        holder.tvDesc.text = deck.description

        // Now just notify main activity
        holder.itemView.setOnClickListener {
            onDeckClick(position)
        }
    }

    override fun getItemCount(): Int = decks.size
}
