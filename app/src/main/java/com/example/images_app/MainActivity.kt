package com.example.images_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.images_app.GalleryViewModel.GalleryViewModel
import com.example.images_app.ui.theme.MyApplicationTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowImage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ShowImage(modifier: Modifier = Modifier, viewModel: GalleryViewModel = viewModel()) {
    // imagem atual baseada no índice
    val currentImage = if (viewModel.images.isNotEmpty()) {
        viewModel.images[viewModel.currentIndex]
    } else {
        null
    }

    val isPreview = LocalInspectionMode.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE8F0EE))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(2.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentImage != null || isPreview) {
                    AsyncImage(
                        model = currentImage?.downloadUrl,
                        contentDescription = "Obra de arte por ${currentImage?.author}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = if (isPreview) painterResource(R.drawable.wall_filler) else null
                    )
                } else {
                    CircularProgressIndicator() // Mostra carregando enquanto a API não responde
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD6D6E3), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Picsum Image ID: ${currentImage?.id ?: "..."}",
                fontSize = 18.sp,
                color = Color.DarkGray
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = currentImage?.author ?: "Carregando autor...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = " (Fotografia)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { viewModel.previousImage() },
                enabled = viewModel.currentIndex > 0, // Desativa se for a primeira imagem
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(50) // Botão arredondado
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Anterior",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Previous",
                    color = Color.Black
                )
            }

            Button(
                onClick = { viewModel.nextImage() },
                enabled = viewModel.currentIndex < viewModel.images.size - 1, // Desativa na última
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Next",
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = "Próximo",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowImagePreview() {
    MyApplicationTheme {
        ShowImage()
    }
}