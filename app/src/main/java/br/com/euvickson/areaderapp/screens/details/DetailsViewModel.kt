package br.com.euvickson.areaderapp.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.euvickson.areaderapp.data.Resource
import br.com.euvickson.areaderapp.model.Item
import br.com.euvickson.areaderapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {
    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfo(bookId)
    }
}