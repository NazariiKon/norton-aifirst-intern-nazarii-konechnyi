package com.example.scammessagedetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.scammessagedetector.ui.ScamDetectorScreen
import com.example.scammessagedetector.ui.theme.ScamMessageDetectorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display support
        enableEdgeToEdge()
        setContent {
            ScamMessageDetectorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Main application screen with padding for system bars
                    ScamDetectorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
