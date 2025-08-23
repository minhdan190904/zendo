package com.gig.zendo.ui.presentation.Test


data class StepWrapper(
    val steps: List<String>
)


import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.DrawableRes
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.gson.Gson

class GenerateSolutionSteps(
    private val context: Context     // truyền context vào constructor
) {

    /**  Phân tích bài toán từ ảnh nằm trong drawable  */
    suspend fun generateSteps(@DrawableRes drawableId: Int): List<String> {

        /* 1. Nạp bitmap từ drawable  */
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)

        /* 2. Định nghĩa schema JSON  */
        val jsonSchema = Schema.obj(
            mapOf("steps" to Schema.array(Schema.string()))
        )

        /* 3. Khởi tạo model Gemini  */
        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-2.5-flash",
                generationConfig = generationConfig {
                    responseMimeType = "application/json"
                    responseSchema = jsonSchema
                }
            )

        /* 4. Tạo prompt đa phương tiện  */
        val prompt = content {
            image(bitmap)
            text(
                """
                Đây là hình chụp một bài toán. 
                Trả về JSON chứa mảng "steps" liệt kê ngắn gọn từng bước giải 
                (chỉ JSON, không văn bản ngoài).
                """.trimIndent()
            )
        }

        /* 5. Gọi Gemini  */
        val json = model.generateContent(prompt).text ?: return emptyList()

        /* 6. Parse JSON thành List<String>  */
        val steps = Gson().fromJson(json, StepWrapper::class.java).steps

        steps.forEach { Log.i("SolutionStep", it) }
        return steps
    }
}

