package com.example.flashlearn

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DeckAdapter(
    private val decks: MutableList<Deck>,
    private val repository: FlashRepository,
    private val onDeckSelected: (Int) -> Unit
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

        // TAP → only SELECT deck
        holder.itemView.setOnClickListener {
            onDeckSelected(position)
        }

        // LONG TAP → menu
        holder.itemView.setOnLongClickListener {

            val options = arrayOf("Open Deck", "Edit Deck", "Delete Deck")

            AlertDialog.Builder(holder.itemView.context)
                .setItems(options) { _, which ->
                    when (which) {

                        // OPEN DECK
                        0 -> {
                            val intent = Intent(
                                holder.itemView.context,
                                DeckActivity::class.java
                            )
                            intent.putExtra("deckIndex", position)
                            holder.itemView.context.startActivity(intent)
                        }

                        // ✏️ EDIT DECK
                        1 -> {
                            showEditDeckDialog(holder, position)
                        }

                        // DELETE DECK
                        2 -> {
                            AlertDialog.Builder(holder.itemView.context)
                                .setTitle("Delete Deck?")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Delete") { _, _ ->
                                    repository.deleteDeck(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, decks.size)
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }
                    }
                }
                .show()

            true
        }
    }

    // ✏️ EDIT DECK FUNCTION
    private fun showEditDeckDialog(holder: DeckViewHolder, position: Int) {

        val context = holder.itemView.context
        val deck = decks[position]

        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_deck, null)

        val etName = dialogView.findViewById<EditText>(R.id.etEditDeckName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etEditDeckDesc)

        etName.setText(deck.name)
        etDesc.setText(deck.description)

        AlertDialog.Builder(context)
            .setTitle("Edit Deck")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->

                val newName = etName.text.toString().trim()
                val newDesc = etDesc.text.toString().trim()

                if (newName.isEmpty()) {
                    Toast.makeText(context, "Name required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Update database
                repository.updateDeck(position, newName, newDesc)

                // Update UI list
                deck.name = newName
                deck.description = newDesc

                notifyItemChanged(position)

                Toast.makeText(context, "Deck updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun getItemCount(): Int = decks.size
}
