package com.example.feathertv.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.feathertv.data.model.Channel
import com.example.feathertv.data.model.RadioStation
import com.example.feathertv.data.model.StreamingApp
import com.example.feathertv.data.repository.ChannelRepository
import com.example.feathertv.data.repository.RadioRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    TV,
    RADIO,
    FAVORITES,
    APPS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val channelRepository = ChannelRepository(application)
    private val radioRepository = RadioRepository(application)

    // Current navigation tab
    private val _currentTab = MutableStateFlow(MainTab.TV)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    // TV State
    private val _tvCategory = MutableStateFlow("All")
    val tvCategory: StateFlow<String> = _tvCategory.asStateFlow()

    private val _tvSearchQuery = MutableStateFlow("")
    val tvSearchQuery: StateFlow<String> = _tvSearchQuery.asStateFlow()

    val favoriteChannelNames = channelRepository.favoriteChannelNames

    private val allChannels = channelRepository.getAllChannels()

    val filteredTvChannels: StateFlow<List<Channel>> = combine(
        _tvCategory,
        _tvSearchQuery,
        favoriteChannelNames
    ) { category, query, favs ->
        var list = when (category) {
            "Favorites" -> allChannels.filter { favs.contains(it.name) }
            "All" -> allChannels
            else -> allChannels.filter { it.category.equals(category, ignoreCase = true) }
        }
        val q = query.trim().lowercase()
        if (q.isNotBlank()) {
            list = list.filter {
                it.name.lowercase().contains(q) || it.category.lowercase().contains(q)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allChannels)

    private val _activeTvChannel = MutableStateFlow<Channel?>(null)
    val activeTvChannel: StateFlow<Channel?> = _activeTvChannel.asStateFlow()

    // Radio State
    private val allStations = radioRepository.getAllStations()
    val allRadioStations: List<RadioStation> get() = allStations

    private val _radioCategory = MutableStateFlow("All")
    val radioCategory: StateFlow<String> = _radioCategory.asStateFlow()

    private val _radioSearchQuery = MutableStateFlow("")
    val radioSearchQuery: StateFlow<String> = _radioSearchQuery.asStateFlow()

    val favoriteRadioIds = radioRepository.favoriteStationIds

    val filteredRadioStations: StateFlow<List<RadioStation>> = combine(
        _radioCategory,
        _radioSearchQuery,
        favoriteRadioIds
    ) { category, query, favs ->
        var list = when (category) {
            "Favorites" -> allStations.filter { favs.contains(it.id) }
            "All" -> allStations
            else -> allStations.filter { it.category.equals(category, ignoreCase = true) }
        }
        val q = query.trim().lowercase()
        if (q.isNotBlank()) {
            list = list.filter {
                it.name.lowercase().contains(q) ||
                        it.slogan.lowercase().contains(q) ||
                        it.category.lowercase().contains(q)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allStations)

    val favoriteChannelsList: StateFlow<List<Channel>> = favoriteChannelNames.combine(
        MutableStateFlow(allChannels)
    ) { favs, channels ->
        channels.filter { favs.contains(it.name) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteStationsList: StateFlow<List<RadioStation>> = favoriteRadioIds.combine(
        MutableStateFlow(allStations)
    ) { favs, stations ->
        stations.filter { favs.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streamingApps: List<StreamingApp> = radioRepository.getApps()

    // Audio ExoPlayer for Radio playback
    private var exoPlayer: ExoPlayer? = null

    private val _currentRadioStation = MutableStateFlow<RadioStation?>(null)
    val currentRadioStation: StateFlow<RadioStation?> = _currentRadioStation.asStateFlow()

    private val _isRadioPlaying = MutableStateFlow(false)
    val isRadioPlaying: StateFlow<Boolean> = _isRadioPlaying.asStateFlow()

    private val _isRadioBuffering = MutableStateFlow(false)
    val isRadioBuffering: StateFlow<Boolean> = _isRadioBuffering.asStateFlow()

    private val _radioVolume = MutableStateFlow(1.0f)
    val radioVolume: StateFlow<Float> = _radioVolume.asStateFlow()

    private val _showRadioSheet = MutableStateFlow(false)
    val showRadioSheet: StateFlow<Boolean> = _showRadioSheet.asStateFlow()

    private val _sleepTimerMinutesLeft = MutableStateFlow<Int?>(null)
    val sleepTimerMinutesLeft: StateFlow<Int?> = _sleepTimerMinutesLeft.asStateFlow()

    private var sleepTimerJob: Job? = null

    init {
        initExoPlayer()
    }

    private fun initExoPlayer() {
        exoPlayer = ExoPlayer.Builder(getApplication()).build().apply {
            volume = _radioVolume.value
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isRadioPlaying.value = isPlaying
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    _isRadioBuffering.value = (playbackState == Player.STATE_BUFFERING)
                }

                override fun onPlayerError(error: PlaybackException) {
                    _isRadioPlaying.value = false
                    _isRadioBuffering.value = false
                }
            })
        }
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    // TV Actions
    fun selectTvCategory(category: String) {
        _tvCategory.value = category
    }

    fun setTvSearchQuery(query: String) {
        _tvSearchQuery.value = query
    }

    fun openTvChannel(channel: Channel) {
        // If radio is playing, pause it so audio does not overlap
        if (_isRadioPlaying.value) {
            pauseRadio()
        }
        _activeTvChannel.value = channel
    }

    fun closeTvPlayer() {
        _activeTvChannel.value = null
    }

    fun toggleTvFavorite(channelName: String) {
        channelRepository.toggleFavorite(channelName)
    }

    // Radio Actions
    fun selectRadioCategory(category: String) {
        _radioCategory.value = category
    }

    fun setRadioSearchQuery(query: String) {
        _radioSearchQuery.value = query
    }

    fun playRadioStation(station: RadioStation) {
        // If TV player was open, close TV so user can listen to radio
        _activeTvChannel.value = null

        _currentRadioStation.value = station
        exoPlayer?.apply {
            stop()
            clearMediaItems()
            val mediaItem = MediaItem.fromUri(station.streamUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
        _isRadioPlaying.value = true
    }

    fun toggleRadioPlayPause() {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
            _isRadioPlaying.value = false
        } else {
            if (player.playbackState == Player.STATE_IDLE || player.currentMediaItem == null) {
                _currentRadioStation.value?.let { playRadioStation(it) }
            } else {
                player.play()
                _isRadioPlaying.value = true
            }
        }
    }

    fun pauseRadio() {
        exoPlayer?.pause()
        _isRadioPlaying.value = false
    }

    fun nextRadioStation() {
        val current = _currentRadioStation.value ?: return
        val list = allStations
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            val nextIndex = (currentIndex + 1) % list.size
            playRadioStation(list[nextIndex])
        }
    }

    fun previousRadioStation() {
        val current = _currentRadioStation.value ?: return
        val list = allStations
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex != -1) {
            val prevIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
            playRadioStation(list[prevIndex])
        }
    }

    fun setRadioVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        _radioVolume.value = clamped
        exoPlayer?.volume = clamped
    }

    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) {
            _sleepTimerMinutesLeft.value = null
            return
        }
        _sleepTimerMinutesLeft.value = minutes
        sleepTimerJob = viewModelScope.launch {
            var remaining = minutes
            while (remaining > 0) {
                delay(60_000)
                remaining -= 1
                _sleepTimerMinutesLeft.value = if (remaining > 0) remaining else null
            }
            pauseRadio()
            _sleepTimerMinutesLeft.value = null
        }
    }

    fun openRadioSheet() {
        _showRadioSheet.value = true
    }

    fun closeRadioSheet() {
        _showRadioSheet.value = false
    }

    fun toggleRadioFavorite(stationId: Int) {
        radioRepository.toggleFavorite(stationId)
    }

    override fun onCleared() {
        super.onCleared()
        sleepTimerJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}
