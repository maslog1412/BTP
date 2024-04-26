package com.example.btp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btp.model.Budget
import com.example.btp.model.Location
import com.example.btp.utils.Result
import com.example.btp.utils.getSampleBudgets
import com.example.btp.utils.getSampleDestinations
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private var coroutineExceptionHandler: CoroutineExceptionHandler

    private val _destinationList: MutableLiveData<Result<List<Location>>> = MutableLiveData()
    val destinationList: LiveData<Result<List<Location>>>
        get() = _destinationList

    private val _budgetList: MutableLiveData<Result<List<Budget>>> = MutableLiveData()
    val budgetList: LiveData<Result<List<Budget>>>
        get() = _budgetList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _destinationList.value = Result.Failure(exception)
            _budgetList.value = Result.Failure(exception)
        }
        getDestinationList()
        getBudgetList()
    }

    private fun getDestinationList() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _destinationList.value = Result.Loading
            // In the actual code, we would make an API request here to fetch some data
            _destinationList.value = Result.Success(getSampleDestinations())
        }
    }

    private fun getBudgetList() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _budgetList.value = Result.Loading
            // In the actual code, we would make an API request here to fetch some data
            _budgetList.value = Result.Success(getSampleBudgets())
        }
    }
}