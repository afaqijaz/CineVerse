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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.WatchlistEntity
import com.example.data.model.WatchlistStatus
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun WatchlistScreen(
    viewModel: MediaViewModel,
    onNavigateTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val allItems by viewModel.watchlistItems.collectAsState()
    val downloadedItems by viewModel.downloadedItems.collectAsState()
    val downloadingProgress by viewModel.downloadingProgress.collectAsState()

    val tabs = listOf("All Items", "Plan to Watch", "Watching", "Completed", "Offline Vault")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val filteredItems = when (selectedTabIndex) {
        1 -> allItems.filter { it.status == "PLAN_TO_WATCH" }
        2 -> allItems.filter { it.status == "WATCHING" }
        3 -> allItems.filter { it.status == "COMPLETED" }
        4 -> allItems.filter { it.isDownloaded }
        else -> allItems
    }

    var noteItemToEdit by remember { mutableStateOf<WatchlistEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("watchlist_vault_screen")
    ) {
        // Vault Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D0F12))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Cinema Vault",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "${allItems.size} Saved Titles • ${downloadedItems.size} Offline Packages",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                // Storage indicator pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF19202C),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C384E))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = "Storage",
                            tint = AccentCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${downloadedItems.size * 1.45} GB / 64 GB",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Vault Status Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = Color(0xFF121620),
            contentColor = AccentCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = AccentCyan,
                    height = 3.dp
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) AccentCyan else Color(0xFF94A3B8)
                        )
                    }
                )
            }
        }

        // Vault List Content
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔖", fontSize = 52.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (selectedTabIndex == 4) "No Offline Downloads Yet" else "Your Vault is Empty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (selectedTabIndex == 4)
                            "Download movies or TV episodes to watch in airplane mode without internet"
                        else
                            "Save movies and TV series across 50+ countries to track your journey",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(top = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { onNavigateTab(AppTab.EXPLORE) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Explore Global Movies", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    val dlProgress = downloadingProgress[item.id]
                    WatchlistItemCard(
                        item = item,
                        downloadProgress = dlProgress,
                        onOpen = {
                            val media = viewModel.repository.getMediaById(item.id)
                            if (media != null) viewModel.selectMedia(media)
                        },
                        onPlayTrailer = {
                            val media = viewModel.repository.getMediaById(item.id)
                            if (media != null) viewModel.openTrailer(media)
                        },
                        onChangeStatus = { newStatus ->
                            viewModel.updateWatchlistStatus(item.id, newStatus)
                        },
                        onToggleDownload = {
                            if (item.isDownloaded) {
                                viewModel.removeDownloaded(item.id)
                            } else {
                                val media = viewModel.repository.getMediaById(item.id)
                                if (media != null) viewModel.simulateDownload(media)
                            }
                        },
                        onRemove = { viewModel.removeFromWatchlist(item.id) },
                        onEditNotes = { noteItemToEdit = item }
                    )
                }
            }
        }
    }

    // Edit Notes & Rating Dialog
    if (noteItemToEdit != null) {
        val target = noteItemToEdit!!
        var noteText by remember { mutableStateOf(target.userNotes) }
        var ratingVal by remember { mutableStateOf(target.userPersonalRating) }

        AlertDialog(
            onDismissRequest = { noteItemToEdit = null },
            title = {
                Text(
                    text = "Personal Review: ${target.title}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column {
                    Text(
                        text = "Your Rating:",
                        fontSize = 13.sp,
                        color = Color(0xFFCBD5E1)
                    )
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star $star",
                                tint = if (ratingVal >= star) AccentAmber else Color(0xFF475569),
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { ratingVal = star.toFloat() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Add personal thoughts, episode notes, or quotes...", fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF141822),
                            unfocusedContainerColor = Color(0xFF141822),
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = Color(0xFF2E384C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserFeedback(target.id, ratingVal, noteText)
                        noteItemToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan, contentColor = Color.Black)
                ) {
                    Text("Save Review", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { noteItemToEdit = null }) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            },
            containerColor = Color(0xFF181C28)
        )
    }
}

@Composable
private fun WatchlistItemCard(
    item: WatchlistEntity,
    downloadProgress: Int?,
    onOpen: () -> Unit,
    onPlayTrailer: () -> Unit,
    onChangeStatus: (String) -> Unit,
    onToggleDownload: () -> Unit,
    onRemove: () -> Unit,
    onEditNotes: () -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF151922),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
            .testTag("watchlist_item_${item.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Poster
                Box(
                    modifier = Modifier
                        .size(width = 65.dp, height = 95.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF202636))
                ) {
                    AsyncImage(
                        model = item.posterUrl,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Play overlay button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(AccentCyan.copy(alpha = 0.85f))
                            .clickable(onClick = onPlayTrailer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info column
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = onRemove,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(text = item.countryFlag, fontSize = 12.sp)
                        Text(
                            text = "${item.country} • ${item.releaseYear}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(text = "•", fontSize = 11.sp, color = Color(0xFF64748B))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${item.rating}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Interactive Status Picker Pill
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            val currentStatusEnum = WatchlistStatus.values().find { it.name == item.status } ?: WatchlistStatus.PLAN_TO_WATCH
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(currentStatusEnum.badgeColor).copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(currentStatusEnum.badgeColor).copy(alpha = 0.6f)),
                                modifier = Modifier.clickable { showStatusMenu = true }
                            ) {
                                Text(
                                    text = "${currentStatusEnum.label} ▾",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(currentStatusEnum.badgeColor),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showStatusMenu,
                                onDismissRequest = { showStatusMenu = false },
                                modifier = Modifier.background(Color(0xFF1E2433))
                            ) {
                                WatchlistStatus.values().forEach { st ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = st.label,
                                                color = Color(st.badgeColor),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        },
                                        onClick = {
                                            showStatusMenu = false
                                            onChangeStatus(st.name)
                                        }
                                    )
                                }
                            }
                        }

                        // Download Offline Simulator Button
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onToggleDownload,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                    contentDescription = "Offline Sync",
                                    tint = if (item.isDownloaded) AccentEmerald else Color(0xFF94A3B8),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onEditNotes,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Review",
                                    tint = if (item.userNotes.isNotEmpty() || item.userPersonalRating > 0f) AccentAmber else Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Download Progress Bar
            AnimatedVisibility(visible = downloadProgress != null) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Downloading offline stream... ${downloadProgress ?: 0}%",
                        fontSize = 10.sp,
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    LinearProgressIndicator(
                        progress = { (downloadProgress ?: 0) / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = AccentCyan,
                        trackColor = Color(0xFF283244)
                    )
                }
            }

            // User Personal Note if present
            if (item.userNotes.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1B212D),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "💭 \"${item.userNotes}\"",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier.padding(8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
