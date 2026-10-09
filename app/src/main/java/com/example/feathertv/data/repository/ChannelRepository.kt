package com.example.feathertv.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.feathertv.data.model.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChannelRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("feather_tv_prefs", Context.MODE_PRIVATE)

    private val defaultFavorites = setOf(
        "Sony SAB SD",
        "Sony Pal",
        "Disney Channel",
        "Hungama TV",
        "Sony YAY Hindi",
        "Colors HD",
        "Colors Rishtey",
        "MTV HD",
        "Star Plus HD",
        "Star Sports 2"
    )

    private val _favoriteChannelNames = MutableStateFlow<Set<String>>(loadFavorites())
    val favoriteChannelNames: StateFlow<Set<String>> = _favoriteChannelNames.asStateFlow()

    private fun loadFavorites(): Set<String> {
        val saved = prefs.getStringSet("favorite_channels", null)
        return saved ?: defaultFavorites
    }

    fun toggleFavorite(channelName: String) {
        val current = _favoriteChannelNames.value.toMutableSet()
        if (current.contains(channelName)) {
            current.remove(channelName)
        } else {
            current.add(channelName)
        }
        prefs.edit().putStringSet("favorite_channels", current).apply()
        _favoriteChannelNames.value = current
    }

    fun isFavorite(channelName: String): Boolean {
        return _favoriteChannelNames.value.contains(channelName)
    }

    fun getAllChannels(): List<Channel> = channelsList

    companion object {
        val categories = listOf(
            "All",
            "Favorites",
            "Fun-Ment",
            "Kids",
            "Movies",
            "Sports",
            "Info",
            "News",
            "Music",
            "Regional"
        )

        private val channelsList = listOf(
            Channel(
                id = "154",
                name = "Sony SAB SD",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Sony_SAB.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=154"
            ),
            Channel(
                id = "471",
                name = "Sony SAB HD",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Sony_SAB.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=471"
            ),
            Channel(
                id = "sony-pal",
                name = "Sony Pal",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Sony_Pal.png",
                webUrl = "https://suniliv.pages.dev/ptest1?id=sony-pal"
            ),
            Channel(
                id = "144",
                name = "Colors HD",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Colors_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=144"
            ),
            Channel(
                id = "279",
                name = "Colors Rishtey",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/ColorsRishtey.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=279"
            ),
            Channel(
                id = "1132",
                name = "Star Plus HD",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Star_Plus_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1132"
            ),
            Channel(
                id = "167",
                name = "Zee TV HD",
                category = "Fun-Ment",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/ZeeTVHD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=167"
            ),
            Channel(
                id = "472",
                name = "And TV HD",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/And_TV_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=472"
            ),
            Channel(
                id = "291",
                name = "Sony Entertainment Television",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Sony_SD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=291"
            ),
            Channel(
                id = "1359",
                name = "Big Magic",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Big_Magic.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1359"
            ),
            Channel(
                id = "1145",
                name = "MTV HD",
                category = "Music",
                logoUrl = "https://cdn.brandfetch.io/idG02Aquas/w/400/h/400/theme/dark/icon.png?c=1bxid64Mup7aczewSAYMX&t=1667701258766",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1145"
            ),
            Channel(
                id = "1373",
                name = "Disney Channel",
                category = "Kids",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/Disney_Channel.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1373"
            ),
            Channel(
                id = "1391",
                name = "Hungama TV",
                category = "Kids",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/Hungama.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1391"
            ),
            Channel(
                id = "1392",
                name = "Hungama Super",
                category = "Kids",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/Super_Hungama.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1392"
            ),
            Channel(
                id = "872",
                name = "Sony YAY Hindi",
                category = "Kids",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/SonyYAYHin.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=872"
            ),
            Channel(
                id = "816",
                name = "Cartoon Network",
                category = "Kids",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/CNHindi.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=816"
            ),
            Channel(
                id = "156",
                name = "Star Gold",
                category = "Movies",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Star_Gold_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=156"
            ),
            Channel(
                id = "3096",
                name = "Star Gold 2 HD",
                category = "Movies",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Star_Gold_2_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3096"
            ),
            Channel(
                id = "3097",
                name = "Star Gold Romance",
                category = "Movies",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Star_Gold_Romance.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3097"
            ),
            Channel(
                id = "3098",
                name = "Star Gold Thrills",
                category = "Movies",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Star_Gold_Thrills.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3098"
            ),
            Channel(
                id = "476",
                name = "Sony Max HD",
                category = "Movies",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/SonyMAX.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=476"
            ),
            Channel(
                id = "165",
                name = "Zee Cinema HD",
                category = "Movies",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/ZeeCinemaHD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=165"
            ),
            Channel(
                id = "and-pictures",
                name = "& Pictures HD",
                category = "Movies",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/AndPicturesHD.png",
                streamUrl = "https://d1g8wgjurz8via.cloudfront.net/bpk-tv/Andpictures/default/manifest.mpd",
                webUrl = "https://allinonereborn2.online/zee5/player.php?mpd=https%3A%2F%2Fd1g8wgjurz8via.cloudfront.net%2Fbpk-tv%2FAndpictures%2Fdefault%2Fmanifest.mpd&keyid=8dea532cabfe4f71ba20f62310e7949f&key=7a214a974e4f4d1d9bb66364d5f0cb92"
            ),
            Channel(
                id = "1984",
                name = "Star Sports 2",
                category = "Sports",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Star_Sports_2.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1984"
            ),
            Channel(
                id = "sony-ten-3",
                name = "Sony TEN 3 HD",
                category = "Sports",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/Ten3_HD.png",
                webUrl = "https://suniliv.pages.dev/ptest1?id=sony-ten-3-hd"
            ),
            Channel(
                id = "1332",
                name = "National Geographic HD",
                category = "Info",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/Nat_Geo_Wild_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=1332"
            ),
            Channel(
                id = "3458",
                name = "TLC HD Hindi",
                category = "Info",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/TLCHDHindi.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3458"
            ),
            Channel(
                id = "146",
                name = "History TV 18",
                category = "Info",
                logoUrl = "https://jiotvimages.cdn.jio.com/dare_images/images/History_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=146"
            ),
            Channel(
                id = "562",
                name = "Travel XP HD",
                category = "Info",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Travel_XP_HD.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=562"
            ),
            Channel(
                id = "173",
                name = "Aaj Tak HD",
                category = "News",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/Aaj_Tak.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=173"
            ),
            Channel(
                id = "203",
                name = "DD News",
                category = "News",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/DD_News.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=203"
            ),
            Channel(
                id = "3444",
                name = "Discovery HD Hindi",
                category = "Info",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/DiscoveryHDHin.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3444"
            ),
            Channel(
                id = "3453",
                name = "Discovery Science Hindi",
                category = "Info",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/DiscoveryScienceHin.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3453"
            ),
            Channel(
                id = "3434",
                name = "Investigation Discovery Hindi",
                category = "Fun-Ment",
                logoUrl = "https://jiotv.catchup.cdn.jio.com/dare_images/images/IDHDHindi.png",
                webUrl = "https://jtvxweb.pages.dev/pind?id=3434"
            )
        )
    }
}
