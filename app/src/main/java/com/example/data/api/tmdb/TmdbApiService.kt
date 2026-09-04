package com.example.data.api.tmdb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("3/trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String = "week",
        @Query("page") page: Int = 1
    ): Response<TmdbPagedResponse<TmdbMovieDto>>

    @GET("3/trending/tv/{time_window}")
    suspend fun getTrendingTv(
        @Path("time_window") timeWindow: String = "week",
        @Query("page") page: Int = 1
    ): Response<TmdbPagedResponse<TmdbTvDto>>

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbMovieDto>>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbMovieDto>>

    @GET("3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbMovieDto>>

    @GET("3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbMovieDto>>

    @GET("3/tv/popular")
    suspend fun getPopularTv(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbTvDto>>

    @GET("3/tv/top_rated")
    suspend fun getTopRatedTv(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbTvDto>>

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") append: String = "credits,videos,recommendations",
        @Query("language") language: String = "en-US"
    ): Response<TmdbMovieDetailsDto>

    @GET("3/tv/{series_id}")
    suspend fun getTvDetails(
        @Path("series_id") seriesId: Int,
        @Query("append_to_response") append: String = "credits,videos,recommendations",
        @Query("language") language: String = "en-US"
    ): Response<TmdbTvDetailsDto>

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbMovieDto>>

    @GET("3/search/tv")
    suspend fun searchTv(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbTvDto>>

    @GET("3/discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TmdbPagedResponse<TmdbMovieDto>>
}
