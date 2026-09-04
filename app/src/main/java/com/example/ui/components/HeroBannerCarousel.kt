package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MediaItem
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassButtonBg
import com.example.ui.theme.GlassButtonBorder
import com.example.ui.theme.GlassCyanBg
import com.example.ui.theme.GlassCyanBorder
import com.example.ui.theme.GlassSurface
import kotlinx.coroutines.delay

@Composable
fun HeroBannerCarousel(
    heroItems: List<MediaItem>,
    onPlayTrailer: (MediaItem) -> Unit,
    onToggleWatchlist: (MediaItem) -> Unit,
    onOpenDetails: (MediaItem) -> Unit,
    isInWatchlist: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    if (heroItems.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    // Auto-advance banner every 6 seconds
    LaunchedEffect(currentIndex, heroItems.size) {
        delay(6000)
        currentIndex = (currentIndex + 1) % heroItems.size
    }

    val currentItem = heroItems[currentIndex]
    val isSaved = isInWatchlist(currentItem.id)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .height(390.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(GlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
            .testTag("hero_banner_carousel")
    ) {
        // Animated Backdrop Transition
        AnimatedContent(
            targetState = currentItem,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "hero_image_transition"
        ) { targetItem ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (!targetItem.backdropUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = targetItem.backdropUrl,
                        contentDescription = targetItem.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (targetItem.localBackdropRes != null) {
                    Image(
                        painter = painterResource(id = targetItem.localBackdropRes),
                        contentDescription = targetItem.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AsyncImage(
                        model = targetItem.posterUrl,
                        contentDescription = targetItem.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Multi-Layer Gradients for Cinema Ambiance & Text Legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color(0xFF0D0F12).copy(alpha = 0.75f),
                            Color(0xFF0D0F12).copy(alpha = 0.98f)
                        )
                    )
                )
        )

        // Top Badges Row (Unique Premiere & 4K Atmos indicators)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE50914).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✦ SPOTLIGHT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.6f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "4K ULTRA HD • DOLBY ATMOS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE2E8F0),
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                    letterSpacing = 0.8.sp
                )
            }
        }

        // Content on Hero
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Badges Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                // Genre / Highlight Pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GlassCyanBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassCyanBorder)
                ) {
                    Text(
                        text = (currentItem.genres.firstOrNull() ?: "Featured").uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp),
                        letterSpacing = 0.8.sp
                    )
                }

                // Country Flag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderSubtle)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                    ) {
                        Text(text = currentItem.countryFlag, fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentItem.country,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Match %
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF01D277).copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF01D277).copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${currentItem.matchScore}% Match",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF01D277),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                    )
                }

                // Rating
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF5C518).copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF5C518).copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                    ) {
                        Text(text = "★", fontSize = 10.sp, color = Color(0xFFF5C518))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${currentItem.rating}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF5C518)
                        )
                    }
                }
            }

            // Title
            Text(
                text = currentItem.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                lineHeight = 28.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Tagline or Genres
            Text(
                text = if (currentItem.tagline.isNotBlank()) "“${currentItem.tagline}”" else currentItem.genres.joinToString(" • "),
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFE3E2E6).copy(alpha = 0.75f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Call to Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stream Now CTA (Red Cinema Button)
                Button(
                    onClick = { onPlayTrailer(currentItem) },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE50914),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1.2f)
                        .height(42.dp)
                        .testTag("hero_play_trailer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Now",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Play Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Add to Vault CTA (Frosted Glass Button)
                Button(
                    onClick = { onToggleWatchlist(currentItem) },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) AccentViolet.copy(alpha = 0.35f) else GlassButtonBg,
                        contentColor = if (isSaved) AccentCyan else Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSaved) AccentViolet else GlassButtonBorder
                    ),
                    modifier = Modifier
                        .height(42.dp)
                        .testTag("hero_watchlist_button")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save to Vault",
                        tint = if (isSaved) AccentCyan else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSaved) "Saved" else "Save",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Info / Details Button (Frosted Circle)
                IconButton(
                    onClick = { onOpenDetails(currentItem) },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(GlassButtonBg)
                        .border(1.dp, GlassButtonBorder, CircleShape)
                        .testTag("hero_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Details",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel Dot Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                heroItems.forEachIndexed { index, _ ->
                    val isSelected = index == currentIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(4.dp)
                            .width(if (isSelected) 22.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) AccentViolet else Color.White.copy(alpha = 0.2f))
                            .clickable { currentIndex = index }
                    )
                }
            }
        }
    }
}
