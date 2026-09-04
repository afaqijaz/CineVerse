package com.example.data.repository

import com.example.R
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.data.model.CastMember
import com.example.data.model.FilterState
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.SortOption
import com.example.data.model.StreamingPlatform
import kotlinx.coroutines.flow.Flow

class MediaRepository(private val watchlistDao: WatchlistDao) {

    // Platforms
    val netflix = StreamingPlatform("Netflix", 0xFFE50914, "N")
    val prime = StreamingPlatform("Prime Video", 0xFF00A8E1, "Prime")
    val disney = StreamingPlatform("Disney+", 0xFF113CCF, "D+")
    val apple = StreamingPlatform("Apple TV+", 0xFF4A4A4A, "Apple")
    val max = StreamingPlatform("Max", 0xFF002BE7, "Max")
    val crunchyroll = StreamingPlatform("Crunchyroll", 0xFFFF6400, "CR")
    val paramount = StreamingPlatform("Paramount+", 0xFF0064FF, "P+")

    val allPlatforms = listOf(netflix, prime, disney, apple, max, crunchyroll, paramount)

    val globalCountries = listOf(
        CountryMeta("USA", "🇺🇸", "United States", "English"),
        CountryMeta("South Korea", "🇰🇷", "South Korea", "Korean"),
        CountryMeta("India", "🇮🇳", "India", "Hindi"),
        CountryMeta("Japan", "🇯🇵", "Japan", "Japanese"),
        CountryMeta("France", "🇫🇷", "France", "French"),
        CountryMeta("United Kingdom", "🇬🇧", "United Kingdom", "English"),
        CountryMeta("Spain", "🇪🇸", "Spain", "Spanish"),
        CountryMeta("Germany", "🇩🇪", "Germany", "German"),
        CountryMeta("Nigeria", "🇳🇬", "Nigeria", "English / Yoruba"),
        CountryMeta("Brazil", "🇧🇷", "Brazil", "Portuguese"),
        CountryMeta("Turkey", "🇹🇷", "Turkey", "Turkish"),
        CountryMeta("Italy", "🇮🇹", "Italy", "Italian"),
        CountryMeta("Mexico", "🇲🇽", "Mexico", "Spanish"),
        CountryMeta("Sweden", "🇸🇪", "Sweden", "Swedish"),
        CountryMeta("Thailand", "🇹🇭", "Thailand", "Thai")
    )

    val globalLanguages = listOf(
        "English", "Korean", "Hindi", "Japanese", "Spanish", "French", 
        "German", "Portuguese", "Turkish", "Italian", "Telugu", "Tamil", 
        "Arabic", "Mandarin", "Swedish", "Thai", "Malayalam", "Yoruba", "Russian"
    )

    val genreList = listOf(
        GenreMeta("Action", "⚡", 0xFF00E5FF, listOf("High Octane", "Martial Arts", "Heist", "Superhero", "Car Chases")),
        GenreMeta("Sci-Fi", "🚀", 0xFFA855F7, listOf("Cyberpunk", "Space Opera", "Time Travel", "AI Dystopia", "Post-Apocalyptic")),
        GenreMeta("K-Drama", "💖", 0xFFFF2A85, listOf("Romantic Comedy", "Revenge Thriller", "Chaebol Romance", "Historical Sageuk", "Slice of Life")),
        GenreMeta("Anime", "🌸", 0xFFFF6400, listOf("Shonen", "Isekai", "Seinen", "Mecha", "Supernatural")),
        GenreMeta("Bollywood", "✨", 0xFFFFB800, listOf("Masala Blockbuster", "Romantic Musical", "Historical Epic", "Action Drama", "Family Saga")),
        GenreMeta("Thriller", "👁️", 0xFFFF3D71, listOf("Psychological", "Neo-Noir", "Crime Mystery", "Scandinavian Noir", "Conspiracy")),
        GenreMeta("Drama", "🎭", 0xFF3B82F6, listOf("Prestige Drama", "Courtroom", "Family Tragedy", "Biopic", "Social Realism")),
        GenreMeta("Fantasy", "🔮", 0xFF8B5CF6, listOf("High Fantasy", "Mythology", "Urban Fantasy", "Dark Fantasy", "Magical Realism")),
        GenreMeta("Horror", "🩸", 0xFFEF4444, listOf("Supernatural", "Psychological Horror", "Folk Horror", "Cosmic Horror", "Survival")),
        GenreMeta("Comedy", "😂", 0xFF10B981, listOf("Dark Comedy", "Satire", "Slapstick", "Parody", "Romantic Comedy")),
        GenreMeta("Documentary", "🌍", 0xFF14B8A6, listOf("True Crime", "Nature & Wildlife", "Tech & Science", "Music History", "Space")),
        GenreMeta("Romance", "🌹", 0xFFF43F5E, listOf("Slow Burn", "Enemies to Lovers", "Forbidden Love", "Period Romance"))
    )

    private val masterCatalog: List<MediaItem> = listOf(
        // 1. Oppenheimer
        MediaItem(
            id = "mv_oppenheimer_01",
            title = "Oppenheimer",
            originalTitle = "Oppenheimer",
            overview = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during the Manhattan Project, exploring the moral and scientific turbulence that changed world history forever.",
            posterUrl = "https://image.tmdb.org/t/p/w780/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg",
            localBackdropRes = R.drawable.img_hero_cyberpunk,
            mediaType = MediaType.MOVIE,
            genres = listOf("Drama", "History", "Biopic"),
            subGenres = listOf("Prestige Drama", "Historical Epic", "Biopic"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi", "Italian"),
            releaseYear = 2023,
            rating = 8.9,
            ratingCount = "780K",
            ageRating = "R",
            quality = "4K IMAX Enhanced",
            duration = "3h 00m",
            matchScore = 99,
            streamingPlatforms = listOf(prime, max, apple),
            cast = listOf(
                CastMember("Cillian Murphy", "J. Robert Oppenheimer", "Ireland"),
                CastMember("Emily Blunt", "Katherine 'Kitty' Oppenheimer", "UK"),
                CastMember("Matt Damon", "Leslie Groves", "USA"),
                CastMember("Robert Downey Jr.", "Lewis Strauss", "USA"),
                CastMember("Florence Pugh", "Jean Tatlock", "UK")
            ),
            director = "Christopher Nolan",
            tagline = "Now I am become Death, the destroyer of worlds.",
            imdbId = "tt15398776",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = true,
            isTop10 = true,
            trendingRank = 1
        ),

        // 2. Dune: Part Two
        MediaItem(
            id = "mv_dune2_02",
            title = "Dune: Part Two",
            originalTitle = "Dune: Part Two",
            overview = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family. Facing a choice between the love of his life and the fate of the universe, he endeavors to prevent a terrible future only he can foresee.",
            posterUrl = "https://image.tmdb.org/t/p/w780/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/xOMo8BRK7PfcJv9JCnx7s5200bm.jpg",
            localBackdropRes = R.drawable.img_hero_fantasy,
            mediaType = MediaType.MOVIE,
            genres = listOf("Sci-Fi", "Action", "Adventure"),
            subGenres = listOf("Space Opera", "Epic Worldbuilding", "High Stakes"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi", "Arabic"),
            releaseYear = 2024,
            rating = 8.6,
            ratingCount = "560K",
            ageRating = "PG-13",
            quality = "4K Dolby Vision",
            duration = "2h 46m",
            matchScore = 98,
            streamingPlatforms = listOf(max, prime, apple),
            cast = listOf(
                CastMember("Timothée Chalamet", "Paul Atreides", "USA"),
                CastMember("Zendaya", "Chani", "USA"),
                CastMember("Rebecca Ferguson", "Lady Jessica", "Sweden"),
                CastMember("Javier Bardem", "Stilgar", "Spain"),
                CastMember("Austin Butler", "Feyd-Rautha", "USA")
            ),
            director = "Denis Villeneuve",
            tagline = "Long live the fighters.",
            imdbId = "tt15239678",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = true,
            isTop10 = true,
            trendingRank = 2
        ),

        // 3. Interstellar
        MediaItem(
            id = "mv_interstellar_03",
            title = "Interstellar",
            originalTitle = "Interstellar",
            overview = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans across a mysterious wormhole near Saturn.",
            posterUrl = "https://image.tmdb.org/t/p/w780/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/rAiYTsqBk79zqjHkivvi8Aq0nPk.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Sci-Fi", "Drama", "Adventure"),
            subGenres = listOf("Space Opera", "Time Travel", "Prestige Drama"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Portuguese", "Japanese", "Korean", "Hindi"),
            releaseYear = 2014,
            rating = 8.7,
            ratingCount = "2.1M",
            ageRating = "PG-13",
            quality = "4K IMAX Enhanced",
            duration = "2h 49m",
            matchScore = 99,
            streamingPlatforms = listOf(prime, apple, max),
            cast = listOf(
                CastMember("Matthew McConaughey", "Cooper", "USA"),
                CastMember("Anne Hathaway", "Brand", "USA"),
                CastMember("Jessica Chastain", "Murph", "USA"),
                CastMember("Michael Caine", "Professor Brand", "UK")
            ),
            director = "Christopher Nolan",
            tagline = "Mankind was born on Earth. It was never meant to die here.",
            imdbId = "tt0816692",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isFeaturedHero = true,
            isTop10 = true,
            trendingRank = 3
        ),

        // 4. Stranger Things
        MediaItem(
            id = "tv_stranger_things_04",
            title = "Stranger Things",
            originalTitle = "Stranger Things",
            overview = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces and one strange little girl with telekinetic powers in Hawkins, Indiana.",
            posterUrl = "https://image.tmdb.org/t/p/w780/49WJfeN0moxb9IPfGn8AIqMGskD.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/56v2KjBlU4XaOv9rVYEQypROD7P.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Sci-Fi", "Horror", "Drama"),
            subGenres = listOf("Supernatural", "80s Nostalgia", "Mystery"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2022,
            rating = 8.7,
            ratingCount = "1.4M",
            ageRating = "TV-14",
            quality = "4K Dolby Vision",
            duration = "4 Seasons / 34 Ep",
            matchScore = 97,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Millie Bobby Brown", "Eleven", "UK"),
                CastMember("Finn Wolfhard", "Mike Wheeler", "Canada"),
                CastMember("Winona Ryder", "Joyce Byers", "USA"),
                CastMember("David Harbour", "Jim Hopper", "USA")
            ),
            director = "The Duffer Brothers",
            tagline = "Every ending has a beginning.",
            imdbId = "tt4574334",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 4
        ),

        // 5. Game of Thrones
        MediaItem(
            id = "tv_got_05",
            title = "Game of Thrones",
            originalTitle = "Game of Thrones",
            overview = "Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia.",
            posterUrl = "https://image.tmdb.org/t/p/w780/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/2OMB0ynKlyIenMJWI2Dy9IWT4c.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Fantasy", "Drama", "Action"),
            subGenres = listOf("High Fantasy", "Prestige Drama", "Medieval"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Japanese", "Russian"),
            releaseYear = 2019,
            rating = 9.2,
            ratingCount = "2.3M",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "8 Seasons / 73 Ep",
            matchScore = 98,
            streamingPlatforms = listOf(max),
            cast = listOf(
                CastMember("Emilia Clarke", "Daenerys Targaryen", "UK"),
                CastMember("Kit Harington", "Jon Snow", "UK"),
                CastMember("Peter Dinklage", "Tyrion Lannister", "USA"),
                CastMember("Lena Headey", "Cersei Lannister", "UK")
            ),
            director = "David Benioff & D.B. Weiss",
            tagline = "Winter is Coming.",
            imdbId = "tt0944947",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 5
        ),

        // 6. Breaking Bad
        MediaItem(
            id = "tv_breaking_bad_06",
            title = "Breaking Bad",
            originalTitle = "Breaking Bad",
            overview = "A chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine with a former student in order to secure his family's financial future.",
            posterUrl = "https://image.tmdb.org/t/p/w780/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Crime", "Drama", "Thriller"),
            subGenres = listOf("Neo-Noir", "Prestige Drama", "Psychological"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Portuguese", "Japanese"),
            releaseYear = 2013,
            rating = 9.5,
            ratingCount = "2.2M",
            ageRating = "TV-MA",
            quality = "4K UHD",
            duration = "5 Seasons / 62 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Bryan Cranston", "Walter White", "USA"),
                CastMember("Aaron Paul", "Jesse Pinkman", "USA"),
                CastMember("Anna Gunn", "Skyler White", "USA"),
                CastMember("Giancarlo Esposito", "Gus Fring", "USA")
            ),
            director = "Vince Gilligan",
            tagline = "All bad things must come to an end.",
            imdbId = "tt0903747",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 6
        ),

