package com.example.scammessagedetector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scammessagedetector.R
import com.example.scammessagedetector.BuildConfig
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
    // Scroll state for the entire screen to handle smaller devices and long results
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState), // Makes the whole screen scrollable
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScamDetectorHeader()

            Spacer(modifier = Modifier.height(8.dp))

            MessageInputSection(
                value = uiState.messageInput,
                onValueChange = { viewModel.onMessageChange(it) },
                onPasteClick = {
                    clipboardManager.getText()?.text?.let { viewModel.onMessageChange(it) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExamplesSection(
                examples = uiState.examples,
                onExampleClick = { viewModel.onMessageChange(it.message) }
            )

            // Space between input and results
            Spacer(modifier = Modifier.height(24.dp))

            AnalysisResultSection(
                result = uiState.analysisResult,
                isLoading = uiState.isLoading,
                error = uiState.error
            )

            // Push the button to the bottom if there is space, or just after content
            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(16.dp))

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
                    text = stringResource(R.string.scan_button),
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
 * Header section with the decorative orb and title.
 */
@Composable
fun ScamDetectorHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            text = stringResource(R.string.input_label),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Section for message input with a paste button.
 */
@Composable
fun MessageInputSection(
    value: String,
    onValueChange: (String) -> Unit,
    onPasteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(NortonGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxSize(),
            placeholder = { Text(stringResource(R.string.input_placeholder)) },
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
            onClick = onPasteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .height(36.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NortonBlue),
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
        ) {
            Icon(
                Icons.Default.ContentPaste,
                contentDescription = stringResource(R.string.paste_button),
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(R.string.paste_button), color = Color.White, fontSize = 14.sp)
        }
    }
}

/**
 * Section displaying clickable scam examples.
 */
@Composable
fun ExamplesSection(
    examples: List<ExampleScam>,
    onExampleClick: (ExampleScam) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.examples_label),
            style = MaterialTheme.typography.titleSmall,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(examples) { example ->
                ExampleChip(example) {
                    onExampleClick(example)
                }
            }
        }
    }
}

/**
 * Section for displaying analysis results, loading states, or errors.
 */
@Composable
fun AnalysisResultSection(
    result: com.example.scammessagedetector.domain.model.ScamAnalysisResult?,
    isLoading: Boolean,
    error: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        result?.let {
            ResultCard(result = it)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(color = NortonBlue)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (error != null) {
            Text(text = error, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
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
                    text = stringResource(
                        R.string.confidence_format,
                        (result.confidenceScore * 100).toInt()
                    ),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // The explanation text is inside a scrollable screen, so it can be any length.
            Text(text = result.explanation, color = Color.Black, fontSize = 14.sp)
        }
    }
}
