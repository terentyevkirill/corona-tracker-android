package com.terentiev.coronatracker.ui.marked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MarkedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is marked Fragment"
    }
    val text: LiveData<String> = _text
}