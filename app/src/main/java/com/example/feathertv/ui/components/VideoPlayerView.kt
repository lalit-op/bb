package com.example.feathertv.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.feathertv.data.model.Channel
import com.example.feathertv.ui.theme.NeonBlue

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayerView(
    channel: Channel,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    val hasDirectStream = channel.streamUrl.isNotBlank()

    Box(
        modifier = modifier
            .background(Color.Black)
            .testTag("video_player_container"),
        contentAlignment = Alignment.Center
    ) {
        if (hasDirectStream) {
            val exoPlayer = remember(channel.id) {
                ExoPlayer.Builder(context).build().apply {
                    val mediaItem = MediaItem.fromUri(channel.streamUrl)
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = isPlaying
                }
            }

            DisposableEffect(channel.id) {
                onDispose {
                    exoPlayer.release()
                }
            }

            DisposableEffect(isPlaying) {
                exoPlayer.playWhenReady = isPlaying
                onDispose { }
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("exo_player_view"),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }
            )
        } else {
            val urlToLoad = channel.webUrl.ifBlank { "https://jtvxweb.pages.dev/pind?id=${channel.id}" }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("web_player_view"),
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(0xFF000000.toInt())
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            mediaPlaybackRequiresUserGesture = false
                            loadsImagesAutomatically = true
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            userAgentString =
                                "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36"
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                return false
                            }
                        }
                        webChromeClient = WebChromeClient()
                        loadUrl(urlToLoad)
                    }
                },
                update = { webView ->
                    if (webView.url != urlToLoad) {
                        webView.loadUrl(urlToLoad)
                    }
                }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color = NeonBlue,
                    modifier = Modifier.testTag("video_loading_indicator")
                )
            }
        }
    }
}
