package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.GenreMeta
import com.example.ui.components.MediaPosterCard
import com.example.ui.theme.AccentCyan
import com.example.ui.viewmodel.MediaViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenresScreen(
    viewModel: MediaViewModel,
    modifier: Modifier = Modifier
) {
    val repository = viewModel.repository
    var selectedGenre by remember { mutableStateOf<GenreMeta?>(null) }
    var selectedSubGenre by remember { mutableStateOf<String?>(null) }

    val activeGenreMedia = remember(selectedGenre, selectedSubGenre) {
        if (selectedGenre == null) emptyList()
        else {
            val allInGenre = repository.getItemsByGenre(selectedGenre!!.name)
            if (selectedSubGenre == null) allInGenre
            else allInGenre.filter { it.subGenres.contains(selectedSubGenre) }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("genres_screen")
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D0F12))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (selectedGenre == null) "Genres & Categories" else "${selectedGenre!!.iconEmoji} ${selectedGenre!!.name}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = if (selectedGenre == null) "Explore cinema by thematic and narrative universe" else "${activeGenreMedia.size} titles in this genre",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                if (selectedGenre != null) {
                    IconButton(
                        onClick = {
                            selectedGenre = null
                            selectedSubGenre = null
                        },
                        modifier = Modifier.background(Color(0xFF1E2536), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Back to All Genres",
                            tint = Color.White
                        )
                    }
                }
            }

            // Sub-Genre Tag Cloud if Genre Selected
            AnimatedVisibility(visible = selectedGenre != null) {
                if (selectedGenre != null) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedSubGenre == null,
                            onClick = { selectedSubGenre = null },
                            label = { Text("All ${selectedGenre!!.name}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentCyan,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF1E2536),
                                labelColor = Color.White
                            )
                        )

                        selectedGenre!!.subGenres.forEach { sub ->
                            val isSelected = selectedSubGenre == sub
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedSubGenre = if (isSelected) null else sub },
                                label = { Text("#$sub") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(selectedGenre!!.colorHex),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFF1E2536),
                                    labelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Body Content: Either Genre Cards Grid OR Selected Genre Media Grid
        if (selectedGenre == null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(repository.genreList) { genre ->
                    GenreCard(
                        genre = genre,
                        onClick = {
                            selectedGenre = genre
                            selectedSubGenre = null
                        }
                    )
                }
            }
        } else {
            // Selected Genre Media Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(activeGenreMedia) { item ->
                    MediaPosterCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) },
                        width = 135,
                        height = 200
                    )
                }
            }
        }
    }
}

@Composable
private fun GenreCard(
    genre: GenreMeta,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(genre.colorHex).copy(alpha = 0.25f),
                        Color(0xFF181C26)
                    )
                )
            )
            .border(1.dp, Color(genre.colorHex).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = genre.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = genre.iconEmoji,
                    fontSize = 24.sp
                )
            }

            Text(
                text = genre.subGenres.take(2).joinToString(" • "),
                fontSize = 11.sp,
                color = Color(0xFFCBD5E1)
            )
        }
    }
}
