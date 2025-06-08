package com.example.artspace

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceApp()
        }
    }
}

data class ArtPiece(val imageRes: Int, val title: String, val author: String, val year: Int, val description: String)

@Composable
fun ArtSpaceApp() {
    var currentIndex by remember { mutableStateOf(0) }
    var showPreviousTooltip by remember { mutableStateOf(false) }
    var showNextTooltip by remember { mutableStateOf(false) }
    var isSwiping by remember { mutableStateOf(false) }

    // 定義圖片、標題、作者和描述
    val artPieces = listOf(
        ArtPiece(R.drawable.image1, "Gray Cat's Quiet Moment", "Sophie Lin", 2021, "A gray cat with vivid green eyes, captured in a serene moment while grooming its paw."),
        ArtPiece(R.drawable.image2, "Eagle's Piercing Stare", "Mark Thompson", 2020, "A bald eagle with a piercing gaze, its white head contrasting sharply against a dark background."),
        ArtPiece(R.drawable.image3, "Leopard's Blue-Eyed Focus", "Clara Evans", 2022, "A leopard with mesmerizing blue eyes, its spotted coat highlighted in a close-up shot."),
        ArtPiece(R.drawable.image4, "Tiger's Morning Stretch", "Henry Wu", 2023, "A tiger stretching gracefully in a grassy field, surrounded by trees and natural light."),
        ArtPiece(R.drawable.image5, "Pug's Curious Glance", "Rachel Kim", 2019, "A black pug in a black-and-white portrait, looking up with a curious and expressive gaze."),
        ArtPiece(R.drawable.image6, "Joyful White Owl", "Ethan Park", 2024, "A white owl with its beak open in what looks like a joyful laugh, set against a soft background."),
        ArtPiece(R.drawable.image7, "Llamas in the Meadow", "Laura Bennett", 2018, "Two fluffy llamas standing side by side in a meadow, with trees and a wooden fence in the background."),
        ArtPiece(R.drawable.image8, "Giraffe Over the Savanna", "Thomas Reed", 2017, "A giraffe standing tall in a dry savanna, with distant mountains and sparse trees in the background."),
        ArtPiece(R.drawable.image9, "Stylish Bunny in Yellow", "Mia Johnson", 2025, "A white bunny wearing stylish yellow sunglasses, posing against a vibrant yellow background."),
        ArtPiece(R.drawable.image10, "Wolf on Rocky Terrain", "Daniel Foster", 2016, "A gray wolf standing proudly on rocky terrain, surrounded by a rugged natural landscape.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = artPieces[currentIndex].imageRes),
                contentDescription = artPieces[currentIndex].title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.67f)
                    .heightIn(max = 600.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { isSwiping = true },
                            onDragEnd = { isSwiping = false },
                            onDragCancel = { isSwiping = false }
                        ) { _, dragAmount ->
                            if (isSwiping) {
                                if (dragAmount < 0) {
                                    currentIndex = if (currentIndex < artPieces.size - 1) {
                                        currentIndex + 1
                                    } else {
                                        0
                                    }
                                } else if (dragAmount > 0) {
                                    currentIndex = if (currentIndex > 0) {
                                        currentIndex - 1
                                    } else {
                                        artPieces.size - 1
                                    }
                                }
                                isSwiping = false
                            }
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = artPieces[currentIndex].title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "by ${artPieces[currentIndex].author} ${artPieces[currentIndex].year}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Button(
                    onClick = {
                        currentIndex = if (currentIndex > 0) {
                            currentIndex - 1
                        } else {
                            artPieces.size - 1
                        }
                        Log.d("ArtSpace", "Previous button clicked, currentIndex: $currentIndex")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showPreviousTooltip = true
                                    Log.d("ArtSpace", "Previous button long pressed, showPreviousTooltip: $showPreviousTooltip")
                                },
                                onPress = {
                                    try {
                                        awaitPointerEventScope {
                                            val event = awaitPointerEvent()
                                            if (!event.changes.any { it.pressed }) {
                                                showPreviousTooltip = false
                                                Log.d("ArtSpace", "Previous button released, showPreviousTooltip: $showPreviousTooltip")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        showPreviousTooltip = false
                                        Log.d("ArtSpace", "Previous button released with exception, showPreviousTooltip: $showPreviousTooltip")
                                    }
                                }
                            )
                        }
                ) {
                    Text("Previous")
                }

                if (showPreviousTooltip) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.9f))
                            .padding(12.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        Text(
                            text = "Show the previous photo",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Button(
                    onClick = {
                        currentIndex = if (currentIndex < artPieces.size - 1) {
                            currentIndex + 1
                        } else {
                            0
                        }
                        Log.d("ArtSpace", "Next button clicked, currentIndex: $currentIndex")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    showNextTooltip = true
                                    Log.d("ArtSpace", "Next button long pressed, showNextTooltip: $showNextTooltip")
                                },
                                onPress = {
                                    try {
                                        awaitPointerEventScope {
                                            val event = awaitPointerEvent()
                                            if (!event.changes.any { it.pressed }) {
                                                showNextTooltip = false
                                                Log.d("ArtSpace", "Next button released, showNextTooltip: $showNextTooltip")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        showNextTooltip = false
                                        Log.d("ArtSpace", "Next button released with exception, showNextTooltip: $showNextTooltip")
                                    }
                                }
                            )
                        }
                ) {
                    Text("Next")
                }

                if (showNextTooltip) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.9f))
                            .padding(12.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        Text(
                            text = "Show the next photo",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}