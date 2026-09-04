package com.example.data.api.oneembed

import com.example.data.model.MediaItem
import com.example.data.model.MediaType

/**
 * Direct implementation of 1Embed streaming engine based on https://1embed.cc/#quick-start
 * 
 * Embed URLs:
 * - Movies: https://1embed.cc/embed/movie/{movie_id}
 * - TV Shows: https://1embed.cc/embed/tv/{show_id}/{season}/{episode}
 * 
 * Query Parameters:
 * - color: UI accent color (e.g. E50914)
 * - autoplay: 1 or 0
 * - autonext: 1 or 0 (auto-next episode overlay)
 */
object OneEmbedStreaming {

    const val BASE_URL = "https://1embed.cc"
    const val DEFAULT_THEME_COLOR = "E50914" // CineVerse Red

    /**
     * Constructs the official embed player URL
     */
    fun buildEmbedUrl(
        item: MediaItem,
        season: Int = item.seasonNumber,
        episode: Int = item.episodeNumber,
        colorHex: String = DEFAULT_THEME_COLOR,
        autoplay: Boolean = true,
        autonext: Boolean = true
    ): String {
        val id = item.tmdbId?.toString() ?: item.imdbId.ifBlank { item.id.removePrefix("mv_").removePrefix("tv_").removePrefix("tmdb_m_").removePrefix("tmdb_tv_") }
        val cleanColor = colorHex.removePrefix("#")
        val autoPlayParam = if (autoplay) "1" else "0"
        val autoNextParam = if (autonext) "1" else "0"

        val path = if (item.mediaType == MediaType.TV_SHOW) {
            "/embed/tv/$id/$season/$episode"
        } else {
            "/embed/movie/$id"
        }

        return "$BASE_URL$path?color=$cleanColor&autoplay=$autoPlayParam&autonext=$autoNextParam"
    }

    /**
     * Builds standard 16:9 responsive HTML page wrapper for in-app WebView playback
     * as documented in Step 1 & 2 of https://1embed.cc/#quick-start
     */
    fun buildResponsiveHtmlPage(
        item: MediaItem,
        season: Int = item.seasonNumber,
        episode: Int = item.episodeNumber
    ): String {
        val embedUrl = buildEmbedUrl(item, season, episode)
        return buildResponsiveHtmlPage(embedUrl = embedUrl, title = item.title)
    }

    /**
     * Builds standard 16:9 responsive HTML page wrapper for in-app WebView playback
     * as documented in Step 1 & 2 of https://1embed.cc/#quick-start
     */
    fun buildResponsiveHtmlPage(
        embedUrl: String,
        title: String
    ): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
              <title>$title - 1Embed Cinema</title>
              <style>
                * {
                  margin: 0;
                  padding: 0;
                  box-sizing: border-box;
                  background-color: #000000;
                }
                html, body {
                  width: 100%;
                  height: 100%;
                  overflow: hidden;
                  background: #000000;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                }
                .embed-container {
                  position: relative;
                  width: 100%;
                  height: 100%;
                  max-height: 100vh;
                  background: #000000;
                }
                iframe {
                  position: absolute;
                  top: 0;
                  left: 0;
                  width: 100%;
                  height: 100%;
                  border: 0;
                }
              </style>
            </head>
            <body>
              <div class="embed-container">
                <iframe 
                  src="$embedUrl" 
                  frameborder="0" 
                  allowfullscreen="true" 
                  webkitallowfullscreen="true" 
                  mozallowfullscreen="true"
                  allow="encrypted-media; autoplay; fullscreen; picture-in-picture">
                </iframe>
              </div>
            </body>
            </html>
        """.trimIndent()
    }

    /**
     * Generates copyable HTML iframe code snippet from https://1embed.cc/#quick-start
     */
    fun generateQuickStartSnippet(item: MediaItem, season: Int = 1, episode: Int = 1): String {
        val embedUrl = buildEmbedUrl(item, season, episode, autoplay = false)
        return """<!-- 1Embed 16:9 Aspect Ratio Container -->
<div style="position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden; border-radius: 8px;">
  <iframe
    src="$embedUrl"
    style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;"
    frameborder="0"
    allowfullscreen
    allow="encrypted-media; autoplay; fullscreen">
  </iframe>
</div>"""
    }
}
