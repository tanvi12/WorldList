package com.example.worldlist.ui.screens.countryList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.worldlist.network.utils.interfaces.RepositoryInterface

class CountryListViewModel(
    private val repository: RepositoryInterface
) : ViewModel() {
}

fun fetchCountryList() {

}

internal class CountryListViewModelFactory(
    private val repository: RepositoryInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CountryListViewModel(repository) as T
    }
}