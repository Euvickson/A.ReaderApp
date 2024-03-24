package br.com.euvickson.areaderapp.model

data class MBook(
    //As long as we are working with Firebase, the variables need to be "var's", because the value will be changed when we pass to the Firestore Database
    var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null
)
