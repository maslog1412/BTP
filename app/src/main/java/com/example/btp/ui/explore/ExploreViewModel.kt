package com.example.btp.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btp.model.Location
import com.example.btp.utils.Result
import com.example.btp.utils.getSampleDestinations
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private var coroutineExceptionHandler: CoroutineExceptionHandler

    private val _destinationList: MutableLiveData<Result<List<Location>>> = MutableLiveData()
    val destinationList: LiveData<Result<List<Location>>>
        get() = _destinationList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _destinationList.value = Result.Failure(exception)
        }
        getDestinationList()
    }

    private fun getDestinationList() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _destinationList.value = Result.Loading
            // In the actual code, we would make an API request here to fetch some data
            _destinationList.value = Result.Success(getSampleDestinations())
        }
    }
}
