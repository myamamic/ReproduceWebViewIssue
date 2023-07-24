package com.myamamic.webview

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel = MainViewModel()

    private lateinit var webView: WebView

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val webViewState = rememberWebViewState(url = "")
            val navigator = rememberWebViewNavigator()

            Scaffold(
                content = { innerPadding ->
                    WebView(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        state = webViewState,
                        navigator = navigator,
                        onCreated = {
                            webView = it

                            observeEffect(webViewNavigator = navigator)
                        },
                    )
                }
            )
        }
    }

    private fun observeEffect(webViewNavigator: WebViewNavigator) {
        viewModel.viewEffect.onEach {
            when (it) {
                is MainViewModel.ViewState.ViewEffect.StartNewPage -> {
                    webViewNavigator.loadUrl(url = it.url)
                }
            }
        }
            .flowWithLifecycle(lifecycle = lifecycle)
            .launchIn(lifecycleScope)
    }
}