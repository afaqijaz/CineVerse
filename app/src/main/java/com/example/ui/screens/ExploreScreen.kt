package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SortOption
import com.example.ui.components.MediaPosterCard
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.viewmodel.MediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: MediaViewModel,
    modifier: Modifier = Modifier
) {
    val repository = viewModel.repository
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    val vibePrompts = listOf(
        "Tokyo Cyberpunk",
        "Seoul Rain Romance",
        "Madrid Royal Heist",
        "Black Forest Time Loop",
        "Lagos Syndicate",
        "Scandinavian Ice Mystery"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("explore_screen")
    ) {
        // Search & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D0F12))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Search Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "Search titles, actors, directors, vibes...",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AccentCyan
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF161A24),
                        unfocusedContainerColor = Color(0xFF161A24),
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = Color(0xFF283042),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("explore_search_input")
                )

                // Filter Sheet Toggle Button
                IconButton(
                    onClick = { showFilterSheet = true },
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF161A24), RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            if (filterState.selectedCountry != null || filterState.selectedLanguage != null || filterState.selectedPlatform != null || filterState.minRating > 0f) AccentCyan else Color(0xFF283042),
                            RoundedCornerShape(14.dp)
                        )
                        .testTag("explore_filter_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filters",
                        tint = if (filterState.selectedCountry != null || filterState.minRating > 0f) AccentCyan else Color.White
                    )
                }
            }

            // Vibe Prompts Quick Pills
            AnimatedVisibility(visible = searchQuery.isEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(vibePrompts) { vibe ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF1A1F2C),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2B3648)),
                            modifier = Modifier.clickable { viewModel.setSearchQuery(vibe) }
                        ) {
                            Text(
                                text = "✨ $vibe",
                                fontSize = 11.sp,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Country of Origin Filter Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF12151D))
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                val isAllSelected = filterState.selectedCountry == null
                FilterChip(
                    selected = isAllSelected,
                    onClick = { viewModel.updateFilter { it.copy(selectedCountry = null) } },
                    label = { Text("🌎 All Countries") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF1C222F),
                        labelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isAllSelected,
                        borderColor = if (isAllSelected) AccentCyan else Color(0xFF2D374A)
                    )
                )
            }

            items(repository.globalCountries) { country ->
                val isSelected = filterState.selectedCountry == country.id
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        viewModel.updateFilter {
                            it.copy(selectedCountry = if (isSelected) null else country.id)
                        }
                    },
                    label = { Text("${country.flag} ${country.id}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF1C222F),
                        labelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) AccentCyan else Color(0xFF2D374A)
                    )
                )
            }
        }

        // Secondary Info Bar: Result count & Sort Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${results.size} Titles Found",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF94A3B8)
            )

            // Sort Dropdown Button
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1A1F2C))
                        .clickable { showSortMenu = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort",
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = filterState.sortBy.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false },
                    modifier = Modifier.background(Color(0xFF1E2433))
                ) {
                    SortOption.values().forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option.label,
                                    color = if (filterState.sortBy == option) AccentCyan else Color.White,
                                    fontWeight = if (filterState.sortBy == option) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                showSortMenu = false
                                viewModel.updateFilter { it.copy(sortBy = option) }
                            }
                        )
                    }
                }
            }
        }

        // Active Filter Chips Indicator (if any active)
        if (filterState.selectedLanguage != null || filterState.selectedPlatform != null || filterState.minRating > 0f) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (filterState.selectedLanguage != null) {
                    ActiveFilterTag(
                        text = "Lang: ${filterState.selectedLanguage}",
                        onRemove = { viewModel.updateFilter { it.copy(selectedLanguage = null) } }
                    )
                }
                if (filterState.selectedPlatform != null) {
                    ActiveFilterTag(
                        text = "Platform: ${filterState.selectedPlatform}",
                        onRemove = { viewModel.updateFilter { it.copy(selectedPlatform = null) } }
                    )
                }
                if (filterState.minRating > 0f) {
                    ActiveFilterTag(
                        text = "Rating: ≥ ${filterState.minRating}",
                        onRemove = { viewModel.updateFilter { it.copy(minRating = 0f) } }
                    )
                }
            }
        }

        // Search Results Grid
        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No media found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Try adjusting your country, language, or rating filters",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.resetFilters() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset All Filters", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(results) { item ->
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

    // Filter Bottom Sheet
    if (showFilterSheet) {
        val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = filterSheetState,
            containerColor = Color(0xFF131722),
            modifier = Modifier.testTag("deep_filter_sheet")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Global Discovery Filters",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = { showFilterSheet = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 1. Language Filter
                Text(
                    text = "Audio & Original Language",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentCyan
                )

                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(repository.globalLanguages) { lang ->
                        val isSelected = filterState.selectedLanguage == lang
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.updateFilter {
                                    it.copy(selectedLanguage = if (isSelected) null else lang)
                                }
                            },
                            label = { Text(lang) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AccentCyan,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF1E2536),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Streaming Platform Availability
                Text(
                    text = "Streaming Platform Availability",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentCyan
                )

                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(repository.allPlatforms) { platform ->
                        val isSelected = filterState.selectedPlatform == platform.name
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.updateFilter {
                                    it.copy(selectedPlatform = if (isSelected) null else platform.name)
                                }
                            },
                            label = { Text(platform.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(platform.hexColor),
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFF1E2536),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Minimum IMDb Rating Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Minimum IMDb Score",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentCyan
                    )
                    Text(
                        text = if (filterState.minRating > 0f) "≥ ${String.format("%.1f", filterState.minRating)} ⭐" else "Any Rating",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentAmber
                    )
                }

                Slider(
                    value = filterState.minRating,
                    onValueChange = { rating -> viewModel.updateFilter { it.copy(minRating = rating) } },
                    valueRange = 0f..9.5f,
                    steps = 18,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentCyan,
                        activeTrackColor = AccentCyan,
                        inactiveTrackColor = Color(0xFF283244)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Apply / Reset Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.resetFilters() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF283244),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear All")
                    }

                    Button(
                        onClick = { showFilterSheet = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply Filters", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ActiveFilterTag(
    text: String,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AccentCyan.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.5f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(text = text, fontSize = 11.sp, color = AccentCyan)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = AccentCyan,
                modifier = Modifier
                    .size(14.dp)
                    .clickable(onClick = onRemove)
            )
        }
    }
}
