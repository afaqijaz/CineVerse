package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MediaDetailModal
import com.example.ui.components.TrailerPlayerModal
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.GenresScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MediaViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: MediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settings.collectAsState()

            MyApplicationTheme(themeMode = settings.themeMode) {
                CineVerseApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CineVerseApp(viewModel: MediaViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedMedia by viewModel.selectedMedia.collectAsState()
    val trailerState by viewModel.trailerState.collectAsState()
    val watchlistItems by viewModel.watchlistItems.collectAsState()
    val downloadingProgress by viewModel.downloadingProgress.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Listen for toast notifications
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            CineVerseBottomNav(
                currentTab = currentTab,
                watchlistCount = watchlistItems.size,
                onTabSelect = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen Content Switcher
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_content_transition"
            ) { tab ->
                when (tab) {
                    AppTab.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateTab = { viewModel.setTab(it) }
                    )
                    AppTab.EXPLORE -> ExploreScreen(
                        viewModel = viewModel
                    )
                    AppTab.GENRES -> GenresScreen(
                        viewModel = viewModel
                    )
                    AppTab.WATCHLIST -> WatchlistScreen(
                        viewModel = viewModel,
                        onNavigateTab = { viewModel.setTab(it) }
                    )
                    AppTab.SETTINGS -> SettingsScreen(
                        viewModel = viewModel
                    )
                }
            }

            // Media Detail Modal (Bottom Sheet)
            if (selectedMedia != null) {
                val currentItem = selectedMedia!!
                val isSaved = watchlistItems.any { it.id == currentItem.id }
                val savedEntity = watchlistItems.find { it.id == currentItem.id }
                val dlProgress = downloadingProgress[currentItem.id]
                val similarItems = remember(currentItem) {
                    viewModel.repository.getSimilarMedia(currentItem)
                }

                MediaDetailModal(
                    item = currentItem,
                    onDismiss = { viewModel.selectMedia(null) },
                    onPlayTrailer = { viewModel.openTrailer(it) },
                    onPlayFullMovie = { viewModel.playMedia(it, isFullMovie = true) },
                    onPlay1Embed = { item, season, ep -> viewModel.play1Embed(item, season, ep) },
                    onToggleWatchlist = { item, status ->
                        if (status == "REMOVE") {
                            viewModel.removeFromWatchlist(item.id)
                        } else {
                            viewModel.toggleWatchlist(item, status)
                        }
                    },
                    onSimulateDownload = { viewModel.simulateDownload(it) },
                    onSelectSimilar = { viewModel.selectMedia(it) },
                    similarItems = similarItems,
                    isInWatchlist = isSaved,
                    currentWatchlistStatus = savedEntity?.status,
                    downloadProgress = dlProgress,
                    isDownloaded = savedEntity?.isDownloaded == true
                )
            }

            // High-End Cinema & Trailer Player
            TrailerPlayerModal(
                state = trailerState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CineVerseBottomNav(
    currentTab: AppTab,
    watchlistCount: Int,
    onTabSelect: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xF21A1D24),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.height(72.dp)
        ) {
            AppTab.values().forEach { tab ->
                val isSelected = currentTab == tab
                val iconVector: ImageVector = when (tab) {
                    AppTab.HOME -> Icons.Default.Home
                    AppTab.EXPLORE -> Icons.Default.Explore
                    AppTab.GENRES -> Icons.Default.Category
                    AppTab.WATCHLIST -> Icons.Default.Bookmark
                    AppTab.SETTINGS -> Icons.Default.Settings
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelect(tab) },
                    icon = {
                        if (tab == AppTab.WATCHLIST && watchlistCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = AccentViolet,
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = watchlistCount.toString(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color(0xFF919191),
                        unselectedTextColor = Color(0xFF919191),
                        indicatorColor = AccentViolet.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                )
            }
        }
    }
}
