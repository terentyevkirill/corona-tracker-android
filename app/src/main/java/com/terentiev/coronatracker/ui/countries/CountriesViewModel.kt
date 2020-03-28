package com.terentiev.coronatracker.ui.countries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountriesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is countries Fragment"
    }
    val text: LiveData<String> = _text
}