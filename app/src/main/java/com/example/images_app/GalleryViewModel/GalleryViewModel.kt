package com.example.images_app.GalleryViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import com.example.images_app.model.ImageInfo



class GalleryViewModel : ViewModel() {
    val images = mutableStateListOf<ImageInfo>() // lista

    var currentIndex by mutableIntStateOf(0)

    init {
        fetchImages()
    }

    private fun fetchImages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Buscando uma lista de 20 imagens da API
                val response = URL("https://picsum.photos/v2/list?page=1&limit=20").readText()
                val jsonArray = JSONArray(response)

                val loadedImages = mutableListOf<ImageInfo>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    loadedImages.add(
                        ImageInfo(
                            id = obj.getString("id"),
                            width = obj.getInt("width"),
                            height = obj.getInt("height"),
                            author = obj.getString("author"),
                            url = obj.getString("url"),
                            downloadUrl = obj.getString("download_url")
                        )
                    )
                }

                // Atualiza a interface na thread principal
                withContext(Dispatchers.Main) {
                    images.addAll(loadedImages)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun nextImage() {
        if (currentIndex < images.size - 1) {
            currentIndex++
        }
    }

    fun previousImage() {
        if (currentIndex > 0) {
            currentIndex--
        }
    }
}
