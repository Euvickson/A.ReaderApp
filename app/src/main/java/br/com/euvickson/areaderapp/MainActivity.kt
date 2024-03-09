package br.com.euvickson.areaderapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.euvickson.areaderapp.ui.theme.AReaderAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AReaderAppTheme {
                val db = FirebaseFirestore.getInstance()
                val user: MutableMap<String, Any> = HashMap()
                user["firstName"] = "Jeo"
                user["lastName"] = "James"

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")

                    db.collection("users").add(user).addOnSuccessListener {
                        Log.d("FB", "onCreate: ${it.id}")
                    }.addOnFailureListener {
                        Log.d("FB", "onCreate: ${it}")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AReaderAppTheme {
        Greeting("Android")
    }
}