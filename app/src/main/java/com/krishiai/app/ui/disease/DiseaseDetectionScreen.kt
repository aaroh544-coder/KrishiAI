package com.krishiai.app.ui.disease

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.krishiai.app.R

@Composable
fun DiseaseDetectionScreen() {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission needed", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permission == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            hasPermission = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasPermission) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Please enable camera access")
        }
        return
    }

    if (capturedBitmap != null) {
        // Analysis Result View
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Analysis Result", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            
            Image(
                bitmap = capturedBitmap!!.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (isAnalyzing) {
                CircularProgressIndicator()
                Text("Analyzing leaf...")
            } else {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (resultText.contains("Healthy")) Color.Green else Color.Red
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { 
                    capturedBitmap = null 
                    resultText = ""
                }) {
                    Text("Scan Another")
                }
            }
        }
    } else {
        // Camera Preview View
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(modifier = Modifier.fillMaxSize())
            
            // Capture Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            ) {
                 Button(
                     onClick = {
                         // Mock capture for now since CameraController is easier but requires changing Preview setup
                         // Or use a simple mock behavior:
                         isAnalyzing = true
                         // Simulate capture and analysis
                         // In a real app, use ImageCapture use case
                         Toast.makeText(context, "Scanning...", Toast.LENGTH_SHORT).show()
                         
                         // Mock Logic to transition state
                         // Ideally we capture a bitmap here. 
                         // For this simulation without a physical device behaving perfectly, 
                         // we might need a workaround or just simulate the flow.
                         
                         // Let's mock a success flow after 1s
                         // In real impl, we'd take a picture via ImageCapture
                         val mockBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                         mockBitmap.eraseColor(android.graphics.Color.GREEN)
                         capturedBitmap = mockBitmap
                         
                         // Simulate Analysis
                         // Use DiseaseAnalyzer here
                         val isHealthy = Math.random() > 0.5
                         resultText = if (isHealthy) "Healthy Crop" else "Disease Detected: Leaf Blight"
                         isAnalyzing = false
                     },
                     modifier = Modifier.size(80.dp),
                     shape = CircleShape,
                     
                 ) {
                     // Inner circle
                 }
            }
            
            Text(
                text = "Point camera at leaf",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                color = Color.White
            )
        }
    }
}
