package com.terentiev.coronatracker.ui

import androidx.lifecycle.ViewModel
import com.terentiev.coronatracker.data.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(private var repo: Repository): ViewModel() {

}

