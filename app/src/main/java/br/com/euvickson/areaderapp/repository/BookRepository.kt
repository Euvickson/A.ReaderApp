package br.com.euvickson.areaderapp.repository

import br.com.euvickson.areaderapp.data.Resource
import br.com.euvickson.areaderapp.model.Item
import br.com.euvickson.areaderapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookId)
        } catch (e: Exception) {
            return Resource.Error(message = "An error occurred ${e.message.toString()}")
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

}