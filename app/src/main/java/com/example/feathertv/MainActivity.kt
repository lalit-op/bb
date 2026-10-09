package com.example.feathertv

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feathertv.ui.MainTab
import com.example.feathertv.ui.MainViewModel
import com.example.feathertv.ui.components.MiniRadioBar
import com.example.feathertv.ui.screens.AppsScreen
import com.example.feathertv.ui.screens.FavoritesScreen
import com.example.feathertv.ui.screens.RadioPlayerSheet
import com.example.feathertv.ui.screens.RadioScreen
import com.example.feathertv.ui.screens.TvPlayerScreen
import com.example.feathertv.ui.screens.TvScreen
import com.example.feathertv.ui.theme.BgDark
import com.example.feathertv.ui.theme.CardDark
import com.example.feathertv.ui.theme.FeatherTVTheme
import com.example.feathertv.ui.theme.NeonBlue
import com.example.feathertv.ui.theme.NeonPink
import com.example.feathertv.ui.theme.TextMuted

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FeatherTVTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val context = LocalContext.current

    val currentTab by viewModel.currentTab.collectAsState()
    val activeTvChannel by viewModel.activeTvChannel.collectAsState()

    // TV state
    val tvChannels by viewModel.filteredTvChannels.collectAsState()
    val tvCategory by viewModel.tvCategory.collectAsState()
    val tvSearchQuery by viewModel.tvSearchQuery.collectAsState()
    val favoriteChannelNames by viewModel.favoriteChannelNames.collectAsState()

    // Radio state
    val radioStations by viewModel.filteredRadioStations.collectAsState()
    val currentRadioStation by viewModel.currentRadioStation.collectAsState()
    val isRadioPlaying by viewModel.isRadioPlaying.collectAsState()
    val radioCategory by viewModel.radioCategory.collectAsState()
    val radioSearchQuery by viewModel.radioSearchQuery.collectAsState()
    val favoriteRadioIds by viewModel.favoriteRadioIds.collectAsState()
    val radioVolume by viewModel.radioVolume.collectAsState()
    val showRadioSheet by viewModel.showRadioSheet.collectAsState()
    val sleepTimerMinutesLeft by viewModel.sleepTimerMinutesLeft.collectAsState()

    // Favorites state
    val favoriteChannelsList by viewModel.favoriteChannelsList.collectAsState()
    val favoriteStationsList by viewModel.favoriteStationsList.collectAsState()

    // Apps
    val apps = viewModel.streamingApps

    // Fullscreen TV player screen
    if (activeTvChannel != null) {
        val channel = activeTvChannel!!
        TvPlayerScreen(
            currentChannel = channel,
            allChannels = tvChannels,
            isFavorite = favoriteChannelNames.contains(channel.name),
            onClosePlayer = { viewModel.closeTvPlayer() },
            onSelectChannel = { viewModel.openTvChannel(it) },
            onToggleFavorite = { viewModel.toggleTvFavorite(channel.name) }
        )
        return
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgDark)
            ) {
                // Mini Radio Bar if radio station is active
                if (currentRadioStation != null) {
                    MiniRadioBar(
                        station = currentRadioStation!!,
                        isPlaying = isRadioPlaying,
                        onTogglePlayPause = { viewModel.toggleRadioPlayPause() },
                        onClickBar = { viewModel.openRadioSheet() }
                    )
                }

                NavigationBar(
                    containerColor = CardDark,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == MainTab.TV,
                        onClick = { viewModel.setTab(MainTab.TV) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Tv,
                                contentDescription = "Live TV",
                                tint = if (currentTab == MainTab.TV) NeonBlue else TextMuted
                            )
                        },
                        label = {
                            Text(
                                "Live TV",
                                color = if (currentTab == MainTab.TV) NeonBlue else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == MainTab.TV) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_tv")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.RADIO,
                        onClick = { viewModel.setTab(MainTab.RADIO) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Radio,
                                contentDescription = "Radio",
                                tint = if (currentTab == MainTab.RADIO) NeonPink else TextMuted
                            )
                        },
                        label = {
                            Text(
                                "Radio",
                                color = if (currentTab == MainTab.RADIO) NeonPink else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == MainTab.RADIO) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonPink.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_radio")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.FAVORITES,
                        onClick = { viewModel.setTab(MainTab.FAVORITES) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "Favorites",
                                tint = if (currentTab == MainTab.FAVORITES) NeonPink else TextMuted
                            )
                        },
                        label = {
                            Text(
                                "Favorites",
                                color = if (currentTab == MainTab.FAVORITES) NeonPink else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == MainTab.FAVORITES) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonPink.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_favorites")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.APPS,
                        onClick = { viewModel.setTab(MainTab.APPS) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Apps,
                                contentDescription = "Apps",
                                tint = if (currentTab == MainTab.APPS) NeonBlue else TextMuted
                            )
                        },
                        label = {
                            Text(
                                "Apps",
                                color = if (currentTab == MainTab.APPS) NeonBlue else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == MainTab.APPS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = NeonBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag("nav_item_apps")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.TV -> {
                    TvScreen(
                        channels = tvChannels,
                        favoriteNames = favoriteChannelNames,
                        selectedCategory = tvCategory,
                        searchQuery = tvSearchQuery,
                        apps = apps,
                        onSelectCategory = { viewModel.selectTvCategory(it) },
                        onSearchQueryChange = { viewModel.setTvSearchQuery(it) },
                        onChannelClick = { viewModel.openTvChannel(it) },
                        onToggleFavorite = { viewModel.toggleTvFavorite(it) },
                        onOpenApp = { app ->
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Opening ${app.name}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                MainTab.RADIO -> {
                    RadioScreen(
                        stations = radioStations,
                        favoriteIds = favoriteRadioIds,
                        currentStation = currentRadioStation,
                        isPlaying = isRadioPlaying,
                        selectedCategory = radioCategory,
                        searchQuery = radioSearchQuery,
                        onSelectCategory = { viewModel.selectRadioCategory(it) },
                        onSearchQueryChange = { viewModel.setRadioSearchQuery(it) },
                        onStationClick = { station ->
                            viewModel.playRadioStation(station)
                        },
                        onToggleFavorite = { viewModel.toggleRadioFavorite(it) }
                    )
                }

                MainTab.FAVORITES -> {
                    FavoritesScreen(
                        favoriteChannels = favoriteChannelsList,
                        favoriteStations = favoriteStationsList,
                        currentStation = currentRadioStation,
                        isRadioPlaying = isRadioPlaying,
                        onChannelClick = { viewModel.openTvChannel(it) },
                        onToggleChannelFavorite = { viewModel.toggleTvFavorite(it) },
                        onStationClick = { viewModel.playRadioStation(it) },
                        onToggleStationFavorite = { viewModel.toggleRadioFavorite(it) }
                    )
                }

                MainTab.APPS -> {
                    AppsScreen(
                        apps = apps,
                        onOpenApp = { app ->
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.url))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cannot open URL", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    // Harsh-IT Radio Player Full BottomSheet Modal
    if (showRadioSheet && currentRadioStation != null) {
        val station = currentRadioStation!!
        RadioPlayerSheet(
            station = station,
            isPlaying = isRadioPlaying,
            isFavorite = favoriteRadioIds.contains(station.id),
            volume = radioVolume,
            sleepTimerMinutesLeft = sleepTimerMinutesLeft,
            onTogglePlayPause = { viewModel.toggleRadioPlayPause() },
            onNextStation = { viewModel.nextRadioStation() },
            onPreviousStation = { viewModel.previousRadioStation() },
            onToggleFavorite = { viewModel.toggleRadioFavorite(station.id) },
            onVolumeChange = { viewModel.setRadioVolume(it) },
            onSetSleepTimer = { viewModel.setSleepTimer(it) },
            onDismiss = { viewModel.closeRadioSheet() }
        )
    }
}
