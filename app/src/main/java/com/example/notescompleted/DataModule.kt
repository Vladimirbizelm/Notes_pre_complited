package com.example.notescompleted


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataModule : ViewModel() {

    // TODO: fix all the unused data and make it less
    val msgForActivity: MutableLiveData<Note> by lazy {
        MutableLiveData<Note>()
    }
    val msgForFragment: MutableLiveData<Note> by lazy {
        MutableLiveData<Note>()
    }
    val fragIsClosed: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val fragForSearchOrAddOrUpdate: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val dataHeadings: MutableLiveData<MutableList<String>> by lazy {
        MutableLiveData<MutableList<String>>()
    }
    val dataDescriptions: MutableLiveData<MutableList<String>> by lazy {
        MutableLiveData<MutableList<String>>()
    }
    val noteForUpdate: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val noteList: MutableLiveData<ArrayList<Note>> by lazy {
        MutableLiveData<ArrayList<Note>>()
    }

    val changedList: MutableLiveData<ArrayList<Note>> by lazy {
        MutableLiveData<ArrayList<Note>>()
    }

    val afterWhat: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}