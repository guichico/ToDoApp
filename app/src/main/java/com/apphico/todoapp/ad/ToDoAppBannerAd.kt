package com.apphico.todoapp.ad

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.apphico.todoapp.BuildConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adView: AdView
) {
    // Ad load does not work in preview mode because it requires a network connection.
    if (LocalInspectionMode.current) {
        Box { Text(text = "Google Mobile Ads preview banner.", modifier.align(Alignment.Center)) }
        return
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { adView }
    )

    // Pause and resume the AdView when the lifecycle is paused and resumed.
    LifecycleResumeEffect(adView) {
        adView.resume()
        onPauseOrDispose { adView.pause() }
    }
}

class ToDoAppBannerAd(val activity: Activity) : AdListener() {

    companion object {
        private val TAG = ToDoAppBannerAd::class.simpleName
    }

    fun getAnchoredAdView(): AdView =
        getAdView(
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                activity,
                360
            )
        )

    fun getAdaptiveAdView(width: Int): AdView =
        getAdView(
            AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(
                activity,
                width
            )
        )

    private fun getAdView(adSize: AdSize): AdView {
        return AdView(activity)
            .apply {
                adUnitId = BuildConfig.ADMOB_BANNER_AD
                setAdSize(adSize)

                adListener = this@ToDoAppBannerAd
                loadAd(AdRequest.Builder().build())
            }
    }

    override fun onAdClicked() {
        // Code to be executed when the user clicks on an ad.
        Log.d(TAG, "Ad was clicked.")
    }

    override fun onAdClosed() {
        // Code to be executed when the user is about to return
        // to the app after tapping on an ad.
        Log.d(TAG, "Ad was closed.")
    }

    override fun onAdFailedToLoad(adError: LoadAdError) {
        // Code to be executed when an ad request fails.
        Log.d(TAG, adError.message)
    }

    override fun onAdImpression() {
        // Code to be executed when an impression is recorded
        // for an ad.
        Log.d(TAG, "Ad recorded an impression.")
    }

    override fun onAdLoaded() {
        // Code to be executed when an ad finishes loading.
        Log.d(TAG, "Ad was loaded.")
    }

    override fun onAdOpened() {
        // Code to be executed when an ad opens an overlay that
        // covers the screen.
        Log.d(TAG, "Ad was open.")
    }
}