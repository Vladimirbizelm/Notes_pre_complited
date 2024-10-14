package com.example.notescompleted


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notescompleted.databinding.FragmentAddNoteBinding

class FragmentAddNote : Fragment(), RecyclerViewInterface {

    lateinit var binding: FragmentAddNoteBinding
    private val dataModule: DataModule by activityViewModels()
    private val adapter = NoteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dataHeadings = dataModule.dataHeadings.value?.toList()
        val dataDescriptions = dataModule.dataDescriptions.value?.toList()
        binding.rvFrag.visibility = View.VISIBLE
        when (dataModule.fragForSearchOrAddOrUpdate.value) {
            "Search" -> {

                dataModule.afterWhat.value = "Search"
                val drawable: Drawable? =
                    ResourcesCompat.getDrawable(resources, R.drawable.search, null)
                binding.saveNoteFragment.icon = drawable
                binding.editDescriptionFragment.visibility = View.GONE

                binding.apply {
                    rvFrag.layoutManager = LinearLayoutManager(context)
                    rvFrag.adapter = adapter
                    rvFrag.visibility = View.VISIBLE
                }

                val textWatcher: TextWatcher = object : TextWatcher {

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        adapter.clearNotes()
                        if (dataHeadings != null && !s.isNullOrEmpty()) {
                            for (i in dataHeadings) {
                                if (i.contains(s.toString())) {
                                    val ii = dataDescriptions?.get(dataHeadings.indexOf(i))
                                    val note = Note(i, ii.toString())
                                    adapter.addNote(note)
                                }
                            }
                        }
                    }
                }
                binding.editHeadingFragment.addTextChangedListener(textWatcher)
            }

            "Add" -> {
                dataModule.afterWhat.value = "Add"
                binding.saveNoteFragment.setOnClickListener {
                    val heading = binding.editHeadingFragment.getText().toString()
                    val description = binding.editDescriptionFragment.getText().toString()

                    if (heading.isNotEmpty() && description.isNotEmpty()) {
                        dataModule.msgForActivity.value = Note(heading, description)

                    } else {
                        Toast.makeText(context, "One of the fields is empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            "Update" -> {


                dataModule.afterWhat.value = "Update"
                val noteList = dataModule.noteList.value
                initOnclickListener()
                binding.saveNoteFragment.setOnClickListener {

                    if (noteList != null) {
                        val head = binding.editHeadingFragment.getText().toString()
                        val des = binding.editDescriptionFragment.getText().toString()

                        if (!noteList.contains(Note(head, des))) {
                            noteList.add(Note(head, des))
                            dataModule.changedList.value = noteList
                            dataModule.msgForActivity.value = Note(head, des)
                        }
                    }
                }
            }
        }


        binding.backFragmentBtn.setOnClickListener {
            val heading = binding.editHeadingFragment.getText().toString()
            val description = binding.editDescriptionFragment.getText().toString()

            if (heading.isNotEmpty() && description.isNotEmpty()) {
                dataModule.msgForActivity.value = Note(heading, description)
            }

            binding.editDescriptionFragment.visibility = View.GONE

            dataModule.msgForFragment.value = Note("", "")
            dataModule.fragIsClosed.value = true
            dataModule.fragForSearchOrAddOrUpdate.value = ""
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        dataModule.msgForFragment.observe(viewLifecycleOwner) {
            binding.editHeadingFragment.setText(it.heading)
            binding.editDescriptionFragment.setText(it.description)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentAddNote()

    }

    override fun onItemClick(position: Int): Note {
        TODO("Not yet implemented")
    }

    private fun initOnclickListener() {
        adapter.setOnClickListener(object :
            NoteAdapter.OnClickListener {
            override fun onClick(position: Int, model: Note) {
            }
        })
    }

    override fun onStop() {
        val heading = binding.editHeadingFragment.getText().toString()
        val description = binding.editDescriptionFragment.getText().toString()

        if (heading.isNotEmpty() && description.isNotEmpty()) {
            dataModule.msgForActivity.value = Note(heading, description)
        }
        binding.editDescriptionFragment.visibility = View.GONE
        dataModule.msgForFragment.value = Note("", "")
        dataModule.fragIsClosed.value = true
        dataModule.fragForSearchOrAddOrUpdate.value = ""
        super.onStop()
    }
}