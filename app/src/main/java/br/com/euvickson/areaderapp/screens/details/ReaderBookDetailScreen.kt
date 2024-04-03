package br.com.euvickson.areaderapp.screens.details

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.euvickson.areaderapp.components.ReaderAppBar
import br.com.euvickson.areaderapp.components.RoundedButton
import br.com.euvickson.areaderapp.data.Resource
import br.com.euvickson.areaderapp.model.Item
import br.com.euvickson.areaderapp.model.MBook
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Detail",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //A different way to get the Resource from the API. Instead of using a variable in the View Model, it can be called using the Produce State
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId = bookId)
                }.value

                if (bookInfo.data == null) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                } else {
                    ShowDetails(bookInfo, navController)
                }

            }

        }
    }
}

@Composable
fun ShowDetails(bookInfo: Resource<Item>, navController: NavHostController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.padding(34.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = bookData!!.imageLinks.thumbnail),
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(90.dp)
                    .height(90.dp)
                    .padding(1.dp)
            )
        }
        Text(
            text = bookData?.title.toString(),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            overflow = TextOverflow.Ellipsis,
            maxLines = 19,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Authors: ${bookData?.authors.toString()}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Page Count: ${bookData?.pageCount.toString()}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Categories: ${bookData?.categories.toString()}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Published: ${bookData?.publishedDate.toString()}",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        val cleaDescription = HtmlCompat.fromHtml(
            bookData!!.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()

        val localDims = LocalContext.current.resources.displayMetrics

        Surface(
            modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.15f))
                .padding(4.dp),
            shape = RectangleShape,
            border = BorderStroke(1.dp, color = Color.LightGray)
        ) {

            LazyColumn(modifier = Modifier.padding(3.dp)) {
                item {
                    Text(text = cleaDescription)
                }
            }
        }
        Row(
            modifier = Modifier.padding(top = 5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current

            RoundedButton(
                label = "Save",
                radius = 20
            ) {
                //Save this Book to the Firestore Database
                val book = MBook(
                    title = bookData.title,
                    authors = bookData.authors.toString(),
                    description = bookData.description,
                    categories = bookData.categories.toString(),
                    notes = "",
                    photoUrl = bookData.imageLinks.thumbnail,
                    publishedDate = bookData.publishedDate,
                    pageCount = bookData.pageCount.toString(),
                    rating = 0.0,
                    googleBookId = googleBookId,
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
                try {
                    saveToFirebase(book, navController)
                    Toast.makeText(context, "Book Saved", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Error saving book. Try Again Latter", Toast.LENGTH_LONG).show()
                }

            }

            Spacer(modifier = Modifier.width(25.dp))

            RoundedButton(
                label = "Cancel",
                radius = 20
            ) {
                navController.popBackStack()
            }

        }
    }

}

fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }
                    }.addOnFailureListener {
                        Log.d("Error", "saveToFirebase: Error updating doc", it)
                    }
            }
    } else {
        navController.popBackStack()
        throw Exception("Error sending the book to the Firebase")
    }

}
