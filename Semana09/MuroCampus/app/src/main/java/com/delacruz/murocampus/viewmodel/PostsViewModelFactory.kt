package com.delacruz.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delacruz.murocampus.data.PostRepository

/**
 * Crea el PostsViewModel inyectando el Repository.
 * Así el ViewModel no conoce Retrofit ni necesita un constructor vacío.
 */
class PostsViewModelFactory(
    private val repo: PostRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
