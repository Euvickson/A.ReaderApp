package br.com.euvickson.areaderapp.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.euvickson.areaderapp.components.FABContent
import br.com.euvickson.areaderapp.components.ReaderAppBar
import br.com.euvickson.areaderapp.components.TitleSection
import br.com.euvickson.areaderapp.model.MBook
import br.com.euvickson.areaderapp.navigation.ReaderScreens
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {

    Scaffold(
        topBar = {
            ReaderAppBar(title = "A.Reader", navController = navController)
        },
        floatingActionButton = {
            FABContent {

            }
        },

        ) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeContent(navController = navController)
        }

    }
}

@Composable
fun HomeContent(navController: NavController) {

    val currentUserName = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    } else {
        "N/A"
    }

    Column(
        modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ){
        Row (modifier = Modifier.align(alignment = Alignment.Start)){
            TitleSection(label = "Your reading \n " + "activity right now")
            
            Spacer(modifier = Modifier.fillMaxWidth(fraction = 0.7f))

            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip)

                Divider()
            }

        }

        ListCard()
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

}

@Preview
@Composable
fun ListCard(
    book: MBook = MBook(
        "asdf",
        "Runnig",
        "Me And You",
        "Hello World"
    ),
    onPressDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card (shape = RoundedCornerShape(29.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary)) {
        
        
        Column (modifier = Modifier.width(screenWidth.dp - (spacing * 2)), horizontalAlignment = Alignment.Start){

            Row (horizontalArrangement = Arrangement.Center) {

                Image(
                    painter = rememberAsyncImagePainter(model = "http://books.google.com/books/content?id=-1y8CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(50.dp))
                
                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    BookRating(score = 3.5)

                }

            }

            Text(text = "Book title",
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = "Authors: All...",
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.labelMedium)

        }

        Row (
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom){
            RoundedButton(label = "Reading", radius = 70)

        }


    }

}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.onPrimary
    ){

        Column (modifier = Modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally){
            Icon(imageVector = Icons.Filled.StarBorder, contentDescription = "Star", modifier = Modifier.padding(3.dp))
            Text(text = score.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}
) {

    Surface (modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius,
    )),
        color = MaterialTheme.colorScheme.onTertiaryContainer){

        Column (modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(color = MaterialTheme.colorScheme.inverseOnSurface, fontSize = 15.sp))
        }
    }

}