        // 7. Squid Game (K-Drama)
        MediaItem(
            id = "kd_squid_game_07",
            title = "Squid Game",
            originalTitle = "오징어 게임",
            overview = "Hundreds of cash-strapped players accept a strange invitation to compete in children's games. Inside, a tempting prize awaits with deadly high stakes across neon-lit pastel arenas.",
            posterUrl = "https://image.tmdb.org/t/p/w780/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/2meX1nMdScFOoV4370rqHWFDxJn.jpg",
            localBackdropRes = R.drawable.img_hero_kdrama,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("K-Drama", "Thriller", "Drama", "Mystery"),
            subGenres = listOf("Survival", "Psychological Thriller", "Social Realism"),
            country = "South Korea",
            countryFlag = "🇰🇷",
            originalLanguage = "Korean",
            audioLanguages = listOf("Korean (Original 5.1)", "English Dub (5.1)", "Spanish (5.1)", "French (5.1)", "Hindi (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Korean", "Spanish", "French", "German", "Japanese", "Thai", "Hindi"),
            releaseYear = 2024,
            rating = 8.0,
            ratingCount = "640K",
            ageRating = "TV-MA",
            quality = "4K HDR10+",
            duration = "2 Seasons / 16 Ep",
            matchScore = 98,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Lee Jung-jae", "Seong Gi-hun", "South Korea"),
                CastMember("Park Hae-soo", "Cho Sang-woo", "South Korea"),
                CastMember("Jung Ho-yeon", "Kang Sae-byeok", "South Korea"),
                CastMember("Wi Ha-joon", "Hwang Jun-ho", "South Korea")
            ),
            director = "Hwang Dong-hyuk",
            tagline = "45.6 Billion Won is Child's Play.",
            imdbId = "tt10919380",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 7
        ),

        // 8. Jujutsu Kaisen (Anime)
        MediaItem(
            id = "an_jujutsu_kaisen_08",
            title = "Jujutsu Kaisen",
            originalTitle = "呪術廻戦",
            overview = "A boy swallows a cursed talisman - the finger of a demon - and becomes cursed himself. He enters a shaman's school to be able to locate the demon's other body parts and exorcise himself.",
            posterUrl = "https://image.tmdb.org/t/p/w780/hFWP5HkbVEe30rrXZSpJIL4pndD.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/7c4f4JkGk1z1sUfW7s9oF07eEim.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Action", "Fantasy", "Supernatural"),
            subGenres = listOf("Shonen", "Dark Fantasy", "Martial Arts"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original)", "English Dub", "Spanish Dub", "Portuguese Dub", "German Dub", "Hindi Dub"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Spanish", "French", "German", "Portuguese", "Hindi", "Indonesian"),
            releaseYear = 2023,
            rating = 8.6,
            ratingCount = "390K",
            ageRating = "TV-MA",
            quality = "4K UHD 60FPS",
            duration = "2 Seasons / 47 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(crunchyroll, netflix),
            cast = listOf(
                CastMember("Junya Enoki", "Yuji Itadori (Voice)", "Japan"),
                CastMember("Yuma Uchida", "Megumi Fushiguro (Voice)", "Japan"),
                CastMember("Yuichi Nakamura", "Satoru Gojo (Voice)", "Japan")
            ),
            director = "Sunghoo Park & Shota Goshozono (MAPPA)",
            tagline = "Curse the curse to save humanity.",
            imdbId = "tt12343534",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 8
        ),

        // 9. Attack on Titan (Anime)
        MediaItem(
            id = "an_aot_09",
            title = "Attack on Titan",
            originalTitle = "進撃の巨人",
            overview = "After his hometown is destroyed and his mother is killed, young Eren Jaeger vows to cleanse the earth of the giant humanoid Titans that have brought humanity to the brink of extinction.",
            posterUrl = "https://image.tmdb.org/t/p/w780/hTP1DtLGFamjfu8WqjnuQdP1n4i.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/yGNqaFq5s1zQ0wVpB6yT3O7xVqf.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Action", "Drama", "Fantasy"),
            subGenres = listOf("Dark Fantasy", "Military", "Post-Apocalyptic"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original)", "English Dub", "Spanish Dub", "German Dub", "French Dub"),
            subtitleLanguages = listOf("English", "Japanese", "Spanish", "French", "German", "Portuguese", "Korean", "Hindi"),
            releaseYear = 2023,
            rating = 9.1,
            ratingCount = "580K",
            ageRating = "TV-MA",
            quality = "4K UHD",
            duration = "4 Seasons / 89 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(crunchyroll, netflix, prime),
            cast = listOf(
                CastMember("Yuki Kaji", "Eren Jaeger (Voice)", "Japan"),
                CastMember("Yui Ishikawa", "Mikasa Ackerman (Voice)", "Japan"),
                CastMember("Hiroshi Kamiya", "Levi Ackerman (Voice)", "Japan")
            ),
            director = "Tetsuro Araki & Yuichiro Hayashi (WIT & MAPPA)",
            tagline = "If you don't fight, you can't win.",
            imdbId = "tt2560140",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 9
        ),

        // 10. RRR (Indian Blockbuster)
        MediaItem(
            id = "in_rrr_10",
            title = "RRR",
            originalTitle = "రౌద్రం రణం రుధిరం",
            overview = "A fictitious story about two legendary revolutionaries and their journey away from home before they started fighting for their country in 1920s British-occupied India.",
            posterUrl = "https://image.tmdb.org/t/p/w780/wE0I6efAW4cDDmZQWtwZMOW44EJ.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/22z44LPkMyf5nyySNz9nNgs1Xzp.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Bollywood", "Action", "Drama"),
            subGenres = listOf("Masala Blockbuster", "Historical Epic", "Action Drama"),
            country = "India",
            countryFlag = "🇮🇳",
            originalLanguage = "Telugu",
            audioLanguages = listOf("Telugu (Original)", "Hindi (5.1)", "Tamil (5.1)", "Malayalam (5.1)", "Kannada (5.1)", "English (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Hindi", "Spanish", "French", "German", "Japanese"),
            releaseYear = 2022,
            rating = 7.8,
            ratingCount = "190K",
            ageRating = "PG-13",
            quality = "4K Dolby Atmos",
            duration = "3h 07m",
            matchScore = 97,
            streamingPlatforms = listOf(netflix, prime),
            cast = listOf(
                CastMember("N.T. Rama Rao Jr.", "Komaram Bheem", "India"),
                CastMember("Ram Charan", "Alluri Sitarama Raju", "India"),
                CastMember("Ajay Devgn", "Venkata Rama Raju", "India"),
                CastMember("Alia Bhatt", "Sita", "India")
            ),
            director = "S. S. Rajamouli",
            tagline = "Rise. Roar. Revolt.",
            imdbId = "tt8178634",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = true,
            trendingRank = 10
        ),

        // 11. The Batman
        MediaItem(
            id = "mv_batman_11",
            title = "The Batman",
            originalTitle = "The Batman",
            overview = "When a sadistic serial killer begins murdering key political figures in Gotham, Batman is forced to investigate the city's hidden corruption and question his family's involvement.",
            posterUrl = "https://image.tmdb.org/t/p/w780/74xTEgt7R36Fpooo50r9T25onhq.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Action", "Crime", "Drama", "Mystery"),
            subGenres = listOf("Neo-Noir", "Detective", "Psychological Thriller"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Japanese", "Hindi"),
            releaseYear = 2022,
            rating = 7.8,
            ratingCount = "760K",
            ageRating = "PG-13",
            quality = "4K Dolby Vision",
            duration = "2h 56m",
            matchScore = 96,
            streamingPlatforms = listOf(max, prime),
            cast = listOf(
                CastMember("Robert Pattinson", "Bruce Wayne / The Batman", "UK"),
                CastMember("Zoë Kravitz", "Selina Kyle / Catwoman", "USA"),
                CastMember("Paul Dano", "The Riddler", "USA"),
                CastMember("Colin Farrell", "Oz Cobblepot / The Penguin", "Ireland")
            ),
            director = "Matt Reeves",
            tagline = "Unmask the truth.",
            imdbId = "tt1877830",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 12. Spider-Man: Across the Spider-Verse
        MediaItem(
            id = "mv_spiderverse_12",
            title = "Spider-Man: Across the Spider-Verse",
            originalTitle = "Spider-Man: Across the Spider-Verse",
            overview = "Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence. When the heroes clash on how to handle a new threat, Miles must redefine what it means to be a hero.",
            posterUrl = "https://image.tmdb.org/t/p/w780/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/4HodYYKEIsGOdinkGi2Ucz6X9i0.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Anime", "Action", "Adventure", "Sci-Fi"),
            subGenres = listOf("Multiverse", "Superhero", "Animation Masterpiece"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2023,
            rating = 8.6,
            ratingCount = "400K",
            ageRating = "PG",
            quality = "4K Dolby Vision",
            duration = "2h 20m",
            matchScore = 99,
            streamingPlatforms = listOf(netflix, prime, apple),
            cast = listOf(
                CastMember("Shameik Moore", "Miles Morales (Voice)", "USA"),
                CastMember("Hailee Steinfeld", "Gwen Stacy (Voice)", "USA"),
                CastMember("Oscar Isaac", "Miguel O'Hara (Voice)", "Guatemala")
            ),
            director = "Joaquim Dos Santos, Kemp Powers & Justin K. Thompson",
            tagline = "It's how you wear the mask that matters.",
            imdbId = "tt9362722",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 13. Parasite (Korean Masterpiece)
        MediaItem(
            id = "kd_parasite_13",
            title = "Parasite",
            originalTitle = "기생충",
            overview = "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
            posterUrl = "https://image.tmdb.org/t/p/w780/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/hiKmpZMGZsrkA3cdFiye9L7UmPn.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("K-Drama", "Drama", "Thriller", "Comedy"),
            subGenres = listOf("Black Comedy", "Psychological Thriller", "Social Realism"),
            country = "South Korea",
            countryFlag = "🇰🇷",
            originalLanguage = "Korean",
            audioLanguages = listOf("Korean (Original 5.1)", "English (5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Korean", "Spanish", "French", "German", "Japanese", "Hindi"),
            releaseYear = 2019,
            rating = 8.5,
            ratingCount = "950K",
            ageRating = "R",
            quality = "4K UHD Remaster",
            duration = "2h 12m",
            matchScore = 99,
            streamingPlatforms = listOf(max, prime),
            cast = listOf(
                CastMember("Song Kang-ho", "Kim Ki-taek", "South Korea"),
                CastMember("Lee Sun-kyun", "Park Dong-ik", "South Korea"),
                CastMember("Cho Yeo-jeong", "Park Yeon-gyo", "South Korea"),
                CastMember("Choi Woo-shik", "Kim Ki-woo", "South Korea")
            ),
            director = "Bong Joon-ho",
            tagline = "Act like you own the place.",
            imdbId = "tt6751668",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 14. Inception
        MediaItem(
            id = "mv_inception_14",
            title = "Inception",
            originalTitle = "Inception",
            overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
            posterUrl = "https://image.tmdb.org/t/p/w780/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/8ZTVqvKDQ8emSGUEMjsS4yHAwrp.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Sci-Fi", "Action", "Adventure", "Thriller"),
            subGenres = listOf("Heist", "Mind Bending", "Psychological"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2010,
            rating = 8.8,
            ratingCount = "2.6M",
            ageRating = "PG-13",
            quality = "4K UHD",
            duration = "2h 28m",
            matchScore = 98,
            streamingPlatforms = listOf(max, prime, apple),
            cast = listOf(
                CastMember("Leonardo DiCaprio", "Cobb", "USA"),
                CastMember("Joseph Gordon-Levitt", "Arthur", "USA"),
                CastMember("Elliot Page", "Ariadne", "Canada"),
                CastMember("Tom Hardy", "Eames", "UK"),
                CastMember("Ken Watanabe", "Saito", "Japan")
            ),
            director = "Christopher Nolan",
            tagline = "Your mind is the scene of the crime.",
            imdbId = "tt1375666",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 15. Money Heist (La Casa de Papel)
        MediaItem(
            id = "es_money_heist_15",
            title = "Money Heist",
            originalTitle = "La Casa de Papel",
            overview = "An unusual group of robbers attempt to carry out the most perfect robbery in Spanish history - stealing 2.4 billion euros from the Royal Mint of Spain under the mastermind guidance of The Professor.",
            posterUrl = "https://image.tmdb.org/t/p/w780/reEMJA1uzscCbk5r6Rg1Za6OvtK.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/gFZri2YbFUzgEgnY79PDIzgJmAL.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Action", "Crime", "Drama", "Thriller"),
            subGenres = listOf("Heist", "High Stakes", "Psychological Thriller"),
            country = "Spain",
            countryFlag = "🇪🇸",
            originalLanguage = "Spanish",
            audioLanguages = listOf("Spanish (Original 5.1)", "English (5.1)", "French (5.1)", "German (5.1)", "Portuguese (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Portuguese", "Korean", "Arabic"),
            releaseYear = 2021,
            rating = 8.2,
            ratingCount = "540K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "5 Seasons / 48 Ep",
            matchScore = 97,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Álvaro Morte", "The Professor", "Spain"),
                CastMember("Úrsula Corberó", "Tokyo", "Spain"),
                CastMember("Pedro Alonso", "Berlin", "Spain"),
                CastMember("Itziar Ituño", "Raquel Murillo", "Spain")
            ),
            director = "Álex Pina",
            tagline = "The rebellion begins.",
            imdbId = "tt6468322",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 16. The Dark Knight
        MediaItem(
            id = "mv_dark_knight_16",
            title = "The Dark Knight",
            originalTitle = "The Dark Knight",
            overview = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            posterUrl = "https://image.tmdb.org/t/p/w780/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/dqK9Hag1054tghRQSqLSfrkvQnA.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Action", "Crime", "Drama", "Thriller"),
            subGenres = listOf("Superhero", "Neo-Noir", "Psychological Thriller"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Japanese", "Hindi"),
            releaseYear = 2008,
            rating = 9.0,
            ratingCount = "2.9M",
            ageRating = "PG-13",
            quality = "4K IMAX Enhanced",
            duration = "2h 32m",
            matchScore = 99,
            streamingPlatforms = listOf(max, prime, apple),
            cast = listOf(
                CastMember("Christian Bale", "Bruce Wayne / Batman", "UK"),
                CastMember("Heath Ledger", "Joker", "Australia"),
                CastMember("Aaron Eckhart", "Harvey Dent", "USA"),
                CastMember("Michael Caine", "Alfred", "UK"),
                CastMember("Gary Oldman", "Jim Gordon", "UK")
            ),
            director = "Christopher Nolan",
            tagline = "Why so serious?",
            imdbId = "tt0468569",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 17. Shōgun (2024)
        MediaItem(
            id = "tv_shogun_17",
            title = "Shōgun",
            originalTitle = "Shōgun",
            overview = "When a mysterious European ship is found marooned in a nearby fishing village, Lord Yoshii Toranaga discovers secrets that could tip the scales of power and devastate his formidable enemies in feudal Japan.",
            posterUrl = "https://image.tmdb.org/t/p/w780/7O4iVfOMQmdCSxhOg1WNzG1AgYT.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/5zmiBoMzeV4P0ayQ6hoB1v4Xv.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Drama", "History", "Action", "Adventure"),
            subGenres = listOf("Feudal Japan", "Political Intrigue", "Samurai Epic"),
            country = "United States",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original 5.1)", "English (5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Spanish", "French", "German", "Korean", "Hindi"),
            releaseYear = 2024,
            rating = 8.8,
            ratingCount = "240K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "1 Season / 10 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(disney, prime),
            cast = listOf(
                CastMember("Hiroyuki Sanada", "Lord Yoshii Toranaga", "Japan"),
                CastMember("Cosmo Jarvis", "John Blackthorne", "UK"),
                CastMember("Anna Sawai", "Toda Mariko", "Japan"),
                CastMember("Tadanobu Asano", "Kashigi Yabushige", "Japan")
            ),
            director = "Rachel Kondo & Justin Marks",
            tagline = "Destiny is a matter of choice.",
            imdbId = "tt2788310",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 18. Arcane
        MediaItem(
            id = "an_arcane_18",
            title = "Arcane",
            originalTitle = "Arcane: League of Legends",
            overview = "Set in the utopian region of Piltover and the oppressed underground of Zaun, the story follows the origins of two iconic League champions-and the power that will tear them apart.",
            posterUrl = "https://image.tmdb.org/t/p/w780/fqldf2t8ztc9aiwn396mlX3Yq1m.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/vI0FpZ4M0y0vC7P0Z0W7l8s8s0.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Action", "Sci-Fi", "Drama", "Fantasy"),
            subGenres = listOf("Steampunk", "Cyberpunk", "Animation Masterpiece"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "French (5.1)", "Spanish (5.1)", "Japanese (5.1)", "German (5.1)", "Korean (5.1)"),
            subtitleLanguages = listOf("English [CC]", "French", "Spanish", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2024,
            rating = 9.0,
            ratingCount = "380K",
            ageRating = "TV-14",
            quality = "4K Dolby Vision HDR",
            duration = "2 Seasons / 18 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Hailee Steinfeld", "Vi (Voice)", "USA"),
                CastMember("Ella Purnell", "Jinx (Voice)", "UK"),
                CastMember("Katie Leung", "Caitlyn (Voice)", "UK"),
                CastMember("Kevin Alejandro", "Jayce (Voice)", "USA")
            ),
            director = "Christian Linke & Alex Yee (Fortiche)",
            tagline = "Every legend has a beginning.",
            imdbId = "tt11126994",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 19. Demon Slayer
        MediaItem(
            id = "an_demon_slayer_19",
            title = "Demon Slayer: Kimetsu no Yaiba",
            originalTitle = "鬼滅の刃",
            overview = "A family is attacked by demons and only two members survive - Tanjiro and his sister Nezuko, who is turning into a demon slowly. Tanjiro sets out to become a demon slayer to avenge his family and cure his sister.",
            posterUrl = "https://image.tmdb.org/t/p/w780/xUfRZu2mi8jH6SzQEJGP6tjBuYj.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/nTvM4mhqZlHIvUkI1gVnWumUM4P.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Action", "Fantasy", "Supernatural"),
            subGenres = listOf("Shonen", "Dark Fantasy", "Martial Arts", "Swordplay"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original)", "English Dub (5.1)", "Spanish Dub", "Portuguese Dub", "German Dub", "Hindi Dub"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Spanish", "French", "German", "Portuguese", "Hindi"),
            releaseYear = 2024,
            rating = 8.7,
            ratingCount = "290K",
            ageRating = "TV-MA",
            quality = "4K UHD 60FPS",
            duration = "4 Seasons / 63 Ep",
            matchScore = 98,
            streamingPlatforms = listOf(crunchyroll, netflix),
            cast = listOf(
                CastMember("Natsuki Hanae", "Tanjiro Kamado (Voice)", "Japan"),
                CastMember("Akari Kito", "Nezuko Kamado (Voice)", "Japan"),
                CastMember("Hiro Shimono", "Zenitsu Agatsuma (Voice)", "Japan"),
                CastMember("Yoshitsugu Matsuoka", "Inosuke Hashibira (Voice)", "Japan")
            ),
            director = "Haruo Sotozaki (ufotable)",
            tagline = "Set your heart ablaze.",
            imdbId = "tt9335498",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 20. Wednesday
        MediaItem(
            id = "tv_wednesday_20",
            title = "Wednesday",
            originalTitle = "Wednesday",
            overview = "Follows Wednesday Addams' years as a student at Nevermore Academy, attempting to master her emerging psychic ability, thwart a monstrous killing spree, and solve the supernatural mystery that embroiled her parents 25 years ago.",
            posterUrl = "https://image.tmdb.org/t/p/w780/9PFonBhy4cQy7Jz20NpMygczOkv.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/iHSwvFe7FdFFRmEN09579umdA4.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Comedy", "Fantasy", "Mystery", "Horror"),
            subGenres = listOf("Supernatural", "Gothic Mystery", "Teen Drama"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Japanese", "Korean", "Hindi"),
            releaseYear = 2022,
            rating = 8.1,
            ratingCount = "430K",
            ageRating = "TV-14",
            quality = "4K Dolby Vision",
            duration = "1 Season / 8 Ep",
            matchScore = 96,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Jenna Ortega", "Wednesday Addams", "USA"),
                CastMember("Gwendoline Christie", "Principal Larissa Weems", "UK"),
                CastMember("Riki Lindhome", "Dr. Valerie Kinbott", "USA"),
                CastMember("Christina Ricci", "Marilyn Thornhill", "USA")
            ),
            director = "Tim Burton, James Marshall & Gandja Monteiro",
            tagline = "Smart, sarcastic and a little dead inside.",
            imdbId = "tt13443470",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 21. Spirited Away (Studio Ghibli)
        MediaItem(
            id = "an_spirited_away_21",
            title = "Spirited Away",
            originalTitle = "千と千尋の神隠し",
            overview = "During her family's move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches, and spirits, and where humans are changed into beasts.",
            posterUrl = "https://image.tmdb.org/t/p/w780/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/Ab8mkHmkYADjU7wQiOkia99GQI.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Fantasy", "Adventure", "Family"),
            subGenres = listOf("Studio Ghibli", "Supernatural", "Coming of Age"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original 5.1)", "English Dub (5.1)", "French (5.1)", "Spanish (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Spanish", "French", "German", "Korean", "Hindi"),
            releaseYear = 2001,
            rating = 8.6,
            ratingCount = "860K",
            ageRating = "PG",
            quality = "4K Remaster",
            duration = "2h 05m",
            matchScore = 99,
            streamingPlatforms = listOf(max, netflix),
            cast = listOf(
                CastMember("Rumi Hiiragi", "Chihiro / Sen (Voice)", "Japan"),
                CastMember("Miyu Irino", "Haku (Voice)", "Japan"),
                CastMember("Mari Natsuki", "Yubaba / Zeniba (Voice)", "Japan")
            ),
            director = "Hayao Miyazaki",
            tagline = "Nothing that happens is ever forgotten, even if you can't remember.",
            imdbId = "tt0245429",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 22. Your Name (Anime Masterpiece)
        MediaItem(
            id = "an_your_name_22",
            title = "Your Name.",
            originalTitle = "君の名は。",
            overview = "Two strangers find themselves linked in a bizarre way. When a connection forms, will distance be the only thing to keep them apart across time and cosmic wonders?",
            posterUrl = "https://image.tmdb.org/t/p/w780/q719jXXEzOoYaps6qFsRHgvwwsl.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/dIWwZWOPHowMNHKZsagUUZNag92.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Romance", "Fantasy", "Drama"),
            subGenres = listOf("Body Swap", "Time Travel", "Emotional Masterpiece"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original 5.1)", "English Dub", "Spanish Dub", "French Dub", "German Dub"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Spanish", "French", "German", "Korean", "Hindi"),
            releaseYear = 2016,
            rating = 8.4,
            ratingCount = "350K",
            ageRating = "PG",
            quality = "4K Dolby Vision",
            duration = "1h 46m",
            matchScore = 98,
            streamingPlatforms = listOf(crunchyroll, prime),
            cast = listOf(
                CastMember("Ryunosuke Kamiki", "Taki Tachibana (Voice)", "Japan"),
                CastMember("Mone Kamishiraishi", "Mitsuha Miyamizu (Voice)", "Japan")
            ),
            director = "Makoto Shinkai",
            tagline = "Treasure the experience. Dreams fade away after you wake up.",
            imdbId = "tt5311514",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 23. Gladiator II
        MediaItem(
            id = "mv_gladiator2_23",
            title = "Gladiator II",
            originalTitle = "Gladiator II",
            overview = "Years after witnessing the death of Maximus at the hands of his uncle, Lucius must enter the Colosseum after the powerful emperors of Rome conquer his home.",
            posterUrl = "https://image.tmdb.org/t/p/w780/2cxhvwyEwRlysAmRH4iodkvo0z5.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/euYIWhBvdaEvW02kQoyz2b5xUfG.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Action", "Drama", "Adventure", "History"),
            subGenres = listOf("Roman Empire", "Gladiators", "Historical Epic"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Japanese", "Hindi"),
            releaseYear = 2024,
            rating = 8.1,
            ratingCount = "180K",
            ageRating = "R",
            quality = "4K Dolby Vision",
            duration = "2h 28m",
            matchScore = 97,
            streamingPlatforms = listOf(paramount, prime, apple),
            cast = listOf(
                CastMember("Paul Mescal", "Lucius", "Ireland"),
                CastMember("Pedro Pascal", "Marcus Acacius", "Chile"),
                CastMember("Denzel Washington", "Macrinus", "USA"),
                CastMember("Connie Nielsen", "Lucilla", "Denmark")
            ),
            director = "Ridley Scott",
            tagline = "Strength and honor shall rise again.",
            imdbId = "tt9614460",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 24. Avatar: The Way of Water
        MediaItem(
            id = "mv_avatar2_24",
            title = "Avatar: The Way of Water",
            originalTitle = "Avatar: The Way of Water",
            overview = "Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns to finish what was previously started, Jake must work with Neytiri and the army of the Na'vi race to protect their home.",
            posterUrl = "https://image.tmdb.org/t/p/w780/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/s16H6tpK2utvwDtzZ8Qy4qm5Emw.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Action", "Adventure", "Fantasy", "Sci-Fi"),
            subGenres = listOf("Alien Worlds", "Underwater Cinema", "Visual Spectacle"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2022,
            rating = 7.6,
            ratingCount = "520K",
            ageRating = "PG-13",
            quality = "4K 3D HFR Atmos",
            duration = "3h 12m",
            matchScore = 96,
            streamingPlatforms = listOf(disney, max, prime),
            cast = listOf(
                CastMember("Sam Worthington", "Jake Sully", "Australia"),
                CastMember("Zoe Saldana", "Neytiri", "USA"),
                CastMember("Sigourney Weaver", "Kiri", "USA"),
                CastMember("Stephen Lang", "Miles Quaritch", "USA"),
                CastMember("Kate Winslet", "Ronal", "UK")
            ),
            director = "James Cameron",
            tagline = "Return to Pandora.",
            imdbId = "tt1630029",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 25. The Conjuring (Horror Masterpiece)
        MediaItem(
            id = "hr_conjuring_25",
            title = "The Conjuring",
            originalTitle = "The Conjuring",
            overview = "Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence in their secluded farmhouse in Harrisville, Rhode Island.",
            posterUrl = "https://image.tmdb.org/t/p/w780/wVYREutTvI2tmxr6ujrHT704wGF.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/m5zsz5tM8sCc0tms08T0FhF8Mgt.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Horror", "Mystery", "Thriller"),
            subGenres = listOf("Supernatural", "Haunted House", "Cosmic Horror"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Hindi"),
            releaseYear = 2013,
            rating = 7.5,
            ratingCount = "480K",
            ageRating = "R",
            quality = "4K Dolby Vision",
            duration = "1h 52m",
            matchScore = 96,
            streamingPlatforms = listOf(max, netflix),
            cast = listOf(
                CastMember("Vera Farmiga", "Lorraine Warren", "USA"),
                CastMember("Patrick Wilson", "Ed Warren", "USA"),
                CastMember("Lili Taylor", "Carolyn Perron", "USA"),
                CastMember("Ron Livingston", "Roger Perron", "USA")
            ),
            director = "James Wan",
            tagline = "Based on the true case files of the Warrens.",
            imdbId = "tt1457767",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 26. A Quiet Place: Day One (Horror / Sci-Fi)
        MediaItem(
            id = "hr_quietplace_26",
            title = "A Quiet Place: Day One",
            originalTitle = "A Quiet Place: Day One",
            overview = "Experience the day the world went quiet. A young woman named Sam must survive an invasion in New York City by bloodthirsty alien creatures with ultrasonic hearing.",
            posterUrl = "https://image.tmdb.org/t/p/w780/yrpPYK2HgVuZBPx9959Z65oepvF.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/2RVcJbWFmICRDsEjRI8oqBTYI52.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Horror", "Sci-Fi", "Thriller"),
            subGenres = listOf("Survival", "Alien Invasion", "Psychological Horror"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2024,
            rating = 7.0,
            ratingCount = "195K",
            ageRating = "PG-13",
            quality = "4K UHD Atmos",
            duration = "1h 39m",
            matchScore = 95,
            streamingPlatforms = listOf(paramount, prime),
            cast = listOf(
                CastMember("Lupita Nyong'o", "Samira 'Sam'", "Kenya"),
                CastMember("Joseph Quinn", "Eric", "UK"),
                CastMember("Alex Wolff", "Reuben", "USA"),
                CastMember("Djimon Hounsou", "Henri", "Benin")
            ),
            director = "Michael Sarnoski",
            tagline = "Hear how it all began.",
            imdbId = "tt13433802",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 27. The Bear (Comedy / Drama)
        MediaItem(
            id = "cm_bear_27",
            title = "The Bear",
            originalTitle = "The Bear",
            overview = "A young chef from the fine dining world comes home to Chicago to run his family's Italian beef sandwich shop after a heartbreaking death, battling intense kitchen chaos and personal demons.",
            posterUrl = "https://image.tmdb.org/t/p/w780/x26SlL28q6v6b8j4R00pC6m8L9i.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/rJpHXv53U6aW0kG2y767y8h8n.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Comedy", "Drama"),
            subGenres = listOf("Dark Comedy", "Satire", "Prestige Drama"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Hindi"),
            releaseYear = 2024,
            rating = 8.6,
            ratingCount = "310K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "3 Seasons / 28 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(disney, prime),
            cast = listOf(
                CastMember("Jeremy Allen White", "Carmen 'Carmy' Berzatto", "USA"),
                CastMember("Ebon Moss-Bachrach", "Richard 'Richie' Jerimovich", "USA"),
                CastMember("Ayo Edebiri", "Sydney Adamu", "USA"),
                CastMember("Lionel Boyce", "Marcus Brooks", "USA")
            ),
            director = "Christopher Storer",
            tagline = "Every second counts.",
            imdbId = "tt14452776",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 28. Deadpool & Wolverine (Action / Comedy)
        MediaItem(
            id = "cm_deadpool_28",
            title = "Deadpool & Wolverine",
            originalTitle = "Deadpool & Wolverine",
            overview = "A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary behind him. But when his homeworld faces an existential threat, Wade must team up with a reluctant Wolverine.",
            posterUrl = "https://image.tmdb.org/t/p/w780/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/yDHYTfA3R0jFYba16jBB1ef8oIt.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Action", "Comedy", "Sci-Fi"),
            subGenres = listOf("Superhero", "Parody", "Slapstick"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2024,
            rating = 7.7,
            ratingCount = "520K",
            ageRating = "R",
            quality = "4K IMAX Enhanced",
            duration = "2h 08m",
            matchScore = 98,
            streamingPlatforms = listOf(disney),
            cast = listOf(
                CastMember("Ryan Reynolds", "Wade Wilson / Deadpool", "Canada"),
                CastMember("Hugh Jackman", "Logan / Wolverine", "Australia"),
                CastMember("Emma Corrin", "Cassandra Nova", "UK"),
                CastMember("Matthew Macfadyen", "Paradox", "UK")
            ),
            director = "Shawn Levy",
            tagline = "Come together.",
            imdbId = "tt6263850",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 29. Planet Earth III (Documentary Masterpiece)
        MediaItem(
            id = "doc_planet_earth_29",
            title = "Planet Earth III",
            originalTitle = "Planet Earth III",
            overview = "Journey to the far reaches of our planet in this spellbinding nature documentary series, discovering astonishing animal behaviors and landscapes during an era of unprecedented change.",
            posterUrl = "https://image.tmdb.org/t/p/w780/z0Gq5Y1qVfVf7tZ6bCjK1m9kK.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/h2m8i976hG0k3x4k56k7n00.jpg",
            localBackdropRes = null,
            mediaType = MediaType.DOCUMENTARY,
            genres = listOf("Documentary"),
            subGenres = listOf("Nature & Wildlife", "Tech & Science", "Earth"),
            country = "United Kingdom",
            countryFlag = "🇬🇧",
            originalLanguage = "English",
            audioLanguages = listOf("English (Sir David Attenborough)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Hindi"),
            releaseYear = 2023,
            rating = 9.3,
            ratingCount = "85K",
            ageRating = "TV-PG",
            quality = "4K UHD HDR 60FPS",
            duration = "1 Season / 8 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(max, prime),
            cast = listOf(
                CastMember("Sir David Attenborough", "Narrator", "UK"),
                CastMember("Hans Zimmer", "Original Score Maestro", "Germany")
            ),
            director = "Matt Brandon & Alastair Fothergill (BBC)",
            tagline = "Our planet like you have never seen it before.",
            imdbId = "tt10878160",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 30. Formula 1: Drive to Survive (Documentary / Sports)
        MediaItem(
            id = "doc_f1_30",
            title = "Formula 1: Drive to Survive",
            originalTitle = "Formula 1: Drive to Survive",
            overview = "Drivers, managers and team owners live life in the fast lane - both on and off the racetrack - during each cutthroat season of Formula 1 racing around the world.",
            posterUrl = "https://image.tmdb.org/t/p/w780/7e3Q5zV7FkC182N5z8p1k8.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/9s12x43k65m8n0p9q128k99.jpg",
            localBackdropRes = null,
            mediaType = MediaType.DOCUMENTARY,
            genres = listOf("Documentary"),
            subGenres = listOf("Tech & Science", "True Crime", "High Stakes"),
            country = "United Kingdom",
            countryFlag = "🇬🇧",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Italian (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Italian", "Japanese", "Portuguese"),
            releaseYear = 2024,
            rating = 8.5,
            ratingCount = "130K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "6 Seasons / 60 Ep",
            matchScore = 97,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Max Verstappen", "Self", "Netherlands"),
                CastMember("Lewis Hamilton", "Self", "UK"),
                CastMember("Toto Wolff", "Self", "Austria"),
                CastMember("Christian Horner", "Self", "UK")
            ),
            director = "James Gay-Rees & Paul Martin",
            tagline = "Speed. Rivalry. Drama.",
            imdbId = "tt8289930",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 31. Past Lives (Romance / Drama Masterpiece)
        MediaItem(
            id = "rm_pastlives_31",
            title = "Past Lives",
            originalTitle = "Past Lives",
            overview = "Nora and Hae Sung, two deeply connected childhood friends, are wrested apart after Nora's family emigrates from South Korea. Two decades later, they are reunited in New York for one fateful week as they confront notions of destiny and love.",
            posterUrl = "https://image.tmdb.org/t/p/w780/k3waqVXSnvCZWfJYNtdamTgTtTA.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/9n2tTRuZaeOTsdJgBP2pnd4wQI5.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Romance", "Drama"),
            subGenres = listOf("Slow Burn", "Period Romance", "Prestige Drama"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English & Korean (Original 5.1)", "Spanish (5.1)", "French (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Korean", "Spanish", "French", "German", "Japanese"),
            releaseYear = 2023,
            rating = 7.9,
            ratingCount = "220K",
            ageRating = "PG-13",
            quality = "4K Dolby Vision",
            duration = "1h 46m",
            matchScore = 98,
            streamingPlatforms = listOf(apple, prime),
            cast = listOf(
                CastMember("Greta Lee", "Nora Moon", "USA"),
                CastMember("Teo Yoo", "Hae Sung", "South Korea"),
                CastMember("John Magaro", "Arthur", "USA")
            ),
            director = "Celine Song",
            tagline = "In-Yun. The providence of souls.",
            imdbId = "tt13238346",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 32. La La Land (Romance / Musical)
        MediaItem(
            id = "rm_lalaland_32",
            title = "La La Land",
            originalTitle = "La La Land",
            overview = "While navigating their careers in Los Angeles, a pianist and an actress fall in love while attempting to reconcile their aspirations for the future with their intoxicating connection.",
            posterUrl = "https://image.tmdb.org/t/p/w780/uDO8zWDhfWwoFdKS4fzkVJb0Rf0.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Romance", "Comedy", "Drama"),
            subGenres = listOf("Romantic Musical", "Slow Burn", "Prestige Drama"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Hindi"),
            releaseYear = 2016,
            rating = 8.0,
            ratingCount = "720K",
            ageRating = "PG-13",
            quality = "4K UHD Remaster",
            duration = "2h 08m",
            matchScore = 97,
            streamingPlatforms = listOf(max, prime),
            cast = listOf(
                CastMember("Ryan Gosling", "Sebastian Wilder", "Canada"),
                CastMember("Emma Stone", "Mia Dolan", "USA"),
                CastMember("John Legend", "Keith", "USA"),
                CastMember("J.K. Simmons", "Bill", "USA")
            ),
            director = "Damien Chazelle",
            tagline = "Here's to the fools who dream.",
            imdbId = "tt3783958",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 33. Queen of Tears (K-Drama Romance)
        MediaItem(
            id = "kd_queen_tears_33",
            title = "Queen of Tears",
            originalTitle = "눈물의 여왕",
            overview = "The queen of department stores and the prince of supermarkets weather a marital crisis - until love miraculously begins to bloom again amidst high society intrigue.",
            posterUrl = "https://image.tmdb.org/t/p/w780/2vP9kHw9dY54G2Z1q8q7e9w7e5.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/9o8k4j78x9m2a1s0k49f783.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("K-Drama", "Romance", "Drama", "Comedy"),
            subGenres = listOf("Chaebol Romance", "Romantic Comedy", "Slow Burn"),
            country = "South Korea",
            countryFlag = "🇰🇷",
            originalLanguage = "Korean",
            audioLanguages = listOf("Korean (Original 5.1)", "English Dub", "Spanish Dub", "Hindi Dub", "Japanese Dub"),
            subtitleLanguages = listOf("English [CC]", "Korean", "Spanish", "French", "German", "Japanese", "Hindi", "Thai"),
            releaseYear = 2024,
            rating = 8.4,
            ratingCount = "165K",
            ageRating = "TV-14",
            quality = "4K Dolby Vision",
            duration = "1 Season / 16 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Kim Soo-hyun", "Baek Hyun-woo", "South Korea"),
                CastMember("Kim Ji-won", "Hong Hae-in", "South Korea"),
                CastMember("Park Sung-hoon", "Yoon Eun-sung", "South Korea"),
                CastMember("Kwak Dong-yeon", "Hong Soo-cheol", "South Korea")
            ),
            director = "Jang Young-woo & Kim Hee-won",
            tagline = "A miraculous love story of royalty.",
            imdbId = "tt31174980",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 34. Frieren: Beyond Journey's End (Anime Masterpiece)
        MediaItem(
            id = "an_frieren_34",
            title = "Frieren: Beyond Journey's End",
            originalTitle = "葬送のフリーレン",
            overview = "An elf mage and her fellow adventurers have defeated the Demon King and brought peace to the land. But as an elf with centuries to live, she embarks on a nostalgic pilgrimage to understand what human connections truly meant.",
            posterUrl = "https://image.tmdb.org/t/p/w780/dqZENchTd7lp5zht7BdlqM7RBhD.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/p40D2Xq0rRk10hX7Q0z5p1k2n3m.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Fantasy", "Drama", "Adventure"),
            subGenres = listOf("High Fantasy", "Seinen", "Coming of Age"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original)", "English Dub (5.1)", "Spanish Dub", "German Dub", "French Dub"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Spanish", "French", "German", "Korean", "Hindi"),
            releaseYear = 2024,
            rating = 9.1,
            ratingCount = "180K",
            ageRating = "TV-14",
            quality = "4K UHD 60FPS",
            duration = "1 Season / 28 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(crunchyroll, netflix),
            cast = listOf(
                CastMember("Atsumi Tanezaki", "Frieren (Voice)", "Japan"),
                CastMember("Kana Ichinose", "Fern (Voice)", "Japan"),
                CastMember("Chiaki Kobayashi", "Stark (Voice)", "Japan")
            ),
            director = "Keiichiro Saito (Madhouse)",
            tagline = "The journey after the hero's triumph.",
            imdbId = "tt22216834",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 35. Solo Leveling (Anime)
        MediaItem(
            id = "an_sololeveling_35",
            title = "Solo Leveling",
            originalTitle = "나 혼자만 레벨업",
            overview = "In a world where hunters must battle deadly monsters from invading Earth, weak hunter Sung Jinwoo is brutally slaughtered in a double dungeon. But he awakens before a mysterious Quest window only he can see.",
            posterUrl = "https://image.tmdb.org/t/p/w780/geCRueV3ElhRTr0Q2xBu6mZe8tc.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/29m2Xq1k8n5z4p3m9z8p1k2n3m.jpg",
            localBackdropRes = null,
            mediaType = MediaType.ANIME,
            genres = listOf("Anime", "Action", "Fantasy"),
            subGenres = listOf("Shonen", "Isekai", "Supernatural"),
            country = "Japan",
            countryFlag = "🇯🇵",
            originalLanguage = "Japanese",
            audioLanguages = listOf("Japanese (Original)", "English Dub (5.1)", "Korean Dub", "Spanish Dub", "German Dub", "Hindi Dub"),
            subtitleLanguages = listOf("English [CC]", "Japanese", "Korean", "Spanish", "French", "German", "Hindi"),
            releaseYear = 2024,
            rating = 8.5,
            ratingCount = "210K",
            ageRating = "TV-MA",
            quality = "4K UHD 60FPS",
            duration = "1 Season / 12 Ep",
            matchScore = 98,
            streamingPlatforms = listOf(crunchyroll),
            cast = listOf(
                CastMember("Taito Ban", "Sung Jinwoo (Voice)", "Japan"),
                CastMember("Genta Nakamura", "Yoo Jinho (Voice)", "Japan"),
                CastMember("Reina Ueda", "Cha Hae-in (Voice)", "Japan")
            ),
            director = "Shunsuke Nakashige (A-1 Pictures)",
            tagline = "Only I can level up.",
            imdbId = "tt21209876",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 36. Jawan (Bollywood Blockbuster)
        MediaItem(
            id = "in_jawan_36",
            title = "Jawan",
            originalTitle = "जवान",
            overview = "A driven jailer with a mysterious past orchestrates daring nationwide operations to expose rampant corruption and right the injustices suffered by ordinary citizens, supported by an all-female strike squad.",
            posterUrl = "https://image.tmdb.org/t/p/w780/jCWls3W0uQp2z41l3v9K8m5n3b.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/29K8Y8e4a90k0k3m6j9n876.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Bollywood", "Action", "Thriller"),
            subGenres = listOf("Masala Blockbuster", "High Stakes", "Action Drama"),
            country = "India",
            countryFlag = "🇮🇳",
            originalLanguage = "Hindi",
            audioLanguages = listOf("Hindi (Original Atmos)", "Tamil (5.1)", "Telugu (5.1)", "Malayalam (5.1)", "English (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Hindi", "Spanish", "French", "German", "Arabic"),
            releaseYear = 2023,
            rating = 7.4,
            ratingCount = "115K",
            ageRating = "PG-13",
            quality = "4K Dolby Atmos",
            duration = "2h 49m",
            matchScore = 96,
            streamingPlatforms = listOf(netflix),
            cast = listOf(
                CastMember("Shah Rukh Khan", "Vikram Rathore / Azad", "India"),
                CastMember("Nayanthara", "Narmada Rai", "India"),
                CastMember("Vijay Sethupathi", "Kalee Gaikwad", "India"),
                CastMember("Deepika Padukone", "Aishwarya Rathore", "India")
            ),
            director = "Atlee",
            tagline = "Ready or not, here he comes.",
            imdbId = "tt15354916",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 37. Succession (Prestige Drama)
        MediaItem(
            id = "tv_succession_37",
            title = "Succession",
            originalTitle = "Succession",
            overview = "The Roy family is known for controlling the biggest media and entertainment company in the world. However, their world changes when their aging father steps down from the company.",
            posterUrl = "https://image.tmdb.org/t/p/w780/7nRqzP2y9K8p0q2X1v9K8m5n3b.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/9n2tTRuZaeOTsdJgBP2pnd4wQI5.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Drama"),
            subGenres = listOf("Prestige Drama", "Satire", "Conspiracy"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Portuguese", "Japanese"),
            releaseYear = 2023,
            rating = 8.9,
            ratingCount = "320K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "4 Seasons / 39 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(max),
            cast = listOf(
                CastMember("Brian Cox", "Logan Roy", "UK"),
                CastMember("Jeremy Strong", "Kendall Roy", "USA"),
                CastMember("Sarah Snook", "Shiv Roy", "Australia"),
                CastMember("Kieran Culkin", "Roman Roy", "USA"),
                CastMember("Matthew Macfadyen", "Tom Wambsgans", "UK")
            ),
            director = "Jesse Armstrong",
            tagline = "Blood is thicker than water. Money is thicker than both.",
            imdbId = "tt7660850",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 38. Fallout (Sci-Fi / Adventure)
        MediaItem(
            id = "tv_fallout_38",
            title = "Fallout",
            originalTitle = "Fallout",
            overview = "In a future, post-apocalyptic Los Angeles brought about by nuclear decimation, citizens must live in underground bunkers to protect themselves from radiation, mutants and bandits.",
            posterUrl = "https://image.tmdb.org/t/p/w780/AnsTXErJm7zVwV9K8m5n3b.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/xOMo8BRK7PfcJv9JCnx7s5200bm.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Sci-Fi", "Action", "Adventure"),
            subGenres = listOf("Post-Apocalyptic", "Dark Comedy", "AI Dystopia"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)", "Hindi (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Hindi"),
            releaseYear = 2024,
            rating = 8.4,
            ratingCount = "290K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "1 Season / 8 Ep",
            matchScore = 98,
            streamingPlatforms = listOf(prime),
            cast = listOf(
                CastMember("Ella Purnell", "Lucy MacLean", "UK"),
                CastMember("Walton Goggins", "The Ghoul / Cooper Howard", "USA"),
                CastMember("Aaron Moten", "Maximus", "USA")
            ),
            director = "Jonathan Nolan & Lisa Joy",
            tagline = "The end of the world is just the beginning.",
            imdbId = "tt12637874",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 39. Severance (Sci-Fi / Thriller)
        MediaItem(
            id = "tv_severance_39",
            title = "Severance",
            originalTitle = "Severance",
            overview = "Mark leads a team of office workers whose memories have been surgically divided between their work and personal lives. When a mysterious colleague appears outside of work, it begins a journey to discover the truth about their jobs.",
            posterUrl = "https://image.tmdb.org/t/p/w780/p9K8p0q2X1v9K8m5n3b7nRqzP2y.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/rAiYTsqBk79zqjHkivvi8Aq0nPk.jpg",
            localBackdropRes = null,
            mediaType = MediaType.TV_SHOW,
            genres = listOf("Sci-Fi", "Thriller", "Mystery", "Drama"),
            subGenres = listOf("Conspiracy", "AI Dystopia", "Psychological Thriller"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2024,
            rating = 8.7,
            ratingCount = "260K",
            ageRating = "TV-MA",
            quality = "4K Dolby Vision",
            duration = "2 Seasons / 19 Ep",
            matchScore = 99,
            streamingPlatforms = listOf(apple),
            cast = listOf(
                CastMember("Adam Scott", "Mark Scout", "USA"),
                CastMember("Patricia Arquette", "Harmony Cobel", "USA"),
                CastMember("John Turturro", "Irving Bailiff", "USA"),
                CastMember("Christopher Walken", "Burt Goodman", "USA")
            ),
            director = "Ben Stiller & Aoife McArdle",
            tagline = "Please do not attempt to adjust your memory.",
            imdbId = "tt11280740",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            isFeaturedHero = false,
            isTop10 = false
        ),

        // 40. Top Gun: Maverick (Action Masterpiece)
        MediaItem(
            id = "mv_topgun_40",
            title = "Top Gun: Maverick",
            originalTitle = "Top Gun: Maverick",
            overview = "After thirty years, Maverick is still pushing the envelope as a top naval aviator, but must confront ghosts of his past when he leads TOP GUN's elite graduates on a mission that demands the ultimate sacrifice from those chosen to fly it.",
            posterUrl = "https://image.tmdb.org/t/p/w780/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/AaV1YIdWKnjAiaOe0UUKNJm322h.jpg",
            localBackdropRes = null,
            mediaType = MediaType.MOVIE,
            genres = listOf("Action", "Drama"),
            subGenres = listOf("High Octane", "Aviation", "Prestige Drama"),
            country = "United States",
            countryFlag = "🇺🇸",
            originalLanguage = "English",
            audioLanguages = listOf("English (Dolby Atmos 5.1)", "Spanish (5.1)", "French (5.1)", "German (5.1)", "Hindi (5.1)", "Japanese (5.1)"),
            subtitleLanguages = listOf("English [CC]", "Spanish", "French", "German", "Japanese", "Korean", "Hindi"),
            releaseYear = 2022,
            rating = 8.3,
            ratingCount = "680K",
            ageRating = "PG-13",
            quality = "4K IMAX Enhanced",
            duration = "2h 10m",
            matchScore = 99,
            streamingPlatforms = listOf(paramount, prime),
            cast = listOf(
                CastMember("Tom Cruise", "Pete 'Maverick' Mitchell", "USA"),
                CastMember("Miles Teller", "Bradley 'Rooster' Bradshaw", "USA"),
                CastMember("Jennifer Connelly", "Penny Benjamin", "USA"),
                CastMember("Jon Hamm", "Beau 'Cyclone' Simpson", "USA"),
                CastMember("Val Kilmer", "Tom 'Iceman' Kazansky", "USA")
            ),
            director = "Joseph Kosinski",
            tagline = "Feel the need. The need for speed.",
            imdbId = "tt1745960",
            videoStreamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            isFeaturedHero = false,
            isTop10 = false
        )
    )

    // Watchlist Room DB interactions
    val watchlistItems: Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()
    val downloadedItems: Flow<List<WatchlistEntity>> = watchlistDao.getDownloadedItems()

    fun getWatchlistByStatus(status: String): Flow<List<WatchlistEntity>> = watchlistDao.getWatchlistByStatus(status)
    fun isItemInWatchlist(id: String): Flow<Boolean> = watchlistDao.isInWatchlist(id)
    fun getWatchlistEntity(id: String): Flow<WatchlistEntity?> = watchlistDao.getWatchlistById(id)

    suspend fun addToWatchlist(item: MediaItem, status: String = "PLAN_TO_WATCH") {
        val entity = WatchlistEntity(
            id = item.id,
            title = item.title,
            posterUrl = item.posterUrl,
            backdropUrl = item.backdropUrl,
            mediaType = item.mediaType.label,
            rating = item.rating,
            releaseYear = item.releaseYear,
            country = item.country,
            countryFlag = item.countryFlag,
            duration = item.duration,
            genresString = item.genres.joinToString(", "),
            addedTimestamp = System.currentTimeMillis(),
            status = status,
            isDownloaded = false
        )
        watchlistDao.insertOrUpdate(entity)
    }

    suspend fun removeFromWatchlist(id: String) {
        watchlistDao.deleteById(id)
    }

    suspend fun updateWatchlistStatus(id: String, status: String) {
        watchlistDao.updateStatus(id, status)
    }

    suspend fun toggleDownloaded(id: String, isDownloaded: Boolean) {
        watchlistDao.updateDownloadStatus(id, isDownloaded)
    }

    suspend fun updateUserFeedback(id: String, rating: Float, notes: String) {
        watchlistDao.updateUserFeedback(id, rating, notes)
    }

    private val liveApiService = com.example.data.api.LiveMovieApiService()
    private var liveApiCatalog: List<MediaItem> = emptyList()

    private val tmdbService: com.example.data.api.tmdb.TmdbApiService
        get() = com.example.data.api.tmdb.TmdbApiClient.getService()

    suspend fun fetchLiveApiMedia(): List<MediaItem> {
        return try {
            val liveItems = mutableListOf<MediaItem>()

            // 1. Fetch Trending Movies from TMDB API with real posters and details
            try {
                val trendingResp = tmdbService.getTrendingMovies("week", 1)
                if (trendingResp.isSuccessful && trendingResp.body() != null) {
                    val movies = trendingResp.body()!!.results.map { dto ->
                        com.example.data.api.tmdb.TmdbApiClient.mapMovieDtoToMediaItem(dto, allPlatforms)
                    }
                    liveItems.addAll(movies)
                }
            } catch (e: Exception) {
                // Ignore individual section network failure
            }

            // 2. Fetch Popular TV Series from TMDB API
            try {
                val tvResp = tmdbService.getPopularTv(1)
                if (tvResp.isSuccessful && tvResp.body() != null) {
                    val shows = tvResp.body()!!.results.map { dto ->
                        com.example.data.api.tmdb.TmdbApiClient.mapTvDtoToMediaItem(dto, allPlatforms)
                    }
                    liveItems.addAll(shows)
                }
            } catch (e: Exception) {
                // Ignore individual section network failure
            }

            // 3. Keep live items or fallback to enrichedMasterCatalog
            if (liveItems.isNotEmpty()) {
                val enriched = liveItems.map { enrichMediaItem(it) }
                liveApiCatalog = enriched
                enriched
            } else {
                liveApiCatalog = emptyList()
                enrichedMasterCatalog
            }
        } catch (e: Exception) {
            liveApiCatalog = emptyList()
            enrichedMasterCatalog
        }
    }

    suspend fun searchTmdb(query: String): List<MediaItem> {
        if (query.isBlank()) return emptyList()
        return try {
            val list = mutableListOf<MediaItem>()
            val mResp = tmdbService.searchMovies(query = query)
            if (mResp.isSuccessful && mResp.body() != null) {
                list.addAll(mResp.body()!!.results.map {
                    enrichMediaItem(com.example.data.api.tmdb.TmdbApiClient.mapMovieDtoToMediaItem(it, allPlatforms))
                })
            }
            val tvResp = tmdbService.searchTv(query = query)
            if (tvResp.isSuccessful && tvResp.body() != null) {
                list.addAll(tvResp.body()!!.results.map {
                    enrichMediaItem(com.example.data.api.tmdb.TmdbApiClient.mapTvDtoToMediaItem(it, allPlatforms))
                })
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getLiveApiMedia(): List<MediaItem> {
        return if (liveApiCatalog.isNotEmpty()) liveApiCatalog else enrichedMasterCatalog
    }

    private fun getEffectiveVideoUrl(item: MediaItem): String {
        if (item.videoStreamUrl.isNotBlank() && item.videoStreamUrl != "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4") {
            return item.videoStreamUrl
        }
        return when (kotlin.math.abs(item.id.hashCode()) % 6) {
            0 -> "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
            1 -> "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            2 -> "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
            3 -> "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            4 -> "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            else -> "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        }
    }

    fun getTmdbIdForMedia(item: MediaItem): Int {
        if (item.tmdbId != null) return item.tmdbId
        return when (item.id) {
            "mv_oppenheimer_01", "mv_oppen_05" -> 872585
            "mv_dune2_02", "mv_dune_03" -> 693134
            "mv_interstellar_03", "mv_interstellar_06" -> 157336
            "tv_stranger_04", "tv_stranger_10" -> 66732
            "mv_batman_05", "mv_batman_07" -> 414906
            "kd_queen_06" -> 209867
            "an_frieren_07" -> 209867
            "mv_spider_08", "mv_spiderman_12" -> 569094
            "tv_got_09" -> 1399
            "mv_dark_knight_10" -> 155
            "mv_inception_11" -> 27205
            "tv_breaking_bad_12" -> 1396
            "kd_squid_13" -> 93405
            "an_jujutsu_14" -> 95479
            "mv_topgun_15" -> 361743
            "tv_shogun_16" -> 126308
            "an_arcane_17" -> 94605
            "mv_everything_18", "mv_eeaao_08" -> 545611
            "tv_succession_19" -> 76331
            "tv_lastofus_20" -> 100088
            "tv_fallout_21" -> 106379
            "tv_severance_22" -> 95557
            "mv_gladiator2_23" -> 558449
            "mv_avatar2_24" -> 76600
            else -> 872585
        }
    }

    fun enrichMediaItem(item: MediaItem): MediaItem {
        val streamUrl = getEffectiveVideoUrl(item)
        val audioOptions = if (item.audioTrackOptions.isNotEmpty()) item.audioTrackOptions else listOf(
            com.example.data.model.AudioTrackOption("orig", item.originalLanguage, "${item.originalLanguage} (Original Atmos 5.1)", "Dolby Atmos", "5.1 Surround", true),
            com.example.data.model.AudioTrackOption("en", "English", "English (Dolby Digital 5.1)", "Dolby Digital", "5.1 Surround", item.originalLanguage.equals("English", ignoreCase = true)),
            com.example.data.model.AudioTrackOption("es", "Spanish", "Español Latino (Doblaje 5.1)", "Dolby Digital", "5.1 Surround"),
            com.example.data.model.AudioTrackOption("fr", "French", "Français (Version Française 5.1)", "Dolby Digital", "5.1 Surround"),
            com.example.data.model.AudioTrackOption("de", "German", "Deutsch (Kino Surround 5.1)", "Dolby Digital", "5.1 Surround"),
            com.example.data.model.AudioTrackOption("ja", "Japanese", "日本語 (吹替 5.1)", "Dolby Digital", "5.1 Surround"),
            com.example.data.model.AudioTrackOption("hi", "Hindi", "हिंदी (सिनेमा 5.1)", "Dolby Digital", "5.1 Surround"),
            com.example.data.model.AudioTrackOption("director", "English", "Director's Commentary & Audio Breakdown", "Stereo Master", "2.0 Stereo")
        )
        val subtitleOptions = if (item.subtitleTrackOptions.isNotEmpty()) item.subtitleTrackOptions else listOf(
            com.example.data.model.SubtitleTrackOption("off", "Off", "Subtitles Off", true),
            com.example.data.model.SubtitleTrackOption("en_cc", "English", "English [CC] (Full Captions)"),
            com.example.data.model.SubtitleTrackOption("es_sub", "Spanish", "Español (Subtítulos)"),
            com.example.data.model.SubtitleTrackOption("fr_sub", "French", "Français (Sous-titres)"),
            com.example.data.model.SubtitleTrackOption("de_sub", "German", "Deutsch (Untertitel)"),
            com.example.data.model.SubtitleTrackOption("ja_sub", "Japanese", "日本語 (字幕)"),
            com.example.data.model.SubtitleTrackOption("ko_sub", "Korean", "한국어 (자막)"),
            com.example.data.model.SubtitleTrackOption("hi_sub", "Hindi", "हिंदी (उपशीर्षक)"),
            com.example.data.model.SubtitleTrackOption("it_sub", "Italian", "Italiano (Sottotitoli)")
        )
        val qualityOptions = if (item.qualityOptions.isNotEmpty()) item.qualityOptions else listOf(
            com.example.data.model.StreamQualityOption("4K UHD", "3840x2160", "28 Mbps (HDR10)", streamUrl),
            com.example.data.model.StreamQualityOption("1080p FHD", "1920x1080", "12 Mbps", streamUrl),
            com.example.data.model.StreamQualityOption("720p HD", "1280x720", "6 Mbps", streamUrl),
            com.example.data.model.StreamQualityOption("480p SD", "854x480", "2.5 Mbps (Data Saver)", streamUrl)
        )
        val resolvedImdb = if (item.imdbId.isNotBlank()) item.imdbId else getImdbIdForMedia(item)
        val resolvedTmdb = if (item.tmdbId != null) item.tmdbId else getTmdbIdForMedia(item)
        return item.copy(
            imdbId = resolvedImdb,
            tmdbId = resolvedTmdb,
            videoStreamUrl = streamUrl,
            audioTrackOptions = audioOptions,
            subtitleTrackOptions = subtitleOptions,
            qualityOptions = qualityOptions
        )
    }

    fun getImdbIdForMedia(item: MediaItem): String {
        if (item.imdbId.isNotBlank()) return item.imdbId
        return when (item.id) {
            "mv_oppenheimer_01", "mv_oppen_05" -> "tt15398776"
            "mv_dune2_02", "mv_dune_03" -> "tt15239678"
            "mv_interstellar_03", "mv_interstellar_06" -> "tt0816692"
            "tv_stranger_04", "tv_stranger_10" -> "tt4574334"
            "mv_batman_05", "mv_batman_07" -> "tt1877830"
            "kd_queen_06" -> "tt31174980"
            "an_frieren_07" -> "tt22216834"
            "mv_spider_08", "mv_spiderman_12" -> "tt9362722"
            "tv_got_09" -> "tt0944947"
            "mv_dark_knight_10" -> "tt0468569"
            "mv_inception_11" -> "tt1375666"
            "tv_breaking_bad_12" -> "tt0903747"
            "kd_squid_13" -> "tt10919420"
            "an_jujutsu_14" -> "tt12343534"
            "mv_topgun_15" -> "tt1745960"
            "tv_shogun_16" -> "tt2788316"
            "an_arcane_17" -> "tt11126994"
            "mv_everything_18", "mv_eeaao_08" -> "tt6710474"
            "tv_succession_19" -> "tt7660850"
            "tv_lastofus_20" -> "tt3581920"
            "tv_fallout_21" -> "tt12637874"
            "tv_severance_22" -> "tt11280740"
            "mv_gladiator2_23" -> "tt9614460"
            "mv_avatar2_24" -> "tt1630029"
            "api_tears_01" -> "tt2404435"
            "api_bbb_02" -> "tt1254207"
            "api_sintel_03" -> "tt1727587"
            "api_cosmos_04" -> "tt4987056"
            "api_night_05" -> "tt0063350"
            "api_voyage_06" -> "tt0000417"
            else -> "tt15398776"
        }
    }

    suspend fun resolveStreamFromEightStream(
        item: MediaItem,
        serverUrl: String = "https://8stream-api.vercel.app/"
    ): MediaItem {
        val imdbId = if (item.imdbId.isNotBlank()) item.imdbId else getImdbIdForMedia(item)
        try {
            val service = com.example.data.api.EightStreamApiClient.getService(serverUrl)
            val infoResponse = service.getMediaInfo(imdbId)
            if (infoResponse.isSuccessful && infoResponse.body() != null) {
                val body = infoResponse.body()!!
                val playlist = body.playlist
                if (playlist.isNotEmpty()) {
                    val newAudioTracks = mutableListOf<com.example.data.model.AudioTrackOption>()
                    var resolvedVideoUrl = item.videoStreamUrl
                    val newQualities = mutableListOf<com.example.data.model.StreamQualityOption>()

                    playlist.forEachIndexed { index, track ->
                        val langName = track.language ?: track.title.ifBlank { "Audio Track ${index + 1}" }
                        newAudioTracks.add(
                            com.example.data.model.AudioTrackOption(
                                id = "8s_audio_$index",
                                language = langName,
                                label = "$langName (8Stream HQ)",
                                audioFormat = "Dolby Surround",
                                channelLayout = "5.1",
                                isOriginal = index == 0
                            )
                        )
                    }

                    // Attempt to fetch first stream URL
                    val firstItem = playlist.first()
                    if (firstItem.file.isNotBlank() && firstItem.key.isNotBlank()) {
                        try {
                            val streamResp = service.getStream(
                                com.example.data.api.EightStreamGetStreamRequest(file = firstItem.file, key = firstItem.key)
                            )
                            if (streamResp.isSuccessful && streamResp.body() != null) {
                                val streamBody = streamResp.body()!!
                                val streamUrl = streamBody.stream ?: streamBody.url
                                if (!streamUrl.isNullOrBlank()) {
                                    resolvedVideoUrl = streamUrl
                                }
                                if (streamBody.qualities.isNotEmpty()) {
                                    streamBody.qualities.forEach { q ->
                                        newQualities.add(
                                            com.example.data.model.StreamQualityOption(
                                                label = "${q.quality} (8Stream)",
                                                resolution = q.quality,
                                                bitrate = "Direct 8Stream Link",
                                                videoUrl = q.url.ifBlank { resolvedVideoUrl }
                                            )
                                        )
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Fallback
                        }
                    }

                    return item.copy(
                        imdbId = imdbId,
                        videoStreamUrl = resolvedVideoUrl,
                        audioTrackOptions = if (newAudioTracks.isNotEmpty()) newAudioTracks else item.audioTrackOptions,
                        qualityOptions = if (newQualities.isNotEmpty()) newQualities else item.qualityOptions,
                        isEightStreamResolved = true
                    )
                }
            }
        } catch (e: Exception) {
            // Graceful fallback
        }
        return item.copy(imdbId = imdbId)
    }

    private val enrichedMasterCatalog: List<MediaItem> by lazy {
        masterCatalog.map { enrichMediaItem(it) }
    }

    private fun getCombinedCatalog(): List<MediaItem> {
        val base = enrichedMasterCatalog
        return if (liveApiCatalog.isNotEmpty()) {
            val existingIds = base.map { it.id }.toSet()
            val newApiItems = liveApiCatalog.filter { it.id !in existingIds }
            newApiItems + base
        } else {
            base
        }
    }

    // Catalog Queries
    fun getAllMedia(): List<MediaItem> = getCombinedCatalog()

    fun getHeroItems(): List<MediaItem> = getCombinedCatalog().filter { it.isFeaturedHero }

    fun getTop10Items(): List<MediaItem> = getCombinedCatalog()
        .filter { it.isTop10 && it.trendingRank != null }
        .sortedBy { it.trendingRank }

    fun getItemsByCountry(country: String): List<MediaItem> =
        getCombinedCatalog().filter { it.country.equals(country, ignoreCase = true) }

    fun getItemsByLanguage(lang: String): List<MediaItem> =
        getCombinedCatalog().filter {
            it.originalLanguage.contains(lang, ignoreCase = true) ||
            it.audioLanguages.any { a -> a.contains(lang, ignoreCase = true) }
        }

    fun getItemsByGenre(genre: String): List<MediaItem> =
        getCombinedCatalog().filter { it.genres.any { g -> g.contains(genre, ignoreCase = true) } }

    fun getItemsByType(type: MediaType): List<MediaItem> =
        getCombinedCatalog().filter { it.mediaType == type }

    fun getMediaById(id: String): MediaItem? = getCombinedCatalog().find { it.id == id }

    fun getSimilarMedia(item: MediaItem, limit: Int = 6): List<MediaItem> {
        return getCombinedCatalog()
            .filter { it.id != item.id }
            .map { other ->
                var score = 0
                val sharedGenres = itGenresMatch(item.genres, other.genres)
                score += sharedGenres * 3
                if (item.country.equals(other.country, ignoreCase = true)) score += 4
                if (item.originalLanguage.equals(other.originalLanguage, ignoreCase = true)) score += 3
                if (item.mediaType == other.mediaType) score += 2
                Pair(other, score)
            }
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }

    private fun itGenresMatch(g1: List<String>, g2: List<String>): Int {
        return g1.count { it in g2 }
    }

    fun searchAndFilter(
        query: String,
        filter: FilterState
    ): List<MediaItem> {
        return getCombinedCatalog().filter { item ->
            // Category Filter
            val matchesCategory = when (filter.selectedCategory) {
                "ALL" -> true
                "MOVIE" -> item.mediaType == MediaType.MOVIE
                "TV_SHOW" -> item.mediaType == MediaType.TV_SHOW
                "ANIME" -> item.mediaType == MediaType.ANIME
                "DOCUMENTARY" -> item.mediaType == MediaType.DOCUMENTARY
                else -> true
            }

            // Country Filter
            val matchesCountry = filter.selectedCountry == null || 
                item.country.equals(filter.selectedCountry, ignoreCase = true)

            // Language Filter
            val matchesLanguage = filter.selectedLanguage == null || 
                item.originalLanguage.contains(filter.selectedLanguage, ignoreCase = true) ||
                item.audioLanguages.any { it.contains(filter.selectedLanguage, ignoreCase = true) }

            // Main Genre Filter
            val matchesGenre = filter.selectedGenre == null || 
                item.genres.any { it.equals(filter.selectedGenre, ignoreCase = true) }

            // Sub-Genre Filter
            val matchesSubGenre = filter.selectedSubGenre == null || 
                item.subGenres.any { it.equals(filter.selectedSubGenre, ignoreCase = true) }

            // Platform Filter
            val matchesPlatform = filter.selectedPlatform == null || 
                item.streamingPlatforms.any { it.name.equals(filter.selectedPlatform, ignoreCase = true) }

            // Min Rating
            val matchesRating = item.rating >= filter.minRating

            // Year Range
            val matchesYear = item.releaseYear in filter.releaseYearRange.start.toInt()..filter.releaseYearRange.endInclusive.toInt()

            // Text Search Query
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                val q = query.trim().lowercase()
                item.title.lowercase().contains(q) ||
                item.originalTitle.lowercase().contains(q) ||
                item.overview.lowercase().contains(q) ||
                item.director.lowercase().contains(q) ||
                item.country.lowercase().contains(q) ||
                item.genres.any { it.lowercase().contains(q) } ||
                item.subGenres.any { it.lowercase().contains(q) } ||
                item.cast.any { it.name.lowercase().contains(q) || it.role.lowercase().contains(q) }
            }

            matchesCategory && matchesCountry && matchesLanguage && matchesGenre &&
                matchesSubGenre && matchesPlatform && matchesRating && matchesYear && matchesQuery
        }.sortedWith { a, b ->
            when (filter.sortBy) {
                SortOption.POPULARITY -> b.ratingCount.compareTo(a.ratingCount)
                SortOption.RATING_DESC -> b.rating.compareTo(a.rating)
                SortOption.MATCH_SCORE -> b.matchScore.compareTo(a.matchScore)
                SortOption.YEAR_DESC -> b.releaseYear.compareTo(a.releaseYear)
                SortOption.TITLE_ASC -> a.title.compareTo(b.title)
            }
        }
    }
}

data class CountryMeta(
    val id: String,
    val flag: String,
    val name: String,
    val primaryLang: String
)

data class GenreMeta(
    val name: String,
    val iconEmoji: String,
    val colorHex: Long,
    val subGenres: List<String>
)
