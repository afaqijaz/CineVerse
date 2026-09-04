package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CineVerseDatabase
import com.example.data.local.WatchlistEntity
import com.example.data.model.AudioTrackOption
import com.example.data.model.FilterState
import com.example.data.model.MediaItem
import com.example.data.model.SortOption
import com.example.data.model.StreamQualityOption
import com.example.data.model.SubtitleTrackOption
import com.example.data.repository.MediaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val title: String, val iconName: String) {
    HOME("Home", "home"),
    EXPLORE("Explore", "explore"),
    GENRES("Genres", "category"),
    WATCHLIST("Vault", "bookmark"),
    SETTINGS("Settings", "settings")
}

data class CinemaPlayerState(
    val isVisible: Boolean = false,
    val mediaItem: MediaItem? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentPositionSec: Float = 0f,
    val totalDurationSec: Float = 148f,
    val bufferedPercent: Int = 85,
    val isMuted: Boolean = false,
    val volume: Float = 1.0f,
    val brightness: Float = 0.85f,
    val playbackSpeed: Float = 1.0f,
    val selectedAudioTrack: AudioTrackOption? = null,
    val selectedSubtitleTrack: SubtitleTrackOption? = null,
    val selectedQuality: StreamQualityOption? = null,
    val aspectRatio: String = "16:9", // "16:9", "21:9", "Fit", "Fill"
    val isFullscreen: Boolean = false,
    val isFullMovieMode: Boolean = true,
    val streamingEngine: String = "1EMBED", // "1EMBED" or "DIRECT"
    val selectedSeason: Int = 1,
    val selectedEpisode: Int = 1,
    val showAudioModal: Boolean = false,
    val showSubtitleModal: Boolean = false,
    val showQualityModal: Boolean = false,
    val showSpeedModal: Boolean = false,
    val showSettingsModal: Boolean = false,
    val isPiP: Boolean = false,
    val currentSubtitleCaption: String = ""
)

// Legacy alias for compatibility
typealias TrailerPlayerState = CinemaPlayerState

data class TmdbSyncStatus(
    val isSyncing: Boolean = false,
    val message: String = "TMDB Live Catalog Active • Ready to Fetch Trending & Details"
)

