package com.arison62dev.findit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.arison62dev.findit.presentation.navigation.FindITNavGraph
import com.arison62dev.findit.presentation.screen.FindItSplashScreen
import com.arison62dev.findit.presentation.ui.theme.FindITTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FindITTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FindITNavGraph(rememberNavController())
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FindITTheme {
        FindITNavGraph()
    }
}