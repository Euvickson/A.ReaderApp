package br.com.euvickson.areaderapp.screens.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.euvickson.areaderapp.components.ReaderAppBar
import br.com.euvickson.areaderapp.model.Item
import br.com.euvickson.areaderapp.model.MBook
import br.com.euvickson.areaderapp.screens.home.HomeScreenViewModel
import br.com.euvickson.areaderapp.screens.search.BookItem
import br.com.euvickson.areaderapp.utils.formatDate
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderStatsScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            //only Show Books by this user that have been read
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {
                        Icon(imageVector = Icons.Sharp.Person, contentDescription = "icon")
                    }
                    Text(
                        text = "Hi, ${
                            currentUser?.email.toString().split('@')[0].uppercase(Locale.ROOT)
                        }"
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = CircleShape, elevation = CardDefaults.cardElevation(5.dp)
                ) {
                    val readBooksList: List<MBook> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            books.filter { mBook ->
                                mBook.userId == currentUser?.uid && (mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }

                    val readingBooks = books.filter { mBook ->
                        mBook.userId == currentUser?.uid && (mBook.startedReading != null) && mBook.finishedReading == null
                    }

                    Column(
                        modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(text = "Your Stats", style = MaterialTheme.typography.bodyLarge)
                        Divider()
                        Text(text = "You're reading: ${readingBooks.size} books")
                        Text(text = "You've read ${readBooksList.size} books")
                    }

                }


                Divider()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    //filter books by finished
                    val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        viewModel.data.value.data!!.filter { mBook ->
                            mBook.userId == currentUser?.uid && mBook.finishedReading != null
                        }
                    } else {
                        emptyList()
                    }

                    items(readBooks) { books ->
                        BookStatsItemRow(book = books)
                    }

                }

            }

        }
    }
}

@Composable
fun BookStatsItemRow(book: MBook) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            val imageUrl: String =
                book.photoUrl.toString()
                    .ifEmpty { "http://books.google.com/books/content?id=-1y8CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api" }
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(80.dp)
                    .padding(8.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {

                Row (horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = book.title.toString(),
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    if (book.rating!! >= 4) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.8f))

                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "ThumbUp",
                            tint = Color(0xFF58AD46)
                        )

                    } else {
                        Box {}
                    }
                }
                Text(
                    text = "Author: ${book.authors}",
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = "Started: ${formatDate(book.startedReading!!)}",
                    softWrap = true,
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Finished ${formatDate(book.finishedReading!!)}",
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}