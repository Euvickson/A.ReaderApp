package br.com.euvickson.areaderapp.repository

import br.com.euvickson.areaderapp.data.DataOrException
import br.com.euvickson.areaderapp.data.Resource
import br.com.euvickson.areaderapp.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(
    private val queryBook: Query
) {

    suspend fun getAllBooksFromDatabase (): DataOrException<List<MBook>, Boolean, Exception>{
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
        }catch (e: FirebaseFirestoreException) {
           dataOrException.e = e
        }

        return dataOrException

    }


}