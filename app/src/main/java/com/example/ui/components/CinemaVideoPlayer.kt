package com.example.ui.components

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.api.oneembed.OneEmbedStreaming
import com.example.data.model.AudioTrackOption
import com.example.data.model.EpisodeItem
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.StreamQualityOption
import com.example.data.model.SubtitleTrackOption
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassSurface
import com.example.ui.viewmodel.CinemaPlayerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemaVideoPlayer(
    state: CinemaPlayerState,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onSkipForward: () -> Unit,
    onSkipBackward: () -> Unit,
    onToggleMute: () -> Unit,
    onSetVolume: (Float) -> Unit = {},
    onSetBrightness: (Float) -> Unit = {},
    onToggleFullscreen: () -> Unit = {},
    onSelectAudioTrack: (AudioTrackOption) -> Unit,
    onSelectSubtitleTrack: (SubtitleTrackOption) -> Unit,
    onSelectQuality: (StreamQualityOption) -> Unit,
    onSetPlaybackSpeed: (Float) -> Unit,
    onSetAspectRatio: (String) -> Unit,
    onToggleAudioModal: (Boolean) -> Unit,
    onToggleSubtitleModal: (Boolean) -> Unit,
    onToggleQualityModal: (Boolean) -> Unit,
    onToggleSpeedModal: (Boolean) -> Unit,
    onSelectStreamingEngine: (String) -> Unit = {},
    onSelectEpisode: (Int, Int) -> Unit = { _, _ -> },
    onToggleWatchlist: (MediaItem) -> Unit = {},
    onSelectSimilar: (MediaItem) -> Unit = {},
    similarItems: List<MediaItem> = emptyList(),
    isInWatchlist: Boolean = false
) {
    if (!state.isVisible || state.mediaItem == null) return

    val item = state.mediaItem
    val context = LocalContext.current
    val activity = context as? Activity
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager }

    var showControls by remember { mutableStateOf(true) }
    var doubleTapRippleSide by remember { mutableStateOf<String?>(null) } // "LEFT", "RIGHT"
    var gestureHudType by remember { mutableStateOf<String?>(null) } // "BRIGHTNESS", "VOLUME"
    var currentBrightness by remember { mutableFloatStateOf(state.brightness) }
    var currentVolume by remember { mutableFloatStateOf(state.volume) }
    var selectedSeasonTab by remember(item.id) { mutableIntStateOf(state.selectedSeason) }

    // Auto-hide controls timer in fullscreen or active playback
    LaunchedEffect(showControls, state.isPlaying) {
        if (showControls && state.isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    // Reset double tap ripple
    LaunchedEffect(doubleTapRippleSide) {
        if (doubleTapRippleSide != null) {
            delay(500)
            doubleTapRippleSide = null
        }
    }

    // Auto-hide gesture HUD after drag ends
    LaunchedEffect(gestureHudType) {
        if (gestureHudType != null) {
            delay(1500)
            gestureHudType = null
        }
    }

    // Manage screen orientation based on fullscreen state
    DisposableEffect(state.isFullscreen) {
        if (state.isFullscreen) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    // Ambient glow pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "player_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Dialog(
        onDismissRequest = {
            if (state.isFullscreen) {
                onToggleFullscreen()
            } else {
                onClose()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07090E))
                .testTag("cinema_player_container")
        ) {
            // Ambient Aura
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(glowAlpha)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                AccentViolet.copy(alpha = 0.22f),
                                AccentCyan.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    )
            )

            if (state.isFullscreen) {
                // ==========================================
                // FULLSCREEN MODE (LANDSCAPE IMMERSIVE)
                // ==========================================
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("cinema_fullscreen_viewport")
                ) {
                    // Video Viewport Surface
                    VideoSurface(
                        item = item,
                        season = state.selectedSeason,
                        episode = state.selectedEpisode,
                        aspectRatio = state.aspectRatio,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Touch Gestures Overlay (Tap, Double Tap Seek, Drag Brightness & Volume)
                    PlayerGestureOverlay(
                        onTap = { showControls = !showControls },
                        onDoubleTapLeft = {
                            doubleTapRippleSide = "LEFT"
                            onSkipBackward()
                        },
                        onDoubleTapRight = {
                            doubleTapRippleSide = "RIGHT"
                            onSkipForward()
                        },
                        onVerticalDragBrightness = { delta ->
                            val nextB = (currentBrightness + delta).coerceIn(0.05f, 1f)
                            currentBrightness = nextB
                            onSetBrightness(nextB)
                            activity?.window?.attributes = activity?.window?.attributes?.apply {
                                screenBrightness = nextB
                            }
                            gestureHudType = "BRIGHTNESS"
                        },
                        onVerticalDragVolume = { delta ->
                            val nextV = (currentVolume + delta).coerceIn(0f, 1f)
                            currentVolume = nextV
                            onSetVolume(nextV)
                            val max = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15
                            audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, (nextV * max).toInt(), 0)
                            gestureHudType = "VOLUME"
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Double Tap Ripple Animation
                    DoubleTapRippleOverlay(doubleTapRippleSide)

                    // Gesture HUD (Brightness / Volume)
                    GestureHudOverlay(
                        hudType = gestureHudType,
                        brightness = currentBrightness,
                        volume = currentVolume,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Controls Overlay
                    AnimatedVisibility(
                        visible = showControls,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        FullscreenControlsOverlay(
                            item = item,
                            state = state,
                            onClose = { onToggleFullscreen() },
                            onTogglePlayPause = onTogglePlayPause,
                            onSeek = onSeek,
                            onSkipForward = onSkipForward,
                            onSkipBackward = onSkipBackward,
                            onToggleFullscreen = onToggleFullscreen,
                            onToggleAudioModal = { onToggleAudioModal(true) },
                            onCycleAspectRatio = {
                                val nextRatio = when (state.aspectRatio) {
                                    "16:9" -> "21:9"
                                    "21:9" -> "Fit"
                                    "Fit" -> "Fill"
                                    else -> "16:9"
                                }
                                onSetAspectRatio(nextRatio)
                            },
                            onToggleSpeedModal = { onToggleSpeedModal(true) },
                            onToggleQualityModal = { onToggleQualityModal(true) }
                        )
                    }
                }
            } else {
                // ==========================================
                // PORTRAIT MODE (VIDEO AT TOP + DETAILS BELOW)
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("cinema_portrait_layout")
                ) {
                    // Top: 16:9 Cinema Viewport
                    val aspectModifier = when (state.aspectRatio) {
                        "21:9" -> Modifier.fillMaxWidth().aspectRatio(21f / 9f)
                        "Fit" -> Modifier.fillMaxWidth().height(240.dp)
                        "Fill" -> Modifier.fillMaxWidth().height(260.dp)
                        else -> Modifier.fillMaxWidth().aspectRatio(16f / 9f)
                    }

                    Box(
                        modifier = aspectModifier
                            .background(Color.Black)
                            .testTag("portrait_video_viewport")
                    ) {
                        // Video Surface
                        VideoSurface(
                            item = item,
                            season = state.selectedSeason,
                            episode = state.selectedEpisode,
                            aspectRatio = state.aspectRatio,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Touch Gestures Overlay
                        PlayerGestureOverlay(
                            onTap = { showControls = !showControls },
                            onDoubleTapLeft = {
                                doubleTapRippleSide = "LEFT"
                                onSkipBackward()
                            },
                            onDoubleTapRight = {
                                doubleTapRippleSide = "RIGHT"
                                onSkipForward()
                            },
                            onVerticalDragBrightness = { delta ->
                                val nextB = (currentBrightness + delta).coerceIn(0.05f, 1f)
                                currentBrightness = nextB
                                onSetBrightness(nextB)
                                activity?.window?.attributes = activity?.window?.attributes?.apply {
                                    screenBrightness = nextB
                                }
                                gestureHudType = "BRIGHTNESS"
                            },
                            onVerticalDragVolume = { delta ->
                                val nextV = (currentVolume + delta).coerceIn(0f, 1f)
                                currentVolume = nextV
                                onSetVolume(nextV)
                                val max = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15
                                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, (nextV * max).toInt(), 0)
                                gestureHudType = "VOLUME"
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Double Tap Ripple Animation
                        DoubleTapRippleOverlay(doubleTapRippleSide)

                        // Gesture HUD Overlay
                        GestureHudOverlay(
                            hudType = gestureHudType,
                            brightness = currentBrightness,
                            volume = currentVolume,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        // Controls Overlay
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showControls,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            PortraitControlsOverlay(
                                item = item,
                                state = state,
                                onClose = onClose,
                                onTogglePlayPause = onTogglePlayPause,
                                onSeek = onSeek,
                                onSkipForward = onSkipForward,
                                onSkipBackward = onSkipBackward,
                                onToggleFullscreen = onToggleFullscreen,
                                onToggleAudioModal = { onToggleAudioModal(true) },
                                onCycleAspectRatio = {
                                    val nextRatio = when (state.aspectRatio) {
                                        "16:9" -> "21:9"
                                        "21:9" -> "Fit"
                                        "Fit" -> "Fill"
                                        else -> "16:9"
                                    }
                                    onSetAspectRatio(nextRatio)
                                }
                            )
                        }
                    }

                    // Bottom: Details, Seasons, Episodes, Audio & More (Scrollable)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Title & Metadata
                        Text(
                            text = item.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.testTag("portrait_player_title")
                        )

                        // Episode Title if TV Show
                        if (item.mediaType == MediaType.TV_SHOW) {
                            val epTitles = listOf(
                                "Pilot: The Awakening",
                                "Threshold of Whispers",
                                "Shadows in Convergence",
                                "Fractured Memories",
                                "Point of No Return",
                                "The Hidden Frequency",
                                "The Final Reckoning",
                                "Horizon Reborn"
                            )
                            val currentEpTitle = epTitles.getOrElse(state.selectedEpisode - 1) { "Episode ${state.selectedEpisode}" }
                            Text(
                                text = "Season ${state.selectedSeason} • Episode ${state.selectedEpisode}: $currentEpTitle",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AccentCyan,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Badges Row (Clean, Professional - No TMDB ID or Source Leaks)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Match %
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AccentCyan.copy(alpha = 0.9f)
                            ) {
                                Text(
                                    text = "${item.matchScore}% Match",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                                )
                            }

                            // Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Color(0xFF1B202E), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = AccentAmber,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = item.rating.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Year
                            Text(
                                text = item.releaseYear.toString(),
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8)
                            )

                            // Age Rating
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF263043)
                            ) {
                                Text(
                                    text = item.ageRating,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }

                            // Quality
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AccentPurple.copy(alpha = 0.25f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AccentPurple)
                            ) {
                                Text(
                                    text = item.quality,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentPurple,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            // Audio format
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF1E293B)
                            ) {
                                Text(
                                    text = "Dolby Atmos",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFCBD5E1),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Actions (Vault, Download, Share)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isInWatchlist) AccentCyan.copy(alpha = 0.2f) else Color(0xFF1A1F2C),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isInWatchlist) AccentCyan else GlassBorderSubtle
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onToggleWatchlist(item) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Vault",
                                        tint = if (isInWatchlist) AccentCyan else Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isInWatchlist) "In Vault" else "+ Vault",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isInWatchlist) AccentCyan else Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF1A1F2C),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        // Trigger feedback
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Download",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Download 4K",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF1A1F2C),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .clickable {
                                        // Share
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Share",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Overview / Synopsis
                        Text(
                            text = item.overview,
                            fontSize = 13.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 19.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // TV Series Season & Episode Picker
                        val isSeries = item.mediaType == MediaType.TV_SHOW || item.mediaType == MediaType.ANIME
                        if (isSeries) {
                            Text(
                                text = "Seasons & Episodes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Season selector tabs
                            val availableSeasons = item.getAvailableSeasons()
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                availableSeasons.forEach { season ->
                                    val isSelected = selectedSeasonTab == season
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) AccentCyan else Color(0xFF1B202D),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) AccentCyan else GlassBorderSubtle
                                        ),
                                        modifier = Modifier.clickable { selectedSeasonTab = season }
                                    ) {
                                        Text(
                                            text = "Season $season",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else Color.White,
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            // Episode Cards List
                            val episodes = item.getEpisodesForSeason(selectedSeasonTab)
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp)
                            ) {
                                episodes.forEach { ep ->
                                    val isCurrentPlaying = state.selectedSeason == selectedSeasonTab && state.selectedEpisode == ep.episodeNumber
                                    EpisodeCardItem(
                                        episode = ep,
                                        isPlaying = isCurrentPlaying,
                                        onClick = {
                                            onSelectEpisode(selectedSeasonTab, ep.episodeNumber)
                                        }
                                    )
                                }
                            }
                        }

                        // Audio & Subtitles Quick Bar
                        Text(
                            text = "Audio & Subtitles",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF171B26),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
                                modifier = Modifier.clickable { onToggleAudioModal(true) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Audio",
                                        tint = AccentCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = state.selectedAudioTrack?.label ?: "English (Dolby Atmos 5.1)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF171B26),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
                                modifier = Modifier.clickable { onToggleSubtitleModal(true) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ClosedCaption,
                                        contentDescription = "Subtitles",
                                        tint = AccentPurple,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = state.selectedSubtitleTrack?.label ?: "English [CC]",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // More Like This Recommendations
                        if (similarItems.isNotEmpty()) {
                            Text(
                                text = "More Like This",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            LazyRow(
                                contentPadding = PaddingValues(bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(similarItems) { similar ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF151922),
                                        modifier = Modifier
                                            .width(115.dp)
                                            .clickable { onSelectSimilar(similar) }
                                    ) {
                                        Column {
                                            AsyncImage(
                                                model = similar.posterUrl,
                                                contentDescription = similar.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(160.dp)
                                                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                            )
                                            Text(
                                                text = similar.title,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Audio Selection Modal Bottom Sheet
            if (state.showAudioModal) {
                AudioTrackSelectionSheet(
                    selectedTrack = state.selectedAudioTrack,
                    options = item.audioTrackOptions,
                    onSelect = { onSelectAudioTrack(it) },
                    onDismiss = { onToggleAudioModal(false) }
                )
            }

            // Subtitles Selection Modal Bottom Sheet
            if (state.showSubtitleModal) {
                SubtitleTrackSelectionSheet(
                    selectedTrack = state.selectedSubtitleTrack,
                    options = item.subtitleTrackOptions,
                    onSelect = { onSelectSubtitleTrack(it) },
                    onDismiss = { onToggleSubtitleModal(false) }
                )
            }

            // Speed Selection Modal Bottom Sheet
            if (state.showSpeedModal) {
                SpeedSelectionSheet(
                    currentSpeed = state.playbackSpeed,
                    onSelect = { onSetPlaybackSpeed(it) },
                    onDismiss = { onToggleSpeedModal(false) }
                )
            }

            // Quality Selection Modal Bottom Sheet
            if (state.showQualityModal) {
                QualitySelectionSheet(
                    currentQuality = state.selectedQuality,
                    options = item.qualityOptions,
                    onSelect = { onSelectQuality(it) },
                    onDismiss = { onToggleQualityModal(false) }
                )
            }
        }
    }
}

/**
 * Clean Video Surface: Embedded responsive cinema iframe stream
 */
@Composable
private fun VideoSurface(
    item: MediaItem,
    season: Int,
    episode: Int,
    aspectRatio: String,
    modifier: Modifier = Modifier
) {
    val embedUrl = remember(item.id, season, episode) {
        OneEmbedStreaming.buildEmbedUrl(
            item = item,
            season = season,
            episode = episode,
            colorHex = "E50914",
            autoplay = true,
            autonext = true
        )
    }

    val htmlContent = remember(embedUrl) {
        """
        <!DOCTYPE html>
        <html style="margin:0;padding:0;width:100%;height:100%;background-color:#000;overflow:hidden;">
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
          <style>
            * { margin:0; padding:0; box-sizing:border-box; }
            body, html { width:100%; height:100%; background:#000000; overflow:hidden; }
            .video-container { position:relative; width:100%; height:100%; display:flex; justify-content:center; align-items:center; }
            iframe { border:none; width:100%; height:100%; position:absolute; top:0; left:0; }
          </style>
        </head>
        <body>
          <div class="video-container">
            <iframe 
              src="$embedUrl" 
              allowfullscreen 
              allow="autoplay; fullscreen; encrypted-media; picture-in-picture">
            </iframe>
          </div>
        </body>
        </html>
        """.trimIndent()
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                // Software layer fallback for headless/virtualized renderers
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    allowFileAccess = false
                    allowContentAccess = false
                    cacheMode = WebSettings.LOAD_DEFAULT
                }

                webViewClient = object : WebViewClient() {
                    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                        return true
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        return true
                    }
                }

                loadDataWithBaseURL("https://1embed.cc", htmlContent, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL("https://1embed.cc", htmlContent, "text/html", "UTF-8", null)
        },
        modifier = modifier
    )
}

/**
 * Touch Gesture Detector for Brightness, Volume, Double-Tap 10s Skip, and Tap
 */
@Composable
private fun PlayerGestureOverlay(
    onTap: () -> Unit,
    onDoubleTapLeft: () -> Unit,
    onDoubleTapRight: () -> Unit,
    onVerticalDragBrightness: (Float) -> Unit,
    onVerticalDragVolume: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() },
                    onDoubleTap = { offset ->
                        if (offset.x < size.width / 2) {
                            onDoubleTapLeft()
                        } else {
                            onDoubleTapRight()
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                var isLeft = false
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        isLeft = offset.x < size.width / 2
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        // Upwards drag is negative dragAmount -> increases level
                        val delta = -dragAmount / 320f
                        if (isLeft) {
                            onVerticalDragBrightness(delta)
                        } else {
                            onVerticalDragVolume(delta)
                        }
                    }
                )
            }
    )
}

