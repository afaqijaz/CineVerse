package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AudioTrackOption
import com.example.data.model.StreamQualityOption
import com.example.data.model.SubtitleTrackOption
import com.example.ui.viewmodel.CinemaPlayerState
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun TrailerPlayerModal(
    state: CinemaPlayerState,
    viewModel: MediaViewModel
) {
    if (!state.isVisible || state.mediaItem == null) return

    val watchlistItems by viewModel.watchlistItems.collectAsStateWithLifecycle()
    val isSaved = watchlistItems.any { it.id == state.mediaItem.id }
    val similarItems = remember(state.mediaItem.id) {
        viewModel.repository.getSimilarMedia(state.mediaItem)
    }

    CinemaVideoPlayer(
        state = state,
        onClose = { viewModel.closePlayer() },
        onTogglePlayPause = { viewModel.togglePlayPause() },
        onSeek = { viewModel.seekTo(it) },
        onSkipForward = { viewModel.skipForward10() },
        onSkipBackward = { viewModel.skipBackward10() },
        onToggleMute = { viewModel.toggleMute() },
        onSetVolume = { viewModel.setVolume(it) },
        onSetBrightness = { viewModel.setBrightness(it) },
        onToggleFullscreen = { viewModel.toggleFullscreen() },
        onSelectAudioTrack = { viewModel.selectAudioTrack(it) },
        onSelectSubtitleTrack = { viewModel.selectSubtitleTrack(it) },
        onSelectQuality = { viewModel.selectQuality(it) },
        onSetPlaybackSpeed = { viewModel.setPlaybackSpeed(it) },
        onSetAspectRatio = { viewModel.setAspectRatio(it) },
        onToggleAudioModal = { viewModel.toggleAudioModal(it) },
        onToggleSubtitleModal = { viewModel.toggleSubtitleModal(it) },
        onToggleQualityModal = { viewModel.toggleQualityModal(it) },
        onToggleSpeedModal = { viewModel.toggleSpeedModal(it) },
        onSelectStreamingEngine = { viewModel.setStreamingEngine(it) },
        onSelectEpisode = { season, ep -> viewModel.setEpisode(season, ep) },
        onToggleWatchlist = { item -> viewModel.toggleWatchlist(item) },
        onSelectSimilar = { item -> viewModel.playMedia(item, isFullMovie = true) },
        similarItems = similarItems,
        isInWatchlist = isSaved
    )
}

// Overload for legacy callers
@Composable
fun TrailerPlayerModal(
    state: CinemaPlayerState,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onToggleMute: () -> Unit,
    onSelectQuality: (String) -> Unit
) {
    if (!state.isVisible || state.mediaItem == null) return

    CinemaVideoPlayer(
        state = state,
        onClose = onClose,
        onTogglePlayPause = onTogglePlayPause,
        onSeek = onSeek,
        onSkipForward = { onSeek((state.currentPositionSec + 10f).coerceAtMost(state.totalDurationSec)) },
        onSkipBackward = { onSeek((state.currentPositionSec - 10f).coerceAtLeast(0f)) },
        onToggleMute = onToggleMute,
        onSelectAudioTrack = { /* handled in full modal */ },
        onSelectSubtitleTrack = { /* handled in full modal */ },
        onSelectQuality = { onSelectQuality(it.label) },
        onSetPlaybackSpeed = { /* speed */ },
        onSetAspectRatio = { /* aspect */ },
        onToggleAudioModal = { },
        onToggleSubtitleModal = { },
        onToggleQualityModal = { },
        onToggleSpeedModal = { }
    )
}
