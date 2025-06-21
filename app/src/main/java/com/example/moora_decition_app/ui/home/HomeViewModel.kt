package com.example.moora_decition_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moora_decition_app.data.repository.AlternatifRepository
import com.example.moora_decition_app.model.Alternatif
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AlternatifRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Alternatif>>(emptyList())
    val items: LiveData<List<Alternatif>> = _items

    init {
        loadData()
    }


    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getAll()
            _items.postValue(list)
        }
    }


    fun addItem(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.create(name)
            loadData() // Refresh list
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
            loadData()
        }
    }
}
