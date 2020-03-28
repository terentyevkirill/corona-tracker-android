package com.terentiev.coronatracker.ui.world

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorldViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is world Fragment"
    }
    val text: LiveData<String> = _text
}