package br.com.euvickson.areaderapp.repository

import br.com.euvickson.areaderapp.data.Resource
import br.com.euvickson.areaderapp.model.Item
import br.com.euvickson.areaderapp.network.BooksApi
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksApi) {
    suspend fun getBooks(searchQuerry: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)
            val itemList = api.getAllBooks(searchQuerry).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        } catch (e: Exception) {
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getBookInfo(bookid: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            api.getBookInfo(bookid)
        } catch (e: Exception) {
            return Resource.Error(message = "An error occured ${e.message.toString()}")
        }

        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

}