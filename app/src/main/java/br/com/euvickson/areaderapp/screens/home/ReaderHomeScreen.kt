package br.com.euvickson.areaderapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.euvickson.areaderapp.components.FABContent
import br.com.euvickson.areaderapp.components.ListCard
import br.com.euvickson.areaderapp.components.ReaderAppBar
import br.com.euvickson.areaderapp.components.TitleSection
import br.com.euvickson.areaderapp.model.MBook
import br.com.euvickson.areaderapp.navigation.ReaderScreens
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

        ReadingRightNowArea(books = listOf(), navController = navController)
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    ListCard()
}