/**
 * Gesture HUD Overlay (Pill for Brightness or Volume level)
 */
@Composable
private fun GestureHudOverlay(
    hudType: String?,
    brightness: Float,
    volume: Float,
    modifier: Modifier = Modifier
) {
    if (hudType == null) return

    val isBrightness = hudType == "BRIGHTNESS"
    val icon = if (isBrightness) Icons.Default.BrightnessMedium else (if (volume > 0.5f) Icons.Default.VolumeUp else if (volume > 0f) Icons.Default.VolumeDown else Icons.Default.VolumeMute)
    val percentage = ((if (isBrightness) brightness else volume) * 100).toInt()

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Black.copy(alpha = 0.8f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
        modifier = modifier.testTag("gesture_hud_overlay")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = hudType,
                tint = if (isBrightness) AccentAmber else AccentCyan,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBrightness) "Brightness $percentage%" else "Volume $percentage%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { (if (isBrightness) brightness else volume).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (isBrightness) AccentAmber else AccentCyan,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }
        }
    }
}

/**
 * Double Tap 10s Rewind/Forward Ripple Effect
 */
@Composable
private fun DoubleTapRippleOverlay(side: String?) {
    if (side == null) return

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.65f),
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
            modifier = Modifier
                .align(if (side == "LEFT") Alignment.CenterStart else Alignment.CenterEnd)
                .padding(horizontal = 40.dp)
                .size(72.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = if (side == "LEFT") Icons.Default.FastRewind else Icons.Default.FastForward,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = if (side == "LEFT") "-10s" else "+10s",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Portrait Video Controls Overlay
 */
@Composable
private fun PortraitControlsOverlay(
    item: MediaItem,
    state: CinemaPlayerState,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onSkipForward: () -> Unit,
    onSkipBackward: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onToggleAudioModal: () -> Unit,
    onCycleAspectRatio: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Aspect ratio
                IconButton(
                    onClick = onCycleAspectRatio,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AspectRatio,
                        contentDescription = "Aspect Ratio",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Audio & Subtitles
                IconButton(
                    onClick = onToggleAudioModal,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ClosedCaption,
                        contentDescription = "Subtitles",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Fullscreen
                IconButton(
                    onClick = onToggleFullscreen,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .testTag("portrait_fullscreen_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen",
                        tint = AccentCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Center Playback Controls
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSkipBackward,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.FastRewind,
                    contentDescription = "Rewind 10s",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = onTogglePlayPause,
                modifier = Modifier
                    .size(56.dp)
                    .background(AccentCyan, CircleShape)
                    .testTag("portrait_play_pause_button")
            ) {
                Icon(
                    imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isPlaying) "Pause" else "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = onSkipForward,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "Forward 10s",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Bottom Seek Bar & Timestamps
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatSeconds(state.currentPositionSec),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Slider(
                value = state.currentPositionSec,
                onValueChange = onSeek,
                valueRange = 0f..state.totalDurationSec.coerceAtLeast(1f),
                colors = SliderDefaults.colors(
                    thumbColor = AccentCyan,
                    activeTrackColor = AccentCyan,
                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )

            Text(
                text = formatSeconds(state.totalDurationSec),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

/**
 * Fullscreen (Landscape) Cinema Controls Overlay
 */
@Composable
private fun FullscreenControlsOverlay(
    item: MediaItem,
    state: CinemaPlayerState,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onSkipForward: () -> Unit,
    onSkipBackward: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onToggleAudioModal: () -> Unit,
    onCycleAspectRatio: () -> Unit,
    onToggleSpeedModal: () -> Unit,
    onToggleQualityModal: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .testTag("fullscreen_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Exit Fullscreen",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = item.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    if (item.mediaType == MediaType.TV_SHOW) {
                        Text(
                            text = "Season ${state.selectedSeason} • Episode ${state.selectedEpisode}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentCyan
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Aspect Ratio indicator/button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
                    modifier = Modifier.clickable { onCycleAspectRatio() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AspectRatio,
                            contentDescription = "Aspect Ratio",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = state.aspectRatio,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Audio & Subtitles
                IconButton(
                    onClick = onToggleAudioModal,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ClosedCaption,
                        contentDescription = "Audio & Subtitles",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Exit Fullscreen Button
                IconButton(
                    onClick = onToggleFullscreen,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        .testTag("fullscreen_exit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "Exit Fullscreen",
                        tint = AccentCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Center Controls
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSkipBackward,
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.FastRewind,
                    contentDescription = "Rewind 10s",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(
                onClick = onTogglePlayPause,
                modifier = Modifier
                    .size(68.dp)
                    .background(AccentCyan, CircleShape)
            ) {
                Icon(
                    imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isPlaying) "Pause" else "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(38.dp)
                )
            }

            IconButton(
                onClick = onSkipForward,
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "Forward 10s",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Bottom Controls Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Seek bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatSeconds(state.currentPositionSec),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Slider(
                    value = state.currentPositionSec,
                    onValueChange = onSeek,
                    valueRange = 0f..state.totalDurationSec.coerceAtLeast(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = AccentCyan,
                        activeTrackColor = AccentCyan,
                        inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                )

                Text(
                    text = formatSeconds(state.totalDurationSec),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8)
                )
            }

            // Shortcuts Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Speed
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle),
                        modifier = Modifier.clickable { onToggleSpeedModal() }
                    ) {
                        Text(
                            text = "${state.playbackSpeed}x Speed",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Quality
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AccentPurple.copy(alpha = 0.25f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentPurple),
                        modifier = Modifier.clickable { onToggleQualityModal() }
                    ) {
                        Text(
                            text = "4K Ultra HD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurple,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                // Volume & Fullscreen toggle
                IconButton(
                    onClick = onToggleFullscreen,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FullscreenExit,
                        contentDescription = "Exit Fullscreen",
                        tint = AccentCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

/**
 * Episode List Item Card in Portrait Details View
 */
@Composable
private fun EpisodeCardItem(
    episode: EpisodeItem,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isPlaying) AccentCyan.copy(alpha = 0.12f) else Color(0xFF141822),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPlaying) AccentCyan else GlassBorderSubtle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E2536)),
                contentAlignment = Alignment.Center
            ) {
                if (!episode.thumbnail.isNullOrEmpty()) {
                    AsyncImage(
                        model = episode.thumbnail,
                        contentDescription = episode.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Play icon badge
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(if (isPlaying) AccentCyan else Color.Black.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.GraphicEq else Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = if (isPlaying) Color.Black else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EP ${episode.episodeNumber}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPlaying) AccentCyan else Color(0xFF94A3B8)
                    )
                    Text(
                        text = episode.duration,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = episode.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = episode.overview,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Audio Track Selection Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AudioTrackSelectionSheet(
    selectedTrack: AudioTrackOption?,
    options: List<AudioTrackOption>,
    onSelect: (AudioTrackOption) -> Unit,
    onDismiss: () -> Unit
) {
    val defaultOptions = if (options.isNotEmpty()) options else listOf(
        AudioTrackOption("audio_en", "English", "English (Dolby Atmos 5.1)", "Dolby Atmos", "5.1 Surround", true),
        AudioTrackOption("audio_es", "Spanish", "Spanish (Dolby 5.1)", "Dolby Digital", "5.1 Surround"),
        AudioTrackOption("audio_fr", "French", "French (Dolby 5.1)", "Dolby Digital", "5.1 Surround"),
        AudioTrackOption("audio_ja", "Japanese", "Japanese (Original Atmos)", "Dolby Atmos", "5.1 Surround"),
        AudioTrackOption("audio_hi", "Hindi", "Hindi (Dolby 5.1)", "Dolby Digital", "5.1 Surround"),
        AudioTrackOption("audio_de", "German", "German (Stereo)", "Stereo", "2.0 Channel")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131722),
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Select Audio Track",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            defaultOptions.forEach { track ->
                val isSelected = selectedTrack?.id == track.id || (selectedTrack == null && track.isOriginal)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(track)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = track.label,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) AccentCyan else Color.White
                        )
                        Text(
                            text = "${track.audioFormat} • ${track.channelLayout}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Subtitle Track Selection Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubtitleTrackSelectionSheet(
    selectedTrack: SubtitleTrackOption?,
    options: List<SubtitleTrackOption>,
    onSelect: (SubtitleTrackOption) -> Unit,
    onDismiss: () -> Unit
) {
    val defaultOptions = if (options.isNotEmpty()) options else listOf(
        SubtitleTrackOption("sub_off", "Off", "Off", isOff = true),
        SubtitleTrackOption("sub_en", "English", "English [CC]"),
        SubtitleTrackOption("sub_es", "Spanish", "Spanish"),
        SubtitleTrackOption("sub_fr", "French", "French"),
        SubtitleTrackOption("sub_de", "German", "German"),
        SubtitleTrackOption("sub_ja", "Japanese", "Japanese"),
        SubtitleTrackOption("sub_ko", "Korean", "Korean"),
        SubtitleTrackOption("sub_hi", "Hindi", "Hindi")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131722),
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Subtitles",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            defaultOptions.forEach { track ->
                val isSelected = (selectedTrack?.id == track.id) || (selectedTrack == null && track.id == "sub_en")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(track)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) AccentCyan else Color.White
                    )

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Playback Speed Selection Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpeedSelectionSheet(
    currentSpeed: Float,
    onSelect: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131722),
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Playback Speed",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            speeds.forEach { speed ->
                val isSelected = currentSpeed == speed
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(speed)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (speed == 1.0f) "1.0x (Normal)" else "${speed}x",
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) AccentCyan else Color.White
                    )

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Quality Selection Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QualitySelectionSheet(
    currentQuality: StreamQualityOption?,
    options: List<StreamQualityOption>,
    onSelect: (StreamQualityOption) -> Unit,
    onDismiss: () -> Unit
) {
    val defaultOptions = if (options.isNotEmpty()) options else listOf(
        StreamQualityOption("4K Ultra HD", "2160p", "45 Mbps", ""),
        StreamQualityOption("1080p Full HD", "1080p", "15 Mbps", ""),
        StreamQualityOption("720p HD", "720p", "6 Mbps", ""),
        StreamQualityOption("Auto (Adaptive)", "Auto", "Dynamic", "")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF131722),
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Video Quality",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            defaultOptions.forEach { q ->
                val isSelected = currentQuality?.label == q.label || (currentQuality == null && q.label.contains("4K"))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(q)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = q.label,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) AccentCyan else Color.White
                        )
                        Text(
                            text = "${q.resolution} • ${q.bitrate}",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun formatSeconds(seconds: Float): String {
    val totalSecs = seconds.toInt().coerceAtLeast(0)
    val mins = totalSecs / 60
    val secs = totalSecs % 60
    return String.format("%02d:%02d", mins, secs)
}
