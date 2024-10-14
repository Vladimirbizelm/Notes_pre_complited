package com.example.notescompleted


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notescompleted.databinding.ActivityMainBinding

const val PREFERENCE = "Preferences"
const val KEY = "key_for_text_edit"

class MainActivity : AppCompatActivity(), RecyclerViewInterface {


    private lateinit var binding: ActivityMainBinding

    private var headings: MutableList<String> =
        mutableListOf("example_H1", "example_H2", "example_H3", "example_H4", "example_H5")
    private var descriptions: MutableList<String> = mutableListOf(
        "example_description1",
        "example_description2",
        "example_description3",
        "example_description4",
        "example_description5",
    )
    private val adapter = NoteAdapter()
    private val dataModule: DataModule by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)

        unzipJsonMakeBastard(KEY)
        init()
        addNote()
        getInfoFromFragment()
        initOnclickListener()
        findNote()
        fragmentClosed()
        infoBtn()

    }

    private fun makeJsonBastard() {
        val shrPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var arrayInString = ""

        for (i in 0..<headings.size) {
            arrayInString += "~${headings[i]} ${descriptions[i]}"
        }
        shrPreferences.edit().putString(KEY, arrayInString).apply()
    }

    private fun unzipJsonMakeBastard(key: String) {
        val shrPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val json = shrPreferences.getString(key, "")
        val a = json?.split("~")?.filter { it.isNotEmpty() }

        if (a != null) {
            for (i in a) {
                val curr = i.split(" ")
                if (curr[0] !in headings) {
                    headings.add(curr[0])
                    descriptions.add(curr[1])
                }

            }
        }
    }

    private fun init() {
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, true)
            recyclerView.adapter = adapter
            headings.forEachIndexed { index, value ->
                val note = Note(value, descriptions[index])
                adapter.addNote(note)

            }
        }
    }


    private fun createFragment(frag: Fragment, HolderId: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(HolderId, frag).addToBackStack("frag").commit()
        hideMainElements()
    }

    private fun addNote() {
        binding.addBtn.setOnClickListener {
            createFragment(FragmentAddNote.newInstance(), R.id.addNotePlaceHolder)
            dataModule.fragForSearchOrAddOrUpdate.value = "Add"
        }
    }

    private fun getInfoFromFragment() {
        dataModule.afterWhat.observe(this) { it ->
            when (it) {
                "Add" -> {
                    dataModule.msgForActivity.observe(this) {
                        if (it.heading !in headings && it.description !in descriptions) {
                            adapter.addNote(it)
                            headings.add(it.heading)
                            descriptions.add(it.description)
                        }
                    }
                }

                "Update" -> {
                    dataModule.changedList.observe(this) {
                    }
                    // TODO: fix dataModule
                }
            }
        }

    }

    private fun findNote() {
        binding.searchBtn.setOnClickListener {
            createFragment(FragmentAddNote.newInstance(), R.id.addNotePlaceHolder)
            dataModule.fragForSearchOrAddOrUpdate.value = "Search"
            dataModule.dataHeadings.value = headings
            dataModule.dataDescriptions.value = descriptions
        }

    }

    private fun initOnclickListener() {
        adapter.setOnClickListener(object :
            NoteAdapter.OnClickListener {
            override fun onClick(position: Int, model: Note) {
                dataModule?.msgForFragment?.value = model
                createFragment(FragmentAddNote.newInstance(), R.id.addNotePlaceHolder)
                dataModule.noteForUpdate.value = position
                dataModule.fragForSearchOrAddOrUpdate.value = "Update"
                dataModule.noteList.value = adapter.getList()
            }
        })
    }

    //update visibility of all elements of main activity when fragment is closed
    private fun fragmentClosed() {
        dataModule.fragIsClosed.observe(this) {
            if (it) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.searchBtn.visibility = View.VISIBLE
                binding.addBtn.visibility = View.VISIBLE
                binding.infoBtn.visibility = View.VISIBLE
                binding.tv.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClick(position: Int): Note {
        TODO("Not yet implemented")
    }

    private fun infoBtn() {
        binding.infoBtn.setOnClickListener {
            Toast.makeText(
                this,
                "${getString(R.string.poweredBy)} \n ${getString(R.string.versionControl)}",
                Toast.LENGTH_LONG
            ).show()
        }
        val shrPreferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        var arrayInString = ""
        for (i in 0..<headings.size) {
            arrayInString += "~${headings[i]} ${descriptions[i]}"
        }
        shrPreferences.edit().putString(KEY, arrayInString).apply()
        Log.d("TAG", "from making ${shrPreferences.getString(KEY, "")}")
    }

    private fun hideMainElements() {
        binding.recyclerView.visibility = View.GONE
        binding.searchBtn.visibility = View.GONE
        binding.addBtn.visibility = View.GONE
        binding.infoBtn.visibility = View.GONE
        binding.tv.visibility = View.GONE
    }


    override fun onDestroy() {
        makeJsonBastard()
        super.onDestroy()
    }

    override fun onStop() {
        makeJsonBastard()
        super.onStop()
    }
}
