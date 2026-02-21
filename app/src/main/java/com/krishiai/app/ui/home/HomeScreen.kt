package com.krishiai.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krishiai.app.data.model.Mandi
import com.krishiai.app.data.model.Weather

@Composable
fun HomeScreen(
    onScanCropClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Namaste, Farmer!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Here's what's happening today on your farm.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Tip of the Day Card
        TipCard(tip = uiState.tipOfTheDay)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Quick Summaries", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Weather Summary Card
            SummaryCard(
                title = "Weather",
                value = if (uiState.weather != null) "${uiState.weather?.temperature}°C" else "--",
                subtitle = uiState.weather?.description ?: "Fetching...",
                icon = Icons.Default.Thermostat,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )

            // Mandi Summary Card
            SummaryCard(
                title = "Mandi Price",
                value = if (uiState.latestMandi != null) "₹${uiState.latestMandi?.price}" else "--",
                subtitle = uiState.latestMandi?.commodity ?: "Fetching...",
                icon = Icons.Default.ShoppingBag,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions
        Text(text = "Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        QuickActionItem(
            title = "Scan Crop for Diseases",
            subtitle = "Use AI to identify plant issues instantly",
            icon = Icons.Default.CameraAlt,
            onClick = onScanCropClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TipCard(tip: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Tip of the Day", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = tip, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color
) {
    Card(
        modifier = modifier.height(140.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.labelMedium)
                Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall, maxLines = 1)
            }
        }
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}
