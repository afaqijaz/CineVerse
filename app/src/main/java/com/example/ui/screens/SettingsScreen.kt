package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.oneembed.OneEmbedStreaming
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentPurple
import com.example.ui.viewmodel.MediaViewModel

@Composable
fun SettingsScreen(
    viewModel: MediaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val downloadedItems by viewModel.downloadedItems.collectAsState()
    val tmdbStatus by viewModel.tmdbSyncStatus.collectAsState()
    val playerState by viewModel.cinemaPlayerState.collectAsState()

    var showAudioMenu by remember { mutableStateOf(false) }
    var showSubtitleMenu by remember { mutableStateOf(false) }
    var showQualityMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D0F12))
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Preferences & System",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Customize streaming parameters, themes, and global regional defaults",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Section 1: Appearance & Theme
        item {
            SettingsCategoryHeader(title = "Appearance & Interface", icon = Icons.Default.DarkMode)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ThemeOptionCard(
                    title = "Deep Charcoal",
                    subtitle = "Recommended",
                    isSelected = settings.themeMode == "DARK",
                    color = Color(0xFF0D0F12),
                    accent = AccentCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.updateTheme("DARK") }
                )

                ThemeOptionCard(
                    title = "OLED Pitch",
                    subtitle = "True Black",
                    isSelected = settings.themeMode == "OLED",
                    color = Color(0xFF000000),
                    accent = AccentPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.updateTheme("OLED") }
                )

                ThemeOptionCard(
                    title = "Crisp Light",
                    subtitle = "Day Mode",
                    isSelected = settings.themeMode == "LIGHT",
                    color = Color(0xFFF8FAFC),
                    accent = Color(0xFF007A8C),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.updateTheme("LIGHT") }
                )
            }
        }

        // Section 2: Regional Audio & Subtitle Defaults
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsCategoryHeader(title = "Audio & Multi-Language Preferences", icon = Icons.Default.Language)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF151922),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Default Audio
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAudioMenu = true }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Audiotrack,
                                    contentDescription = null,
                                    tint = AccentCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Primary Audio Track", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Preferred language for global streams", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                }
                            }
                            Text("${settings.defaultAudioLanguage} ▾", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        }

                        DropdownMenu(
                            expanded = showAudioMenu,
                            onDismissRequest = { showAudioMenu = false },
                            modifier = Modifier.background(Color(0xFF1E2433))
                        ) {
                            listOf("Original", "English", "Korean", "Hindi", "Japanese", "Spanish", "French", "German", "Portuguese").forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang, color = Color.White) },
                                    onClick = {
                                        showAudioMenu = false
                                        viewModel.updateSettings { it.copy(defaultAudioLanguage = lang) }
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF262E3E), modifier = Modifier.padding(vertical = 4.dp))

                    // Default Subtitles
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showSubtitleMenu = true }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Subtitles,
                                    contentDescription = null,
                                    tint = AccentPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Default Subtitle Language", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Auto-enable translations when available", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                }
                            }
                            Text("${settings.defaultSubtitleLanguage} ▾", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentPurple)
                        }

                        DropdownMenu(
                            expanded = showSubtitleMenu,
                            onDismissRequest = { showSubtitleMenu = false },
                            modifier = Modifier.background(Color(0xFF1E2433))
                        ) {
                            listOf("English", "Off", "Korean", "Hindi", "Japanese", "Spanish", "French", "German", "Arabic", "Portuguese", "Turkish").forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub, color = Color.White) },
                                    onClick = {
                                        showSubtitleMenu = false
                                        viewModel.updateSettings { it.copy(defaultSubtitleLanguage = sub) }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Playback & Streaming Quality
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsCategoryHeader(title = "Playback & Stream Quality", icon = Icons.Default.Hd)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF151922),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Quality Picker
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showQualityMenu = true }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = null,
                                    tint = AccentCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Default Streaming Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Applies to trailers and media previews", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                }
                            }
                            Text("${settings.streamingQuality} ▾", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                        }

                        DropdownMenu(
                            expanded = showQualityMenu,
                            onDismissRequest = { showQualityMenu = false },
                            modifier = Modifier.background(Color(0xFF1E2433))
                        ) {
                            listOf("4K Dolby Vision", "1080p FHD (Balanced)", "720p HD (Data Saver)").forEach { q ->
                                DropdownMenuItem(
                                    text = { Text(q, color = Color.White) },
                                    onClick = {
                                        showQualityMenu = false
                                        viewModel.updateSettings { it.copy(streamingQuality = q) }
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF262E3E), modifier = Modifier.padding(vertical = 4.dp))

                    // Auto Play Trailers Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = AccentEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Auto-Play Hero Trailers", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Preview content automatically in hero banner", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                        }
                        Switch(
                            checked = settings.autoPlayTrailers,
                            onCheckedChange = { checked -> viewModel.updateSettings { it.copy(autoPlayTrailers = checked) } },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AccentCyan,
                                checkedTrackColor = AccentCyan.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
        }

        // Section 4: 8Stream API & External Movie Streaming Engine
        item {
            val eightStreamStatus by viewModel.eightStreamStatus.collectAsState()
            var customServerUrl by remember(settings.eightStreamApiUrl) { mutableStateOf(settings.eightStreamApiUrl) }

            Spacer(modifier = Modifier.height(16.dp))
            SettingsCategoryHeader(title = "8Stream API & Stream Engine", icon = Icons.Default.Public)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF151922),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (eightStreamStatus.isConnected) Color(0xFF00E676) else Color(0xFFFFB800))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (eightStreamStatus.isConnected) "8Stream API Active" else "8Stream Standby / Fallback CDN",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        if (eightStreamStatus.latencyMs > 0) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF1E2433)
                            ) {
                                Text(
                                    text = "${eightStreamStatus.latencyMs}ms",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = eightStreamStatus.message,
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF262E3E))
                    Spacer(modifier = Modifier.height(12.dp))

                    // API Server URL Input
                    Text(
                        text = "8Stream API Endpoint URL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE2E8F0)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    androidx.compose.material3.OutlinedTextField(
                        value = customServerUrl,
                        onValueChange = { customServerUrl = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("eight_stream_url_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = Color(0xFF262E3E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF0F1218),
                            unfocusedContainerColor = Color(0xFF0F1218)
                        ),
                        placeholder = {
                            Text("https://8stream-api.vercel.app", color = Color(0xFF64748B), fontSize = 13.sp)
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Preset buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Vercel Node" to "https://8stream-api.vercel.app",
                            "Localhost:3000" to "http://10.0.2.2:3000"
                        ).forEach { (label, url) ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (customServerUrl == url) AccentCyan.copy(alpha = 0.15f) else Color(0xFF1E2433),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (customServerUrl == url) AccentCyan else Color(0xFF262E3E)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        customServerUrl = url
                                        viewModel.updateEightStreamUrl(url)
                                    }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (customServerUrl == url) AccentCyan else Color(0xFFE2E8F0),
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateEightStreamUrl(customServerUrl)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentCyan,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_test_eightstream")
                        ) {
                            Text("Ping & Save Server", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Features Supported Badges
                    Text(
                        text = "Integrated Capabilities (himanshu8443/8StreamApi):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("IMDb ID /mediaInfo", "Multi-Audio (Hindi/Eng)", "HLS 1080p", "Fast CDN Fallback").forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF1E2433)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 10.sp,
                                    color = Color(0xFFCBD5E1),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 5: TMDB API & 1Embed.cc Streaming Engine
        item {
            var tmdbKeyInput by remember { mutableStateOf("") }

            Spacer(modifier = Modifier.height(16.dp))
            SettingsCategoryHeader(title = "TMDB API & 1Embed Streaming", icon = Icons.Default.Movie)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF151922),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // TMDB Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (tmdbStatus.isSyncing) Color(0xFFFFB800) else Color(0xFF01D277))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "The Movie Database (TMDB)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF01D277).copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF01D277))
                        ) {
                            Text(
                                text = "v3 Retrofit",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF01D277),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tmdbStatus.message,
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Optional Custom TMDB API Key Input
                    Text(
                        text = "TMDB API Key / Read Access Token",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE2E8F0)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    androidx.compose.material3.OutlinedTextField(
                        value = tmdbKeyInput,
                        onValueChange = { tmdbKeyInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tmdb_key_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF01D277),
                            unfocusedBorderColor = Color(0xFF262E3E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF0F1218),
                            unfocusedContainerColor = Color(0xFF0F1218)
                        ),
                        placeholder = {
                            Text("Enter TMDB API Key (Optional)", color = Color(0xFF64748B), fontSize = 13.sp)
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // TMDB Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (tmdbKeyInput.isNotBlank()) {
                                    viewModel.updateTmdbApiKey(tmdbKeyInput.trim())
                                }
                                viewModel.syncTmdbData()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF01D277),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_sync_tmdb")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sync",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (tmdbStatus.isSyncing) "Syncing TMDB..." else "Sync TMDB Catalog",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFF262E3E))
                    Spacer(modifier = Modifier.height(14.dp))

                    // 1Embed.cc Streaming Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = Color(0xFFE50914),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "1Embed.cc Streaming",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "https://1embed.cc/#quick-start",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE50914).copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE50914))
                        ) {
                            Text(
                                text = "Active Player",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE50914),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Default Engine Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F1218))
                            .border(1.dp, Color(0xFF262E3E), RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val is1Embed = playerState.streamingEngine == "1EMBED"
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (is1Embed) Color(0xFFE50914) else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setStreamingEngine("1EMBED") }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚡ 1Embed (Recommended)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (!is1Embed) AccentCyan else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setStreamingEngine("DIRECT") }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Direct MP4 Streams",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!is1Embed) Color.Black else Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick-Start Info & Actions
                    Text(
                        text = "1Embed embed URL scheme: https://1embed.cc/embed/{id} for movies and https://1embed.cc/embed/{id}/{season}/{episode} for TV shows.",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8),
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Copy responsive iframe snippet
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E2433),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333E54)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val snippet = "<iframe src=\"https://1embed.cc/embed/550\" width=\"100%\" height=\"100%\" frameborder=\"0\" allowfullscreen></iframe>"
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("1Embed Snippet", snippet)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "1Embed HTML iframe snippet copied!", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 9.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Copy iframe Code",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        // Open 1embed.cc quick start in browser
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E2433),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333E54)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://1embed.cc/#quick-start"))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error opening browser: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 9.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = null,
                                    tint = AccentCyan,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "1embed.cc Quick-Start",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AccentCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 6: Storage & Offline Cache
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsCategoryHeader(title = "Offline Storage & Cache", icon = Icons.Default.CleaningServices)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF151922),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Offline Vault Cache", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${downloadedItems.size} packages downloaded (${downloadedItems.size * 1.45} GB)", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        }

                        Button(
                            onClick = {
                                downloadedItems.forEach { item ->
                                    viewModel.removeDownloaded(item.id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF283244),
                                contentColor = Color(0xFFFF3D71)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Clear Cache", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section 5: CineVerse App Info & Global Catalog Stats
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsCategoryHeader(title = "About CineVerse Global", icon = Icons.Default.Info)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF151922),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262E3E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(AccentCyan, AccentPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("C", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("CineVerse Global v3.4.0", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Universal Media Discovery Engine", fontSize = 12.sp, color = AccentCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatPill(number = "50+", label = "Countries")
                        StatPill(number = "19+", label = "Languages")
                        StatPill(number = "10K+", label = "Titles")
                        StatPill(number = "6", label = "Platforms")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCategoryHeader(
    title: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AccentCyan,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun ThemeOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    color: Color,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color,
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) accent else Color(0xFF2E384C)
        ),
        modifier = modifier
            .height(85.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(accent)
            )

            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (color == Color(0xFFF8FAFC)) Color.Black else Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = if (color == Color(0xFFF8FAFC)) Color(0xFF64748B) else Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
private fun StatPill(
    number: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = number, fontSize = 16.sp, fontWeight = FontWeight.Black, color = AccentCyan)
        Text(text = label, fontSize = 10.sp, color = Color(0xFF94A3B8))
    }
}
