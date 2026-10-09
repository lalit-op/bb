package com.example.feathertv.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.feathertv.data.model.RadioStation
import com.example.feathertv.data.model.StreamingApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RadioRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("feather_radio_prefs", Context.MODE_PRIVATE)

    private val defaultFavorites = setOf(1, 7, 2, 3, 4, 6, 106)

    private val _favoriteStationIds = MutableStateFlow<Set<Int>>(loadFavorites())
    val favoriteStationIds: StateFlow<Set<Int>> = _favoriteStationIds.asStateFlow()

    private fun loadFavorites(): Set<Int> {
        val saved = prefs.getStringSet("favorite_radio_ids", null)
        return saved?.mapNotNull { it.toIntOrNull() }?.toSet() ?: defaultFavorites
    }

    fun toggleFavorite(stationId: Int) {
        val current = _favoriteStationIds.value.toMutableSet()
        if (current.contains(stationId)) {
            current.remove(stationId)
        } else {
            current.add(stationId)
        }
        prefs.edit().putStringSet("favorite_radio_ids", current.map { it.toString() }.toSet()).apply()
        _favoriteStationIds.value = current
    }

    fun isFavorite(stationId: Int): Boolean {
        return _favoriteStationIds.value.contains(stationId)
    }

    fun getAllStations(): List<RadioStation> = stationsList

    fun getApps(): List<StreamingApp> = appsList

    companion object {
        val categories = listOf(
            "All",
            "Favorites",
            "AIR",
            "Bollywood",
            "Bhakti",
            "Retro",
            "Evergreen",
            "Hindi"
        )

        private val stationsList = listOf(
            RadioStation(
                id = 1,
                name = "AIR FM Gold",
                slogan = "106.4 MHz • Delhi • Akashvani",
                location = "India",
                streamUrl = "https://airhlspush.pc.cdn.bitgravity.com/httppush/hlspbaudio005/hlspbaudio00564kbps.m3u8",
                colorHex = "#FF5722",
                logoUrl = "https://onlineradiofm.in/assets/image/radio/180/air-gold.jpg",
                category = "AIR",
                isPopular = true,
                isTrending = true
            ),
            RadioStation(
                id = 12,
                name = "AIR Vividh Bharati",
                slogan = "Desh Ki Swar Lahari • Mumbai",
                location = "India",
                streamUrl = "https://airhlspush.pc.cdn.bitgravity.com/httppush/hlspbaudio001/hlspbaudio00164kbps.m3u8",
                colorHex = "#2196F3",
                logoUrl = "https://onlineradiofm.in/assets/image/radio/180/vividh-bharti.jpg",
                category = "AIR",
                isPopular = true,
                isTrending = true
            ),
            RadioStation(
                id = 13,
                name = "AIR Indraprastha",
                slogan = "Bahujan Hitaya Bahujan Sukhaya",
                location = "India",
                streamUrl = "https://airhlspush.pc.cdn.bitgravity.com/httppush/hlspbaudio006/hlspbaudio00664kbps.m3u8",
                colorHex = "#FF9800",
                logoUrl = "https://onlineradiofm.in/assets/image/radio/180/indrapastha.jpg",
                category = "AIR",
                isPopular = true
            ),
            RadioStation(
                id = 7,
                name = "Radio Mirchi Dubai",
                slogan = "102.4 FM • Bollywood • Hindi",
                location = "Dubai",
                streamUrl = "https://playerservices.streamtheworld.com/api/livestream-redirect/DUB_HIN_GSTAAC.m3u8",
                colorHex = "#F44336",
                logoUrl = "https://onlineradiofm.in/assets/image/radio/180/mirchi-dubai.png",
                category = "Bollywood",
                isPopular = true,
                isTrending = true
            ),
            RadioStation(
                id = 2,
                name = "Radio Mirchi New Jersey",
                slogan = "92.7 FM • Bollywood • Hindi",
                location = "New Jersey",
                streamUrl = "https://playerservices.streamtheworld.com/api/livestream-redirect/NJS_HIN_ESTAAC.m3u8",
                colorHex = "#3F51B5",
                logoUrl = "https://onlineradiofm.in/assets/image/radio/180/jersey.png",
                category = "Bollywood",
                isPopular = true
            ),
            RadioStation(
                id = 3,
                name = "Bollywood 2010s",
                slogan = "Golden Modern Hits 24x7",
                location = "Internet",
                streamUrl = "https://drive.uber.radio/uber/bollywood2010s/icecast.audio",
                colorHex = "#9C27B0",
                logoUrl = "https://liveradios.in/wp-content/uploads/br1.jpg",
                category = "Bollywood",
                isPopular = true,
                isTrending = true
            ),
            RadioStation(
                id = 4,
                name = "Mirchi Top 20",
                slogan = "Top Chartbusters & New Releases",
                location = "Internet",
                streamUrl = "https://bhs.edge.mystreaming.net/uber/bollywoodnow/icecast.audio",
                colorHex = "#E91E63",
                logoUrl = "https://liveradios.in/wp-content/uploads/mirchitop20.jpg",
                category = "Bollywood",
                isPopular = true,
                isTrending = true
            ),
            RadioStation(
                id = 5,
                name = "DesiZone Radio",
                slogan = "24x7 Non-Stop Bollywood Hits",
                location = "Internet",
                streamUrl = "https://www.desizoneradio.com/relay1",
                colorHex = "#00BCD4",
                logoUrl = "https://www.desizoneradio.com/images/logo.png",
                category = "Bollywood",
                isPopular = true
            ),
            RadioStation(
                id = 6,
                name = "Red FM 93.5",
                slogan = "Bajaate Raho • Superhit Music",
                location = "India",
                streamUrl = "https://stream-281.surfernetwork.com/9phrkb1e3v8uv",
                colorHex = "#D32F2F",
                logoUrl = "https://radiobarfi.com/wp-content/uploads/2024/04/Red-FM-93.5-Live-Online.png",
                category = "Bollywood",
                isPopular = true,
                isTrending = true
            ),
            RadioStation(
                id = 8,
                name = "Bollywood Love",
                slogan = "24×7 Romantic Melodies",
                location = "Internet",
                streamUrl = "https://drive.uber.radio/uber/bollywoodlove/icecast.audio",
                colorHex = "#EC407A",
                logoUrl = "https://liveradios.in/wp-content/uploads/br3.jpg",
                category = "Bollywood",
                isPopular = true
            ),
            RadioStation(
                id = 10,
                name = "Fun Asia Radio",
                slogan = "Bollywood Music & Entertainment",
                location = "Internet",
                streamUrl = "https://funasia.streamguys1.com/live-1?_=1545125406418",
                colorHex = "#FFA726",
                logoUrl = "https://onlineradiofm.in/assets/image/radio/180/funasia1049.png",
                category = "Bollywood"
            ),
            RadioStation(
                id = 11,
                name = "Caravan Radio",
                slogan = "Evergreen Bollywood Hits",
                location = "Internet",
                streamUrl = "https://streamlky.alsolnet.com/radiocaravanaudio",
                colorHex = "#FFB74D",
                logoUrl = "https://radiosindia.com/images/radiocaravan.jpg",
                category = "Evergreen",
                isPopular = true
            ),
            RadioStation(
                id = 101,
                name = "Bhakti Sangeet",
                slogan = "Sacred Devotional Melodies",
                location = "India",
                streamUrl = "https://stream-291.surfernetwork.com/ut3kgm1vsa0uv",
                colorHex = "#FF9800",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/wczz4pqgu5fa.jpeg",
                category = "Bhakti",
                isPopular = true
            ),
            RadioStation(
                id = 102,
                name = "Bhaktiworld Media Shiva",
                slogan = "Shiv Bhakti & Chants 24x7",
                location = "India",
                streamUrl = "http://hot.out.airtime.pro:8000/hot_a",
                colorHex = "#FF5722",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/446/bhakti-world-shiva.cc853b30.png",
                category = "Bhakti"
            ),
            RadioStation(
                id = 103,
                name = "Hits Of Asha Bhosle",
                slogan = "Evergreen Asha Ji Melodies",
                location = "Vadodara",
                streamUrl = "https://stream.zeno.fm/qd0ddu5qrdktv",
                colorHex = "#E91E63",
                logoUrl = "https://static.mytuner.mobi/media/tvos_radios/589/hits-of-asha-bhosle.35abf741.jpg",
                category = "Retro"
            ),
            RadioStation(
                id = 104,
                name = "Retro Bollywood 90s",
                slogan = "Unforgettable 90s Golden Era",
                location = "Gulbarga",
                streamUrl = "https://stream.zeno.fm/u0hrd3xkzhhvv",
                colorHex = "#7E57C2",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/9fKKkfbJKE.png",
                category = "Retro",
                isPopular = true
            ),
            RadioStation(
                id = 105,
                name = "Bollywood Gaane Purane",
                slogan = "Old Classic Bollywood Songs",
                location = "Belgaum",
                streamUrl = "https://stream.zeno.fm/6n6ewddtad0uv",
                colorHex = "#D81B60",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/ppqbgfej6skx.jpeg",
                category = "Evergreen"
            ),
            RadioStation(
                id = 106,
                name = "Mohammed Rafi Radio",
                slogan = "The Soul of Indian Music",
                location = "Mumbai",
                streamUrl = "https://stream-165.zeno.fm/2xx62x8ztm0uv",
                colorHex = "#00ACC1",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/878/mohammed-rafi-radio.e360f514.jpg",
                category = "Retro",
                isPopular = true
            ),
            RadioStation(
                id = 107,
                name = "Hindi Retro Hits Radio",
                slogan = "Nostalgic Masterpieces",
                location = "India",
                streamUrl = "https://stream-006.zeno.fm/v2zfmxef798uv",
                colorHex = "#FFB300",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/7ksqhvudgsc9.jpg",
                category = "Retro"
            ),
            RadioStation(
                id = 108,
                name = "Mohabbat Radio",
                slogan = "Hindi Romantic Classics",
                location = "Budhlada",
                streamUrl = "https://stream.zeno.fm/t2ekq8zsgtzuv",
                colorHex = "#F06292",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/608/mohabbat-radio.8821dce6.png",
                category = "Hindi"
            ),
            RadioStation(
                id = 109,
                name = "Non Stop Hindi",
                slogan = "Hindi • Non-Stop Hits",
                location = "India",
                streamUrl = "https://s5.voscast.com:8217/stream",
                colorHex = "#00E676",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/Ekv3xnWJpp.png",
                category = "Hindi"
            ),
            RadioStation(
                id = 110,
                name = "GOLDY Evergreen",
                slogan = "Timeless Golden Music",
                location = "Dhandhuka",
                streamUrl = "https://stream.zeno.fm/n2fd0edh9k8uv",
                colorHex = "#FFD54F",
                logoUrl = "https://static.mytuner.mobi/media/radios-150px/khbf2jlccjaf.png",
                category = "Evergreen"
            )
        )

        private val appsList = listOf(
            StreamingApp(
                name = "GDL TV",
                url = "https://play.freeforall.dev",
                iconEmoji = "📡",
                description = "Live Satellite Portal",
                primaryColorHex = "#F43F5E"
            ),
            StreamingApp(
                name = "Momix",
                url = "https://momixtv-jiotv.pages.dev/",
                iconEmoji = "💎",
                description = "Entertainment Streamer",
                primaryColorHex = "#0EA5E9"
            ),
            StreamingApp(
                name = "All Live TV",
                url = "https://allinonelivetv.pages.dev/",
                iconEmoji = "⚡",
                description = "All-in-one Network",
                primaryColorHex = "#F59E0B"
            ),
            StreamingApp(
                name = "SkyFlixer",
                url = "https://skyflixer.fun/browse",
                iconEmoji = "🍿",
                description = "Movies & Series Explorer",
                primaryColorHex = "#10B981"
            ),
            StreamingApp(
                name = "JioTV Plus",
                url = "https://jiotvplusindia.blogspot.com/?m=1",
                iconEmoji = "🌐",
                description = "Indian Broadcast Catalog",
                primaryColorHex = "#EC4899"
            ),
            StreamingApp(
                name = "Jio-Web",
                url = "https://iptvportal.liveblog365.com/Webs/jiowebx/?i=1",
                iconEmoji = "🖥️",
                description = "Web TV Streamer",
                primaryColorHex = "#8B5CF6"
            )
        )
    }
}
