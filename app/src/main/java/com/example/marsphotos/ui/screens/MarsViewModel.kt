/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.model.MarsPhoto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface MarsUiState {
    ///data class Success(val photos: String) : MarsUiState
    ///data class Success(val photos: MarsPhoto) : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

    /*
     * The value for the constructor parameter comes from the application container because
     * the app is now using dependency injection.
    */
class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
        viewModelScope.launch {
            marsUiState = MarsUiState.Loading
            marsUiState = try {
                ///val listResult = MarsApi.retrofitService.getPhotos()

                /*
                you need to update the ViewModel code to use the repository to get the
                data as Android best practices suggest.
                 */

                /*
                 * Instead of the ViewModel directly making the network request for the
                 * data, the repository provides the data. The ViewModel no longer directly
                 * references the MarsApi code.
                 */

                ///val marsPhotosRepository= NetworkMarsPhotosRepository()
                ///val listResult = marsPhotosRepository.getMarsPhotos()




//                MarsUiState.Success(
//                    "Success: ${listResult.size} Mars photos retrieved"
//                )

                ///Assign the first photo object at index 0.
                MarsUiState.Success(marsPhotosRepository.getMarsPhotos())
            } catch (e: IOException) {
                MarsUiState.Error
            } catch (e: HttpException) {
                MarsUiState.Error
            }
        }
    }

        /*
         *Android framework does not allow a ViewModel to be passed values in the constructor
         *when created, we implement a ViewModelProvider.Factory object, which lets us get
         *around this limitation.
         */

        /*
         *The Factory pattern is a creational pattern used to create objects. The
         * MarsViewModel.Factory object uses the application container to retrieve the
         * marsPhotosRepository, and then passes this repository to the ViewModel when the
         * ViewModel object is created.
         */
        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                /*
                define the actual instructions for creating your ViewModel. This block gets
                executed when the factory is asked to create a new MarsViewModel.
                 */
                initializer {
                    val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                    val marsPhotosRepository = application.container.marsPhotosRepository
                    MarsViewModel(marsPhotosRepository = marsPhotosRepository)
                }
            }
        }
}
