package com.example.worldlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.worldlist.data.UiState
import com.example.worldlist.ui.screens.countryList.CountryListViewModel
import com.example.worldlist.ui.screens.countryList.CountryListViewModelFactory
import com.example.worldlist.ui.screens.countryList.CountryRepository

class CountyListActivity : ComponentActivity() {
    private val viewModel: CountryListViewModel by viewModels {
        CountryListViewModelFactory(CountryRepository())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val countryList = viewModel.currentCountryListContentState.collectAsStateWithLifecycle()
            when (val state = countryList.value) {
                is UiState.Success -> {
                    val responseSize = state.data.size
                    print("Result size: $responseSize")
                }

                else -> {

                }
            }
        }
    }
}
