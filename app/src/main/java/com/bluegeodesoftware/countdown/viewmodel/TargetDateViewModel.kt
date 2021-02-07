package com.bluegeodesoftware.countdown.viewmodel

import androidx.lifecycle.*
import com.bluegeodesoftware.countdown.entity.TargetDate
import com.bluegeodesoftware.countdown.repository.TargetDateRepository
import kotlinx.coroutines.launch

class TargetDateViewModel(private val repository: TargetDateRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allTargetDates: LiveData<List<TargetDate>> = repository.allTargetDates.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(date: TargetDate) = viewModelScope.launch {
        repository.insert(date)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class TargetDateViewModelFactory(private val repository: TargetDateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TargetDateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TargetDateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}