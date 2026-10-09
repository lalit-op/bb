# Feather TV & Harsh-IT Radio for Android

A modern Android streaming application built with Kotlin, Jetpack Compose, and Material Design 3.

## Features

- **Live TV Streaming (Feather TV)**:
  - Curated guide of top entertainment, movies, kids, sports, news, and infotainment channels (Sony SAB, Sony Pal, Colors HD, Star Plus, MTV, Zee TV, Cartoon Network, Hungama, Star Sports, Discovery, etc.)
  - Category filtering (All, Favorites, Fun-Ment, Kids, Movies, Sports, Info, News, Music, Regional)
  - Real-time search with channel counter
  - Full playback experience with play/pause, live stream indicators, and fullscreen support
  - Integrated in-player channel drawer for instant channel switching without leaving the player
  - Persistent favorite channels storage

- **Live Radio Player (Harsh-IT Radio)**:
  - Live streaming radio stations covering AIR (FM Gold, Vividh Bharati, Indraprastha), Mirchi, Bollywood hits, 90s retro, devotional (Bhakti), and evergreen classics
  - Media3 / ExoPlayer live audio playback
  - Real-time animated audio visualizer waveform with glowing neon aesthetic
  - Full player modal featuring animated rotating rings, digital clock, station badges, and live equalizer
  - Mini-player bar persistent across the app
  - Sleep timer with countdown (15m, 30m, 45m, 60m)
  - Interactive volume control

- **Curated Favorites**:
  - Unified Favorites tab managing starred TV channels and Radio stations in one place

- **Streaming Hubs (Quick Apps)**:
  - Instant access to partner networks and streaming portals (GDL TV, Momix, SkyFlixer, JioTV Plus, etc.)

## Architecture

- **UI Framework**: Jetpack Compose with Material 3 theming
- **Audio/Video Playback**: AndroidX Media3 (ExoPlayer) + hardware-accelerated WebView fallback
- **Image Loading**: Coil Compose
- **State Management**: MVVM Architecture with `StateFlow` and Kotlin Coroutines
- **Data Persistence**: SharedPreferences
