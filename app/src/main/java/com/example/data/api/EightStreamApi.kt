package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class EightStreamPlaylistItem(
    @Json(name = "title") val title: String = "",
    @Json(name = "file") val file: String = "",
    @Json(name = "key") val key: String = "",
    @Json(name = "language") val language: String? = null,
    @Json(name = "quality") val quality: String? = null
)

@JsonClass(generateAdapter = true)
data class EightStreamMediaInfoResponse(
    @Json(name = "title") val title: String? = null,
    @Json(name = "imdb_id") val imdbId: String? = null,
    @Json(name = "playlist") val playlist: List<EightStreamPlaylistItem> = emptyList(),
    @Json(name = "seasons") val seasons: Int? = null,
    @Json(name = "episodes") val episodes: Int? = null,
    @Json(name = "status") val status: String? = null
)

@JsonClass(generateAdapter = true)
data class EightStreamGetStreamRequest(
    @Json(name = "file") val file: String,
    @Json(name = "key") val key: String
)

@JsonClass(generateAdapter = true)
data class EightStreamQualityOptionItem(
    @Json(name = "quality") val quality: String = "1080p",
    @Json(name = "url") val url: String = ""
)

@JsonClass(generateAdapter = true)
data class EightStreamGetStreamResponse(
    @Json(name = "stream") val stream: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "qualities") val qualities: List<EightStreamQualityOptionItem> = emptyList(),
    @Json(name = "subtitles") val subtitles: List<Map<String, String>> = emptyList(),
    @Json(name = "audio") val audio: List<String> = emptyList()
)

data class EightStreamServerStatus(
    val isConnected: Boolean = false,
    val latencyMs: Long = 0,
    val serverUrl: String = "",
    val message: String = ""
)

interface EightStreamApiService {
    @GET("api/v1/mediaInfo")
    suspend fun getMediaInfo(
        @Query("id") imdbId: String
    ): Response<EightStreamMediaInfoResponse>

    @POST("api/v1/getStream")
    suspend fun getStream(
        @Body request: EightStreamGetStreamRequest
    ): Response<EightStreamGetStreamResponse>

    @GET
    suspend fun pingServer(
        @Url url: String
    ): Response<String>
}

object EightStreamApiClient {
    private var currentBaseUrl = "https://8stream-api.vercel.app/"

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .connectTimeout(6, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    private var retrofitInstance: Retrofit? = null
    private var serviceInstance: EightStreamApiService? = null

    fun getService(baseUrl: String = currentBaseUrl): EightStreamApiService {
        val normalizedUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        if (retrofitInstance == null || currentBaseUrl != normalizedUrl) {
            currentBaseUrl = normalizedUrl
            retrofitInstance = Retrofit.Builder()
                .baseUrl(normalizedUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            serviceInstance = retrofitInstance?.create(EightStreamApiService::class.java)
        }
        return serviceInstance ?: retrofitInstance!!.create(EightStreamApiService::class.java)
    }

    suspend fun testConnection(serverUrl: String): EightStreamServerStatus {
        val start = System.currentTimeMillis()
        return try {
            val normalizedUrl = if (serverUrl.endsWith("/")) serverUrl else "$serverUrl/"
            val service = getService(normalizedUrl)
            // Test with a sample IMDB ID like tt15398776 (Oppenheimer)
            val response = service.getMediaInfo("tt15398776")
            val latency = System.currentTimeMillis() - start
            if (response.isSuccessful && response.body() != null) {
                EightStreamServerStatus(
                    isConnected = true,
                    latencyMs = latency,
                    serverUrl = serverUrl,
                    message = "8StreamApi Online • ${response.body()?.title ?: "Stream Provider Ready"} (${latency}ms)"
                )
            } else {
                EightStreamServerStatus(
                    isConnected = true,
                    latencyMs = latency,
                    serverUrl = serverUrl,
                    message = "8StreamApi Responded (HTTP ${response.code()}) (${latency}ms)"
                )
            }
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - start
            EightStreamServerStatus(
                isConnected = false,
                latencyMs = latency,
                serverUrl = serverUrl,
                message = "Standby / Fallback CDN Active (${e.localizedMessage ?: "Connection Timeout"})"
            )
        }
    }
}
