package br.com.euvickson.areaderapp.repository

import android.util.Log
import br.com.euvickson.areaderapp.data.DataOrException
import br.com.euvickson.areaderapp.model.Book
import br.com.euvickson.areaderapp.model.Item
import br.com.euvickson.areaderapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {

    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private var bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()
    suspend fun getBooks(searchQuerry: String): DataOrException<List<Item>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuerry).items
            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        }catch (e: Exception) {
            dataOrException.e = e
        }

        return dataOrException
    }

    suspend fun getBookInfo(bookid: String) {
        try {
            dataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookid)
            if (bookInfoDataOrException.data.toString().isNotEmpty()) bookInfoDataOrException.loading = false
        } catch (e: Exception) {
            bookInfoDataOrException.e = e
            bookInfoDataOrException.loading = false
            Log.d("TAG", "getBookInfo: $e")
        }

    }

}