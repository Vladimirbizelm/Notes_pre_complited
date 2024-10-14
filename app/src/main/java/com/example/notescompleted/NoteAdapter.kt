package com.example.notescompleted


import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notescompleted.databinding.NoteItemBinding

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteHolder>(), RecyclerViewInterface {

    private val noteList = ArrayList<Note>()
    private var onClickListener: OnClickListener? = null


    class NoteHolder(item: View): RecyclerView.ViewHolder(item){

        private val binding = NoteItemBinding.bind(item)

        fun bind(note: Note) = with(binding){
            title.text = note.heading
            description.text = note.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {

        val currentNote = noteList[position]

        holder.bind(noteList[position])
        // Set click listener for the item view
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, currentNote)
        }
    }

    // Set the click listener for the adapter
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: Note)
    }

    // TODO: fix notifyDataSetChange
    fun addNote(note: Note) {
        noteList.add(note)
        notifyDataSetChanged()
    }

    // TODO: fix notifyDataSetChange
    fun clearNotes() {
        noteList.clear()
        notifyItemRangeChanged(0,0)
    }

    fun getList(): ArrayList<Note> {
        return noteList
    }

    override fun onItemClick(position: Int): Note {
        return noteList[position]
    }
}