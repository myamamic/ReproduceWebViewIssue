package com.myamamic.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _viewEffect = MutableSharedFlow<ViewState.ViewEffect>(replay = 1)
    val viewEffect: SharedFlow<ViewState.ViewEffect> = _viewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            _viewEffect.tryEmit(ViewState.ViewEffect.StartNewPage("https://www.google.com/"))
        }
    }

    data class ViewState(val throwable: Throwable?) {

        companion object {

            val INITIAL = ViewState(throwable = null)
        }

        sealed class ViewEffect {
            data class StartNewPage(val url: String) : ViewEffect()
        }
    }
}