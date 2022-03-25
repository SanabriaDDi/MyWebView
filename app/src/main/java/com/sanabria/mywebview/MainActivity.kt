package com.sanabria.mywebview

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.SearchView
import com.sanabria.mywebview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Private
    private val baseUrl = "https://www.google.com"
    private val searchPath = "/search?q="

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Refresh
        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
        }

        // Search
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    if(URLUtil.isValidUrl(it)){
                        // Es una url
                        binding.webView.loadUrl(it)

                    } else {
                        // No es una url
                        binding.webView.loadUrl("$baseUrl$searchPath$it")
                    }
                }

                return false
            }

        })

        // WebView
        binding.webView.webChromeClient = object : WebChromeClient() {

        }

        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                binding.searchView.setQuery(url, false)
                binding.swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.swipeRefresh.isRefreshing = false
            }

        }

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true

        binding.webView.loadUrl(baseUrl)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}