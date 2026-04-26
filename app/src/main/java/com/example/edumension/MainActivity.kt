package com.example.edumension

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.edumension.ui.navigation.AppNavigation
import com.example.edumension.ui.theme.EdumensionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EdumensionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Adding padding box is not strictly necessary as AppNavigation handles its own screens,
                    // but we can pass padding if needed. For now, AppNavigation is the root.
                    Modifier.padding(innerPadding)
                    AppNavigation()
                }
            }
        }
    }
}