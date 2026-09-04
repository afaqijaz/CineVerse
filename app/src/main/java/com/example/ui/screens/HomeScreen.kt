package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.repository.CountryMeta
import com.example.data.repository.GenreMeta
import com.example.ui.components.HeroBannerCarousel
import com.example.ui.components.MediaBackdropCard
import com.example.ui.components.MediaPosterCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.GlassSurfaceTranslucent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun HomeScreen(
    viewModel: MediaViewModel,
    onNavigateTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val repository = viewModel.repository
    val liveApiItems by viewModel.liveApiItems.collectAsStateWithLifecycle()
    val isApiLoading by viewModel.isLiveApiLoading.collectAsStateWithLifecycle()

    val heroItems = remember(liveApiItems) { repository.getHeroItems() }
    val top10Items = remember(liveApiItems) { repository.getTop10Items() }
    val kdramaItems = remember(liveApiItems) { repository.getItemsByCountry("South Korea") }
    val indianItems = remember(liveApiItems) { repository.getItemsByCountry("India") }
    val animeItems = remember(liveApiItems) { repository.getItemsByType(MediaType.ANIME) }
    val sciFiItems = remember(liveApiItems) { repository.getItemsByGenre("Sci-Fi") }
    val tvSeries = remember(liveApiItems) { repository.getItemsByType(MediaType.TV_SHOW) }

    val categories = listOf("All", "Movies", "TV Shows", "Anime", "Documentary")
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen_feed"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Frosted Glass Sticky Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xEB0D0F12),
                                Color(0xCC0D0F12)
                            )
                        )
                    )
                    .border(
                        androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Brush.verticalGradient(
                                listOf(GlassBorder, Color.Transparent)
                            )
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo & Tagline matching Frosted Glass design
                    Column {
                        Text(
                            text = "GLOBAL DISCOVERY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentCyan,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CineVerse",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }

                    // Region Selector & Search frosted buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = GlassSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                            modifier = Modifier.clickable { onNavigateTab(AppTab.EXPLORE) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = "Global Catalog",
                                    tint = AccentCyan,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "50+ Countries",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        IconButton(
                            onClick = { onNavigateTab(AppTab.EXPLORE) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GlassSurface)
                                .border(1.dp, GlassBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }
        }

        // Frosted Glass Category Filter Pills
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(categories) { index, title ->
                    val isSelected = selectedCategoryIndex == index
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = if (isSelected) AccentViolet else GlassSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Color.White.copy(alpha = 0.25f) else GlassBorderSubtle
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable {
                                selectedCategoryIndex = index
                                val categoryFilter = when (index) {
                                    1 -> "MOVIE"
                                    2 -> "TV_SHOW"
                                    3 -> "ANIME"
                                    4 -> "DOCUMENTARY"
                                    else -> "ALL"
                                }
                                viewModel.updateFilter { it.copy(selectedCategory = categoryFilter) }
                            }
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Hero Auto-Sliding Showcase Banner
        item {
            HeroBannerCarousel(
                heroItems = heroItems,
                onPlayTrailer = { viewModel.playMedia(it, isFullMovie = true) },
                onToggleWatchlist = { viewModel.toggleWatchlist(it) },
                onOpenDetails = { viewModel.selectMedia(it) },
                isInWatchlist = { id -> viewModel.watchlistItems.value.any { it.id == id } }
            )
        }

        // Section: Live Public Domain & Multi-Audio 4K Streams
        item {
            val liveItems by viewModel.liveApiItems.collectAsStateWithLifecycle()
            val streamList = if (liveItems.isNotEmpty()) liveItems else heroItems

            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Live Streaming Movies & Multi-Audio",
                emoji = "🎬",
                actionText = "Live Feed",
                onActionClick = {
                    viewModel.updateFilter { it.copy(selectedCategory = "ALL") }
                    onNavigateTab(AppTab.EXPLORE)
                }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(streamList) { item ->
                    MediaBackdropCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) }
                    )
                }
            }
        }

        // Section 1: Trending Worldwide Today (Top 10 Ranked)
        item {
            SectionHeader(
                title = "Trending Worldwide Today",
                emoji = "🔥",
                actionText = "See All",
                onActionClick = { onNavigateTab(AppTab.EXPLORE) }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(top10Items) { index, item ->
                    MediaPosterCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) },
                        showRank = index + 1
                    )
                }
            }
        }

        // Section 2: Browse by Language Carousel
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Browse by Language",
                emoji = "🌐",
                actionText = "Explore All",
                onActionClick = { onNavigateTab(AppTab.EXPLORE) }
            )

            val languageItems = listOf(
                Triple("English", "🇺🇸", "Hollywood & Global"),
                Triple("Korean", "🇰🇷", "K-Dramas & Thrillers"),
                Triple("Hindi", "🇮🇳", "Bollywood & Pan-India"),
                Triple("Japanese", "🇯🇵", "Anime & Classic Cinema"),
                Triple("Spanish", "🇪🇸", "Spain & Latin America"),
                Triple("French", "🇫🇷", "Arthouse & Crime Noir"),
                Triple("German", "🇩🇪", "Sci-Fi & High Mystery"),
                Triple("Italian", "🇮🇹", "Neorealism & Drama"),
                Triple("Portuguese", "🇧🇷", "Brazil & Portugal"),
                Triple("Turkish", "🇹🇷", "Dizi Dramas & Romance")
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(languageItems) { (lang, flag, subtitle) ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GlassSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                        modifier = Modifier
                            .width(155.dp)
                            .height(84.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.updateFilter { it.copy(selectedLanguage = lang) }
                                onNavigateTab(AppTab.EXPLORE)
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = flag, fontSize = 22.sp)
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = AccentCyan.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Audio",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentCyan,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = lang,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = subtitle,
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Browse by Country Carousel
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Explore by Country & Region",
                emoji = "🌍",
                actionText = "50+ Countries",
                onActionClick = { onNavigateTab(AppTab.EXPLORE) }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(repository.globalCountries) { country ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GlassSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                        modifier = Modifier
                            .width(140.dp)
                            .height(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.updateFilter { it.copy(selectedCountry = country.id) }
                                onNavigateTab(AppTab.EXPLORE)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = country.flag, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = country.id,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = country.primaryLang,
                                    fontSize = 10.sp,
                                    color = AccentCyan,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Browse Genres Carousel
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Explore Genres & Themes",
                emoji = "✨",
                actionText = "All Genres",
                onActionClick = { onNavigateTab(AppTab.EXPLORE) }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(repository.genreList) { genre ->
                    val color = Color(genre.colorHex)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = color.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .width(135.dp)
                            .height(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.updateFilter { it.copy(selectedGenre = genre.name) }
                                onNavigateTab(AppTab.EXPLORE)
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            color.copy(alpha = 0.35f),
                                            Color(0xFF0F121A)
                                        )
                                    )
                                )
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.CenterStart)
                            ) {
                                Text(text = genre.iconEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = genre.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = genre.subGenres.firstOrNull() ?: "Cinema",
                                        fontSize = 9.sp,
                                        color = color,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: K-Drama & East Asian Wonders (Widescreen Backdrops)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "K-Drama & East Asian Cinema",
                emoji = "🇰🇷",
                onActionClick = {
                    viewModel.updateFilter { it.copy(selectedCountry = "South Korea") }
                    onNavigateTab(AppTab.EXPLORE)
                }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(kdramaItems) { item ->
                    MediaBackdropCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) }
                    )
                }
            }
        }

        // Section 3: Bollywood & Pan-Indian Masterpieces
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Bollywood & Pan-Indian Epics",
                emoji = "🇮🇳",
                onActionClick = {
                    viewModel.updateFilter { it.copy(selectedCountry = "India") }
                    onNavigateTab(AppTab.EXPLORE)
                }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(indianItems) { item ->
                    MediaPosterCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) }
                    )
                }
            }
        }

        // Section 4: Cyberpunk & Sci-Fi Visions
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Cyberpunk & High-Concept Sci-Fi",
                emoji = "🚀",
                onActionClick = {
                    viewModel.updateFilter { it.copy(selectedGenre = "Sci-Fi") }
                    onNavigateTab(AppTab.EXPLORE)
                }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(sciFiItems) { item ->
                    MediaBackdropCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) }
                    )
                }
            }
        }

        // Section 5: Binge-Worthy TV Series
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Binge-Worthy Global TV Series",
                emoji = "🍿",
                onActionClick = {
                    viewModel.updateFilter { it.copy(selectedCategory = "TV_SHOW") }
                    onNavigateTab(AppTab.EXPLORE)
                }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tvSeries) { item ->
                    MediaPosterCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) }
                    )
                }
            }
        }

        // Section 6: Anime Legends
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(
                title = "Anime Legends & Shonen",
                emoji = "🌸",
                onActionClick = {
                    viewModel.updateFilter { it.copy(selectedCategory = "ANIME") }
                    onNavigateTab(AppTab.EXPLORE)
                }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animeItems) { item ->
                    MediaPosterCard(
                        item = item,
                        onClick = { viewModel.selectMedia(item) }
                    )
                }
            }
        }
    }
}
