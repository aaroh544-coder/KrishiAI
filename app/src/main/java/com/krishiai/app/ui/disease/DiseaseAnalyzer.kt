package com.krishiai.app.ui.disease

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.IOException

class DiseaseAnalyzer(private val context: Context) {

    private var imageClassifier: ImageClassifier? = null

    init {
        setupClassifier()
    }

    private fun setupClassifier() {
        // Model file must be in src/main/assets
        // val modelName = "disease_model.tflite" 
        // try {
        //     val options = ImageClassifier.ImageClassifierOptions.builder()
        //         .setMaxResults(3)
        //         .build()
        //     imageClassifier = ImageClassifier.createFromFileAndOptions(context, modelName, options)
        // } catch (e: IOException) {
        //     e.printStackTrace()
        // }
    }

    fun analyze(bitmap: Bitmap): String {
        if (imageClassifier == null) {
            return "Model not initialized (Mock: Healthy)"
        }

        val tensorImage = TensorImage.fromBitmap(bitmap)
        val results = imageClassifier?.classify(tensorImage)

        return results?.firstOrNull()?.categories?.firstOrNull()?.label ?: "Unknown"
    }
}