data class UserSettings(
    val themeMode: String = "DARK", // "DARK", "OLED", "LIGHT"
    val defaultAudioLanguage: String = "Original Atmos",
    val defaultSubtitleLanguage: String = "English [CC]",
    val preferredRegion: String = "Global",
    val streamingQuality: String = "4K Dolby Vision",
    val autoPlayTrailers: Boolean = true,
    val ambientGlow: Boolean = true,
    val offlineDataSaver: Boolean = false,
    val tmdbApiKey: String = "",
    val tmdbSyncActive: Boolean = true,
    val oneEmbedColor: String = "E50914",
    val oneEmbedAutoplay: Boolean = true,
    val oneEmbedAutonext: Boolean = true,
    val eightStreamApiUrl: String = "https://8stream-api.vercel.app",
    val useEightStreamPrimary: Boolean = true
)

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CineVerseDatabase.getDatabase(application)
    val repository = MediaRepository(database.watchlistDao())

    // Active navigation tab
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Selected Media item for Detail Page/Modal
    private val _selectedMedia = MutableStateFlow<MediaItem?>(null)
    val selectedMedia: StateFlow<MediaItem?> = _selectedMedia.asStateFlow()

    // Filter & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 8Stream API server diagnostics
    private val _eightStreamStatus = MutableStateFlow(
        com.example.data.api.EightStreamServerStatus(
            serverUrl = "https://8stream-api.vercel.app",
            message = "8Stream API Ready • Multi-Audio & IMDb Stream Resolver Active"
        )
    )
    val eightStreamStatus: StateFlow<com.example.data.api.EightStreamServerStatus> = _eightStreamStatus.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MediaItem>>(repository.getAllMedia())
    val searchResults: StateFlow<List<MediaItem>> = _searchResults.asStateFlow()

    // Live API media items
    private val _liveApiItems = MutableStateFlow<List<MediaItem>>(emptyList())
    val liveApiItems: StateFlow<List<MediaItem>> = _liveApiItems.asStateFlow()

    private val _isLoadingLiveApi = MutableStateFlow(false)
    val isLoadingLiveApi: StateFlow<Boolean> = _isLoadingLiveApi.asStateFlow()
    val isLiveApiLoading: StateFlow<Boolean> = _isLoadingLiveApi.asStateFlow()

    // Cinema Player State
    private val _playerState = MutableStateFlow(CinemaPlayerState())
    val playerState: StateFlow<CinemaPlayerState> = _playerState.asStateFlow()
    val trailerState: StateFlow<CinemaPlayerState> = _playerState.asStateFlow()
    val cinemaPlayerState: StateFlow<CinemaPlayerState> = _playerState.asStateFlow()
    private var playerProgressJob: Job? = null

    // TMDB Sync status
    private val _tmdbSyncStatus = MutableStateFlow(TmdbSyncStatus())
    val tmdbSyncStatus: StateFlow<TmdbSyncStatus> = _tmdbSyncStatus.asStateFlow()

    // Download simulation in progress (Map of mediaId -> progress percentage 0..100)
    private val _downloadingProgress = MutableStateFlow<Map<String, Int>>(emptyMap())
    val downloadingProgress: StateFlow<Map<String, Int>> = _downloadingProgress.asStateFlow()

    // Watchlist from DB
    val watchlistItems: StateFlow<List<WatchlistEntity>> = repository.watchlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadedItems: StateFlow<List<WatchlistEntity>> = repository.downloadedItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User Settings
    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    // Toast/Snackbar notifications
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    init {
        updateSearchResults()
        loadLiveApiMedia()
    }

    fun loadLiveApiMedia() {
        viewModelScope.launch {
            _isLoadingLiveApi.value = true
            try {
                val items = repository.fetchLiveApiMedia()
                _liveApiItems.value = items
                updateSearchResults()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoadingLiveApi.value = false
            }
        }
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun selectMedia(item: MediaItem?) {
        _selectedMedia.value = item
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        updateSearchResults()
    }

    fun updateFilter(update: (FilterState) -> FilterState) {
        _filterState.update(update)
        updateSearchResults()
    }

    fun resetFilters() {
        _filterState.value = FilterState()
        _searchQuery.value = ""
        updateSearchResults()
    }

    private fun updateSearchResults() {
        _searchResults.value = repository.searchAndFilter(_searchQuery.value, _filterState.value)
    }

    fun playMedia(item: MediaItem, isFullMovie: Boolean = true, engine: String = "1EMBED") {
        val defaultAudio = item.audioTrackOptions.firstOrNull { it.isOriginal }
            ?: item.audioTrackOptions.firstOrNull()
        val defaultSubtitle = item.subtitleTrackOptions.firstOrNull { it.language.contains("English", ignoreCase = true) }
            ?: item.subtitleTrackOptions.firstOrNull()
        val defaultQuality = item.qualityOptions.firstOrNull()

        val parsedDuration = parseDurationToSeconds(item.duration)

        _playerState.value = CinemaPlayerState(
            isVisible = true,
            mediaItem = item,
            isPlaying = true,
            isBuffering = false,
            currentPositionSec = 0f,
            totalDurationSec = parsedDuration,
            bufferedPercent = 35,
            isMuted = false,
            volume = 1.0f,
            brightness = 0.85f,
            playbackSpeed = 1.0f,
            selectedAudioTrack = defaultAudio,
            selectedSubtitleTrack = defaultSubtitle,
            selectedQuality = defaultQuality,
            aspectRatio = "16:9",
            isFullMovieMode = isFullMovie,
            streamingEngine = engine,
            selectedSeason = item.seasonNumber,
            selectedEpisode = item.episodeNumber,
            currentSubtitleCaption = if (defaultSubtitle != null && !defaultSubtitle.isOff) "♪ [Atmospheric Cinematic Score playing] ♪" else ""
        )
        startLocalPlaybackTimer()

        // If in direct stream mode and 8Stream is enabled, asynchronously resolve streams
        if (engine == "DIRECT" && _settings.value.useEightStreamPrimary) {
            viewModelScope.launch {
                try {
                    val resolved = repository.resolveStreamFromEightStream(item, _settings.value.eightStreamApiUrl)
                    if (resolved.isEightStreamResolved && _playerState.value.mediaItem?.id == item.id) {
                        _playerState.update { current ->
                            current.copy(
                                mediaItem = resolved,
                                selectedAudioTrack = resolved.audioTrackOptions.firstOrNull() ?: current.selectedAudioTrack,
                                selectedQuality = resolved.qualityOptions.firstOrNull() ?: current.selectedQuality
                            )
                        }
                        _toastEvent.emit("🎬 8Stream API: Connected Live Stream (${resolved.audioTrackOptions.size} Multi-Audio tracks)")
                    }
                } catch (e: Exception) {
                    // Smooth fallback to ultra-fast CDN
                }
            }
        }
    }

    fun play1Embed(item: MediaItem, season: Int = 1, episode: Int = 1) {
        val updatedItem = item.copy(seasonNumber = season, episodeNumber = episode)
        playMedia(updatedItem, isFullMovie = true, engine = "1EMBED")
        viewModelScope.launch {
            _toastEvent.emit("⚡ 1Embed Player Loaded: ${item.title} (Fast Stream)")
        }
    }

    fun setStreamingEngine(engine: String) {
        _playerState.update { it.copy(streamingEngine = engine) }
    }

    fun setEpisode(season: Int, episode: Int) {
        _playerState.update { current ->
            current.copy(
                selectedSeason = season,
                selectedEpisode = episode,
                mediaItem = current.mediaItem?.copy(seasonNumber = season, episodeNumber = episode)
            )
        }
    }

    fun openTrailer(item: MediaItem) {
        playMedia(item, isFullMovie = false, engine = "DIRECT")
    }

    fun closePlayer() {
        playerProgressJob?.cancel()
        _playerState.update { it.copy(isVisible = false, isPlaying = false) }
    }

    fun closeTrailer() {
        closePlayer()
    }

    fun togglePlayPause() {
        val nextPlaying = !_playerState.value.isPlaying
        _playerState.update { it.copy(isPlaying = nextPlaying) }
        if (nextPlaying) {
            startLocalPlaybackTimer()
        } else {
            playerProgressJob?.cancel()
        }
    }

    fun toggleTrailerPlayPause() {
        togglePlayPause()
    }

    fun seekTo(positionSec: Float) {
        val clamped = positionSec.coerceIn(0f, _playerState.value.totalDurationSec)
        _playerState.update { it.copy(currentPositionSec = clamped) }
        updateSubtitleCaptionForPosition(clamped)
    }

    fun seekTrailer(position: Float) {
        seekTo(position)
    }

    fun skipForward10() {
        val newPos = (_playerState.value.currentPositionSec + 10f).coerceAtMost(_playerState.value.totalDurationSec)
        seekTo(newPos)
    }

    fun skipBackward10() {
        val newPos = (_playerState.value.currentPositionSec - 10f).coerceAtLeast(0f)
        seekTo(newPos)
    }

    fun toggleMute() {
        _playerState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleTrailerMute() {
        toggleMute()
    }

    fun setVolume(vol: Float) {
        _playerState.update { it.copy(volume = vol.coerceIn(0f, 1f), isMuted = vol <= 0.01f) }
    }

    fun setBrightness(brightness: Float) {
        _playerState.update { it.copy(brightness = brightness.coerceIn(0.1f, 1f)) }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playerState.update { it.copy(playbackSpeed = speed) }
        viewModelScope.launch {
            _toastEvent.emit("Playback speed: ${speed}x")
        }
    }

    fun setAspectRatio(ratio: String) {
        _playerState.update { it.copy(aspectRatio = ratio) }
        viewModelScope.launch {
            _toastEvent.emit("Aspect ratio: $ratio")
        }
    }

    fun toggleFullscreen() {
        _playerState.update { it.copy(isFullscreen = !it.isFullscreen) }
    }

    fun setFullscreen(fullscreen: Boolean) {
        _playerState.update { it.copy(isFullscreen = fullscreen) }
    }

    fun selectAudioTrack(track: AudioTrackOption) {
        _playerState.update { it.copy(selectedAudioTrack = track, showAudioModal = false) }
        viewModelScope.launch {
            _toastEvent.emit("Audio switched to: ${track.label}")
        }
    }

    fun selectSubtitleTrack(track: SubtitleTrackOption) {
        _playerState.update { 
            it.copy(
                selectedSubtitleTrack = track, 
                showSubtitleModal = false,
                currentSubtitleCaption = if (track.isOff) "" else "♪ [Subtitles: ${track.language}] ♪"
            ) 
        }
        viewModelScope.launch {
            _toastEvent.emit("Subtitles: ${track.label}")
        }
    }

    fun selectQuality(quality: StreamQualityOption) {
        _playerState.update { it.copy(selectedQuality = quality, showQualityModal = false) }
        viewModelScope.launch {
            _toastEvent.emit("Stream quality: ${quality.label} (${quality.resolution})")
        }
    }

    fun setTrailerQuality(quality: String) {
        val qOption = _playerState.value.mediaItem?.qualityOptions?.find { it.label.contains(quality, ignoreCase = true) }
            ?: StreamQualityOption(quality, "1080p", "12 Mbps", _playerState.value.mediaItem?.videoStreamUrl ?: "")
        selectQuality(qOption)
    }

    fun toggleAudioModal(show: Boolean) {
        _playerState.update { it.copy(showAudioModal = show, showSubtitleModal = false, showQualityModal = false, showSpeedModal = false) }
    }

    fun toggleSubtitleModal(show: Boolean) {
        _playerState.update { it.copy(showSubtitleModal = show, showAudioModal = false, showQualityModal = false, showSpeedModal = false) }
    }

    fun toggleQualityModal(show: Boolean) {
        _playerState.update { it.copy(showQualityModal = show, showAudioModal = false, showSubtitleModal = false, showSpeedModal = false) }
    }

    fun toggleSpeedModal(show: Boolean) {
        _playerState.update { it.copy(showSpeedModal = show, showAudioModal = false, showSubtitleModal = false, showQualityModal = false) }
    }

    fun onNativePlayerProgress(positionMs: Long, durationMs: Long, bufferPercent: Int) {
        if (durationMs > 0) {
            val totalSec = durationMs / 1000f
            val currentSec = positionMs / 1000f
            _playerState.update {
                it.copy(
                    currentPositionSec = currentSec,
                    totalDurationSec = totalSec,
                    bufferedPercent = bufferPercent,
                    isBuffering = false
                )
            }
            updateSubtitleCaptionForPosition(currentSec)
        }
    }

    private fun updateSubtitleCaptionForPosition(posSec: Float) {
        val track = _playerState.value.selectedSubtitleTrack
        if (track == null || track.isOff) {
            _playerState.update { it.copy(currentSubtitleCaption = "") }
            return
        }

        val item = _playerState.value.mediaItem
        val sec = posSec.toInt()
        val caption = when {
            sec in 0..4 -> "“${item?.tagline ?: "Welcome to CineVerse World Premiere"}”"
            sec in 5..9 -> "[Dramatic orchestral overture crescendo]"
            sec in 10..15 -> "${item?.cast?.firstOrNull()?.name ?: "Protagonist"}: \"We have only one chance to synchronize the timeline.\""
            sec in 16..22 -> "${item?.cast?.getOrNull(1)?.name ?: "Commander"}: \"All coordinates confirmed. Initiating quantum sequence.\""
            sec in 23..29 -> "[High voltage pulsing & atmospheric reverberation]"
            sec in 30..36 -> "\"Every system in the construct is responding to our presence.\""
            sec in 37..45 -> "[Intense kinetic chase sequence begins]"
            sec in 46..55 -> "\"Hold your position! The horizon is collapsing!\""
            sec in 56..68 -> "[Sonic resonance harmonic frequency peak]"
            sec in 69..80 -> "\"There is no turning back now.\""
            else -> "♪ [High Definition 4K Audio: ${track.language}] ♪"
        }
        _playerState.update { it.copy(currentSubtitleCaption = caption) }
    }

    private fun startLocalPlaybackTimer() {
        playerProgressJob?.cancel()
        playerProgressJob = viewModelScope.launch {
            while (_playerState.value.isPlaying) {
                delay((1000 / _playerState.value.playbackSpeed).toLong().coerceAtLeast(200L))
                _playerState.update { state ->
                    val next = state.currentPositionSec + 1f
                    val buffered = (state.bufferedPercent + 2).coerceAtMost(100)
                    if (next >= state.totalDurationSec) {
                        state.copy(currentPositionSec = 0f, isPlaying = true)
                    } else {
                        state.copy(currentPositionSec = next, bufferedPercent = buffered)
                    }
                }
                updateSubtitleCaptionForPosition(_playerState.value.currentPositionSec)
            }
        }
    }

    private fun parseDurationToSeconds(durationStr: String): Float {
        return try {
            if (durationStr.contains("h")) {
                val parts = durationStr.split("h")
                val hours = parts[0].trim().toFloatOrNull() ?: 1f
                val mins = parts.getOrNull(1)?.replace("m", "")?.trim()?.toFloatOrNull() ?: 30f
                (hours * 3600f) + (mins * 60f)
            } else if (durationStr.contains("m")) {
                val parts = durationStr.split("m")
                val mins = parts[0].trim().toFloatOrNull() ?: 12f
                val secs = parts.getOrNull(1)?.replace("s", "")?.trim()?.toFloatOrNull() ?: 0f
                (mins * 60f) + secs
            } else if (durationStr.contains("s")) {
                durationStr.replace("s", "").trim().toFloatOrNull() ?: 90f
            } else {
                180f
            }
        } catch (e: Exception) {
            180f
        }
    }

    // Watchlist Operations
    fun toggleWatchlist(item: MediaItem, defaultStatus: String = "PLAN_TO_WATCH") {
        viewModelScope.launch {
            val exists = watchlistItems.value.any { it.id == item.id }
            if (exists) {
                repository.removeFromWatchlist(item.id)
                _toastEvent.emit("Removed from your Vault")
            } else {
                repository.addToWatchlist(item, defaultStatus)
                _toastEvent.emit("Saved to Vault (${defaultStatus.replace('_', ' ')})")
            }
        }
    }

    fun updateWatchlistStatus(id: String, status: String) {
        viewModelScope.launch {
            repository.updateWatchlistStatus(id, status)
            _toastEvent.emit("Status updated to ${status.replace('_', ' ')}")
        }
    }

    fun removeFromWatchlist(id: String) {
        viewModelScope.launch {
            repository.removeFromWatchlist(id)
            _toastEvent.emit("Removed from Vault")
        }
    }

    // Offline Download Simulator
    fun simulateDownload(mediaItem: MediaItem) {
        val id = mediaItem.id
        if (_downloadingProgress.value.containsKey(id)) return

        viewModelScope.launch {
            val exists = watchlistItems.value.any { it.id == id }
            if (!exists) {
                repository.addToWatchlist(mediaItem, "PLAN_TO_WATCH")
            }

            _toastEvent.emit("Starting offline 4K sync for ${mediaItem.title}...")
            for (p in 1..10) {
                _downloadingProgress.update { it + (id to (p * 10)) }
                delay(250)
            }
            _downloadingProgress.update { it - id }
            repository.toggleDownloaded(id, true)
            _toastEvent.emit("✓ ${mediaItem.title} available for offline viewing!")
        }
    }

    fun removeDownloaded(id: String) {
        viewModelScope.launch {
            repository.toggleDownloaded(id, false)
            _toastEvent.emit("Offline cache cleared")
        }
    }

    fun updateUserFeedback(id: String, rating: Float, notes: String) {
        viewModelScope.launch {
            repository.updateUserFeedback(id, rating, notes)
            _toastEvent.emit("Review saved to Vault")
        }
    }

    // Settings
    fun updateTheme(themeMode: String) {
        _settings.update { it.copy(themeMode = themeMode) }
    }

    fun updateSettings(update: (UserSettings) -> UserSettings) {
        _settings.update(update)
    }

    fun testEightStreamConnection(serverUrl: String) {
        viewModelScope.launch {
            _eightStreamStatus.value = com.example.data.api.EightStreamServerStatus(
                serverUrl = serverUrl,
                message = "Testing connection to 8Stream API server..."
            )
            val result = com.example.data.api.EightStreamApiClient.testConnection(serverUrl)
            _eightStreamStatus.value = result
            _toastEvent.emit(result.message)
        }
    }

    fun updateEightStreamUrl(url: String) {
        _settings.update { it.copy(eightStreamApiUrl = url) }
        testEightStreamConnection(url)
    }

    fun updateTmdbApiKey(key: String) {
        _settings.update { it.copy(tmdbApiKey = key) }
        com.example.data.api.tmdb.TmdbApiClient.setCustomApiKey(key)
        viewModelScope.launch {
            _toastEvent.emit("TMDB API Key configured successfully")
            syncTmdbData()
        }
    }

    fun syncTmdbData() {
        viewModelScope.launch {
            _isLoadingLiveApi.value = true
            _tmdbSyncStatus.value = TmdbSyncStatus(isSyncing = true, message = "Connecting to TMDB API & downloading live movie metadata...")
            _toastEvent.emit("Connecting to TMDB API & syncing movies...")
            try {
                val items = repository.fetchLiveApiMedia()
                _liveApiItems.value = items
                updateSearchResults()
                _tmdbSyncStatus.value = TmdbSyncStatus(
                    isSyncing = false,
                    message = "TMDB Live Catalog: ${items.size} real movies & posters active"
                )
                _toastEvent.emit("✓ Synced ${items.size} titles & posters from TMDB")
            } catch (e: Exception) {
                _tmdbSyncStatus.value = TmdbSyncStatus(
                    isSyncing = false,
                    message = "TMDB Standby: ${e.localizedMessage ?: "Network error"}"
                )
                _toastEvent.emit("Failed to sync TMDB: ${e.localizedMessage}")
            } finally {
                _isLoadingLiveApi.value = false
            }
        }
    }
}

