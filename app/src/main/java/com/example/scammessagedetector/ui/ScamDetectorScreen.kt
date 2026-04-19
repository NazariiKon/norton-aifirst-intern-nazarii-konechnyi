package com.example.scammessagedetector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scammessagedetector.domain.model.ExampleScam
import com.example.scammessagedetector.presentation.ScamDetectorViewModel
import com.example.scammessagedetector.ui.theme.*

/**
 * Main screen for the Scam Detector app.
 * Provides input for suspicious messages, example scam buttons, and result display.
 */
@Composable
fun ScamDetectorScreen(
    modifier: Modifier = Modifier,
    viewModel: ScamDetectorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Decorative Genie-inspired orb icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(Color(0xFF90CAF9), Color(0xFF42A5F5))
                            ),
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Enter text or URL",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message input area with a specialized "Paste" button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(NortonGray, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                TextField(
                    value = uiState.messageInput,
                    onValueChange = { viewModel.onMessageChange(it) },
                    modifier = Modifier.fillMaxSize(),
                    placeholder = { Text("Paste suspicious message here...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                // Blue "Paste" button for quick clipboard access
                Button(
                    onClick = {
                        clipboardManager.getText()?.text?.let { viewModel.onMessageChange(it) }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NortonBlue),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Icon(
                        Icons.Default.ContentPaste,
                        contentDescription = "Paste",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Paste", color = Color.White, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of pre-defined scam examples for easy testing
            Text(
                text = "Examples",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.examples) { example ->
                    ExampleChip(example) {
                        viewModel.onMessageChange(example.message)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Display risk assessment result if available
            uiState.analysisResult?.let { result ->
                ResultCard(result = result)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Show loading spinner during AI analysis
            if (uiState.isLoading) {
                CircularProgressIndicator(color = NortonBlue)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Display error messages (e.g., connection issues or validation errors)
            if (uiState.error != null) {
                Text(text = uiState.error!!, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Main "Scan now" button to trigger analysis
            Button(
                onClick = { viewModel.analyzeMessage() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NortonYellow),
                shape = RoundedCornerShape(28.dp),
                enabled = uiState.messageInput.isNotBlank() && !uiState.isLoading
            ) {
                Text(
                    text = "Scan now",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Clickable card representing a scam example.
 */
@Composable
fun ExampleChip(example: ExampleScam, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .widthIn(max = 200.dp)
            .clickable { onClick() },
        color = NortonGray,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                text = example.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = NortonBlue
            )
            Text(
                text = example.message,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.DarkGray
            )
        }
    }
}

/**
 * Result display card with color-coded risk levels.
 */
@Composable
fun ResultCard(result: com.example.scammessagedetector.domain.model.ScamAnalysisResult) {
    val color = when (result.riskLevel.uppercase()) {
        "SAFE" -> SafeGreen
        "SUSPICIOUS" -> SuspiciousYellow
        "DANGEROUS" -> DangerousRed
        else -> NortonBlue
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = result.riskLevel,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${(result.confidenceScore * 100).toInt()}% confidence",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = result.explanation, color = Color.Black, fontSize = 14.sp)
        }
    }
}
