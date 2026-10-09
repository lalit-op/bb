package com.example.feathertv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.feathertv.data.model.RadioStation
import com.example.feathertv.data.repository.RadioRepository
import com.example.feathertv.ui.components.AudioVisualizer
import com.example.feathertv.ui.theme.BgDeepDark
import com.example.feathertv.ui.theme.CardDark
import com.example.feathertv.ui.theme.NeonAmber
import com.example.feathertv.ui.theme.NeonCyan
import com.example.feathertv.ui.theme.NeonPink
import com.example.feathertv.ui.theme.NeonPurple
import com.example.feathertv.ui.theme.TextMuted
import com.example.feathertv.ui.theme.TextPrimary
import com.example.feathertv.ui.theme.TextSecondary

@Composable
fun RadioScreen(
    stations: List<RadioStation>,
    favoriteIds: Set<Int>,
    currentStation: RadioStation?,
    isPlaying: Boolean,
    selectedCategory: String,
    searchQuery: String,
    onSelectCategory: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onStationClick: (RadioStation) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryIcons = mapOf(
        "All" to "📻",
        "Favorites" to "❤️",
        "AIR" to "🏛️",
        "Bollywood" to "💃",
        "Bhakti" to "🕉️",
        "Retro" to "📼",
        "Evergreen" to "✨",
        "Hindi" to "🇮🇳"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDeepDark)
            .testTag("radio_screen_content")
    ) {
        // Sticky Header with Search
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgDeepDark.copy(alpha = 0.95f))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("radio_search_input"),
                    placeholder = {
                        Text(
                            "Search live radio stations...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = NeonPink,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "${stations.size} stations",
                                color = TextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardDark,
                        unfocusedContainerColor = CardDark,
                        focusedBorderColor = NeonPink,
                        unfocusedBorderColor = Color(0xFF2A2A3A),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioRepository.categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    val icon = categoryIcons[category] ?: "📻"
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(NeonPink, NeonPurple))
                                else Brush.horizontalGradient(listOf(Color(0xFF1E1E28), Color(0xFF15151E)))
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.White.copy(alpha = 0.4f) else Color(0xFF2A2A3A),
                                CircleShape
                            )
                            .clickable { onSelectCategory(category) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("radio_category_chip_$category")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = icon, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = category,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Stations List
        if (stations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedCategory == "Favorites")
                        "No favorite stations saved yet! Tap ❤️ on any radio station to keep it handy."
                    else
                        "No stations found matching \"$searchQuery\"",
                    color = TextMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("radio_stations_list"),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stations, key = { it.id }) { station ->
                    val isFav = favoriteIds.contains(station.id)
                    val isCurrentStation = currentStation?.id == station.id
                    RadioStationRow(
                        station = station,
                        isFavorite = isFav,
                        isActive = isCurrentStation,
                        isPlaying = isPlaying && isCurrentStation,
                        onClick = { onStationClick(station) },
                        onToggleFavorite = { onToggleFavorite(station.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RadioStationRow(
    station: RadioStation,
    isFavorite: Boolean,
    isActive: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("station_row_${station.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFF1E1E2A) else Color(0xFF15151E)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) NeonPink else Color(0xFF2A2A3A)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Station Logo
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(1.dp, Color(0xFF333344), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = station.logoUrl,
                    contentDescription = station.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Station Metadata
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = station.name,
                        color = if (isActive) NeonPink else TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (isActive && isPlaying) {
                        Spacer(modifier = Modifier.width(6.dp))
                        AudioVisualizer(
                            isPlaying = true,
                            barCount = 4,
                            barWidth = 2.dp,
                            maxHeight = 14.dp,
                            minHeight = 4.dp,
                            color = NeonPink
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = station.slogan,
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonPink.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = station.category,
                            color = NeonPink,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonCyan.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = station.location,
                            color = NeonCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Favorite button
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .testTag("station_fav_btn_${station.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) NeonPink else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
