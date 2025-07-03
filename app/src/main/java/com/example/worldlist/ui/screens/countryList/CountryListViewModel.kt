package com.example.worldlist.ui.screens.countryList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.worldlist.data.CountryRegion
import com.example.worldlist.data.CountryResponse
import com.example.worldlist.data.UiState
import com.example.worldlist.network.utils.interfaces.RepositoryInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {

    private val _currentCountryListContentState =
        MutableStateFlow<UiState<List<CountryResponse>>>(UiState.Loading)
    val currentCountryListContentState: StateFlow<UiState<List<CountryResponse>>> =
        _currentCountryListContentState.asStateFlow()

    private val _currentRegionSelection = MutableStateFlow(CountryRegion.ASIA)

    private val listExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _currentCountryListContentState.value = UiState.Error(exception)
    }

    init {
        fetchCountryList()
    }

    fun setRegionSelection(region: CountryRegion) {
        _currentRegionSelection.value = region
    }

    fun fetchCountryList() {
        viewModelScope.launch(Dispatchers.IO + listExceptionHandler) {
            _currentRegionSelection.collectLatest { region ->
                repository.getCountryList(region).collect { countryList ->
                    _currentCountryListContentState.value = UiState.Success(countryList)
                }
            }

        }
    }
}

internal class CountryListViewModelFactory(
    private val repository: RepositoryInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CountryListViewModel(repository) as T
    }
}