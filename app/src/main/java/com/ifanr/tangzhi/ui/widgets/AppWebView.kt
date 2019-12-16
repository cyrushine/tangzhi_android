package com.ifanr.tangzhi.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.webkit.*
import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AppWebView: WebView {

    private val eventSubject = BehaviorSubject.create<Event>()
    val events: Observable<Event>
        get() = eventSubject

    private val lifecycleBinding = object: LifecycleObserver {

        private var paused = false

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResumeEvent() {
            if (paused) {
                onResume()
                resumeTimers()
                paused = false
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPauseEvent() {
            onPause()
            pauseTimers()
            paused = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyEvent() {
            destroy()
        }
    }

    private val client = object: WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return shouldOverrideUrlLoading(view, url?.let { SimpleWebResourceRequest(it) })
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (view != null && request != null) {
                val scheme = request.url?.scheme
                if (scheme?.startsWith(prefix = "http", ignoreCase = true) == true ||
                        scheme?.startsWith(prefix = "https", ignoreCase = true) == true) {
                    return false
                }
            }
            return true
        }

        override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?
        ): Boolean {
            eventSubject.onNext(Event.WebViewCrash)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            url?.also { eventSubject.onNext(Event.UrlChanged(it)) }
        }
    }

    private val chromeClient = object: WebChromeClient() {

        override fun onReceivedTitle(view: WebView?, title: String?) {
            title?.also { eventSubject.onNext(Event.TitleChanged(it)) }
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            eventSubject.onNext(Event.ProgressChanged(newProgress.coerceIn(0, 100)))
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context, attrs, defStyleAttr, privateBrowsing)

    init {
        with(settings) {
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            databaseEnabled = true
            domStorageEnabled = true
            builtInZoomControls = true
            setSupportZoom(true)
            displayZoomControls = false
        }

        webViewClient = client
        webChromeClient = chromeClient
    }


    fun bind(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(lifecycleBinding)
    }

    private class SimpleWebResourceRequest (uriString: String): WebResourceRequest {

        private val url = Uri.parse(uriString)

        override fun getUrl(): Uri {
            return url
        }

        override fun isRedirect(): Boolean {
            return false
        }

        override fun getMethod(): String {
            return "GET"
        }

        override fun getRequestHeaders(): MutableMap<String, String> {
            return mutableMapOf()
        }

        override fun hasGesture(): Boolean {
            return false
        }

        override fun isForMainFrame(): Boolean {
            return true
        }
    }

    sealed class Event {
        // web view process exited, activity should be finish
        object WebViewCrash: Event()
        data class UrlChanged(val url: String): Event()
        data class TitleChanged(val title: String): Event()
        data class ProgressChanged(@IntRange(from = 0, to = 100) val progress: Int): Event()
    }
}