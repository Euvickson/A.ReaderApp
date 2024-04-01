package br.com.euvickson.areaderapp.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.euvickson.areaderapp.components.InputField
import br.com.euvickson.areaderapp.components.ReaderAppBar
import br.com.euvickson.areaderapp.model.Item
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController, viewModel: BookSearchViewModel = hiltViewModel()) {

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }

        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {

                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    viewModel
                ) {searchQuery ->
                    viewModel.searchBooks(searchQuery)
                }

                BookList(
                    navController = navController,
                )

            }
        }
    }

}

@Composable
fun BookList(
    navController: NavHostController,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    val listOfBooks = viewModel.list

    LazyColumn (modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)){
        items(listOfBooks) { book ->
            BookItem(book) {
                Log.d("TAG", "BookList: $book")
            }
        }
    }
}

@Composable
fun BookItem(book: Item, onItemClicked: () -> Unit) {
    Card (
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onItemClicked.invoke() },
    ){
        Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            val imageUrl: String = book.volumeInfo.imageLinks.smallThumbnail.ifEmpty { "http://books.google.com/books/content?id=-1y8CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api" }
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(80.dp)
                    .padding(8.dp)
            )

            Column (horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                Text(text = book.volumeInfo.title, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}", style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic), maxLines = 1, overflow = TextOverflow.Clip, modifier = Modifier.padding(start = 4.dp))
                Text(text = book.volumeInfo.publishedDate, style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = book.volumeInfo.language, style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    val searchQueryState = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchQueryState.value) { searchQueryState.value.trim().isNotEmpty() }

    Column(modifier = modifier) {
        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }

}