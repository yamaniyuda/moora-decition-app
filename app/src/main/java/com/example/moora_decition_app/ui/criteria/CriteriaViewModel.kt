package com.example.moora_decition_app.ui.criteria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CriteriaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is criteria Fragment"
    }
    val text: LiveData<String> = _text
}