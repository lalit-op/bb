package com.example.feathertv.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feathertv.data.model.Channel
import com.example.feathertv.data.model.RadioStation
import com.example.feathertv.ui.theme.BgDark
import com.example.feathertv.ui.theme.NeonBlue
import com.example.feathertv.ui.theme.NeonPink
import com.example.feathertv.ui.theme.TextMuted
import com.example.feathertv.ui.theme.TextPrimary
import com.example.feathertv.ui.theme.TextSecondary

@Composable
fun FavoritesScreen(
    favoriteChannels: List<Channel>,
    favoriteStations: List<RadioStation>,
    currentStation: RadioStation?,
    isRadioPlaying: Boolean,
    onChannelClick: (Channel) -> Unit,
    onToggleChannelFavorite: (String) -> Unit,
    onStationClick: (RadioStation) -> Unit,
    onToggleStationFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .testTag("favorites_screen_content")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "FAVORITES",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Your curated TV channels and live radio stations",
                color = TextMuted,
                fontSize = 12.sp
            )
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = BgDark,
            contentColor = TextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = if (selectedTab == 0) NeonBlue else NeonPink
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "📺 Live TV (${favoriteChannels.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == 0) NeonBlue else TextSecondary
                    )
                },
                modifier = Modifier.testTag("fav_tv_tab")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "📻 Radio (${favoriteStations.size})",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == 1) NeonPink else TextSecondary
                    )
                },
                modifier = Modifier.testTag("fav_radio_tab")
            )
        }

        if (selectedTab == 0) {
            if (favoriteChannels.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorite TV channels added yet.\nTap ❤️ on channels in Live TV to save them here!",
                        color = TextMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 105.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("fav_channels_grid"),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(favoriteChannels, key = { it.id }) { channel ->
                        ChannelCard(
                            channel = channel,
                            isFavorite = true,
                            onClick = { onChannelClick(channel) },
                            onToggleFavorite = { onToggleChannelFavorite(channel.name) }
                        )
                    }
                }
            }
        } else {
            if (favoriteStations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorite radio stations yet.\nTap ❤️ on stations in Radio tab to keep them handy!",
                        color = TextMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("fav_stations_list"),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favoriteStations, key = { it.id }) { station ->
                        val isCurrentStation = currentStation?.id == station.id
                        RadioStationRow(
                            station = station,
                            isFavorite = true,
                            isActive = isCurrentStation,
                            isPlaying = isRadioPlaying && isCurrentStation,
                            onClick = { onStationClick(station) },
                            onToggleFavorite = { onToggleStationFavorite(station.id) }
                        )
                    }
                }
            }
        }
    }
}
