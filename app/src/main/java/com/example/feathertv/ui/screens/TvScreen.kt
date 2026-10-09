package com.example.feathertv.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.feathertv.data.model.Channel
import com.example.feathertv.data.model.StreamingApp
import com.example.feathertv.data.repository.ChannelRepository
import com.example.feathertv.ui.theme.BgDark
import com.example.feathertv.ui.theme.CardDark
import com.example.feathertv.ui.theme.NeonBlue
import com.example.feathertv.ui.theme.NeonPink
import com.example.feathertv.ui.theme.NeonPurple
import com.example.feathertv.ui.theme.TextMuted
import com.example.feathertv.ui.theme.TextPrimary
import com.example.feathertv.ui.theme.TextSecondary

@Composable
fun TvScreen(
    channels: List<Channel>,
    favoriteNames: Set<String>,
    selectedCategory: String,
    searchQuery: String,
    apps: List<StreamingApp>,
    onSelectCategory: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onChannelClick: (Channel) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenApp: (StreamingApp) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAppsRow by remember { mutableStateOf(false) }

    val categoryIcons = mapOf(
        "All" to "📺",
        "Favorites" to "❤️",
        "Fun-Ment" to "🎭",
        "Kids" to "🧸",
        "Movies" to "🎬",
        "Sports" to "⚽",
        "Info" to "🌎",
        "News" to "📰",
        "Music" to "🎵",
        "Regional" to "🌍"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
            .testTag("tv_screen_content")
    ) {
        // Sticky Header with Search
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgDark.copy(alpha = 0.95f))
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
                        .weight(1f)
                        .height(50.dp)
                        .testTag("home_channel_search_input"),
                    placeholder = {
                        Text(
                            "Search channels...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = NeonBlue,
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
                                text = "${channels.size}",
                                color = TextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardDark,
                        unfocusedContainerColor = CardDark,
                        focusedBorderColor = NeonBlue,
                        unfocusedBorderColor = Color(0xFF26313D),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Toggle Quick Apps
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (showAppsRow) Brush.horizontalGradient(listOf(NeonPurple, NeonBlue))
                            else Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
                        )
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                        .clickable { showAppsRow = !showAppsRow }
                        .padding(horizontal = 10.dp, vertical = 12.dp)
                        .testTag("toggle_apps_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.RocketLaunch,
                            contentDescription = "Apps",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Apps",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Expandable Quick Apps Row
            AnimatedVisibility(visible = showAppsRow) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(
                        text = "🚀 QUICK STREAMING HUBS",
                        color = NeonCyanAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        apps.forEach { app ->
                            Card(
                                modifier = Modifier
                                    .width(130.dp)
                                    .height(72.dp)
                                    .clickable { onOpenApp(app) }
                                    .testTag("app_card_${app.name}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(android.graphics.Color.parseColor(app.primaryColorHex)).copy(alpha = 0.25f)
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = app.iconEmoji, fontSize = 20.sp)
                                    Text(
                                        text = app.name,
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChannelRepository.categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    val icon = categoryIcons[category] ?: "📺"
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(NeonPink, NeonPurple))
                                else Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFF141A22)))
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color.White.copy(alpha = 0.4f) else Color(0xFF26313D),
                                CircleShape
                            )
                            .clickable { onSelectCategory(category) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("category_chip_$category")
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

        // Channels Grid
        if (channels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedCategory == "Favorites")
                        "No favorites yet! Tap ❤️ on any channel to add it here."
                    else
                        "No channels found for \"$searchQuery\"",
                    color = TextMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("channels_grid"),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(channels, key = { it.id }) { channel ->
                    val isFav = favoriteNames.contains(channel.name)
                    ChannelCard(
                        channel = channel,
                        isFavorite = isFav,
                        onClick = { onChannelClick(channel) },
                        onToggleFavorite = { onToggleFavorite(channel.name) }
                    )
                }
            }
        }
    }
}

val NeonCyanAccent = Color(0xFF00E5FF)

@Composable
fun ChannelCard(
    channel: Channel,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("channel_card_${channel.id}"),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26313D)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            // Heart favorite button (top-left)
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .testTag("card_fav_btn_${channel.id}")
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) NeonPink else Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }

            // LIVE indicator badge (top-right)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFCC0000))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "LIVE",
                    color = Color.White,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Center Logo and Titles
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = channel.logoUrl,
                    contentDescription = channel.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = channel.name,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)
                )

                Text(
                    text = channel.category,
                    color = TextMuted,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
