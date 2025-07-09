package com.apphico.todoapp.ad

import android.app.Activity
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import com.apphico.todoapp.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun ShowInterstitialAd() {
    LocalActivity.current?.let { activity ->
        ToDoAppInterstitialAd(activity).showWhenLoaded()
    }
}

class ToDoAppInterstitialAd(val activity: Activity) : FullScreenContentCallback() {

    private var interstitialAd: InterstitialAd? = null

    companion object {
        private val TAG = ToDoAppInterstitialAd::class.simpleName
    }

    fun showWhenLoaded() {
        InterstitialAd.load(
            activity,
            BuildConfig.ADMOB_INTERSTITIAL_AD,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback = this@ToDoAppInterstitialAd
                    interstitialAd?.show(activity)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    interstitialAd = null
                }
            }
        )
    }

    override fun onAdDismissedFullScreenContent() {
        // Called when fullscreen content is dismissed.
        Log.d(TAG, "Ad was dismissed.")
        // Don't forget to set the ad reference to null so you
        // don't show the ad a second time.
        interstitialAd = null
    }

    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
        // Called when fullscreen content failed to show.
        Log.d(TAG, "Ad failed to show.")
        // Don't forget to set the ad reference to null so you
        // don't show the ad a second time.
        interstitialAd = null
    }

    override fun onAdShowedFullScreenContent() {
        // Called when fullscreen content is shown.
        Log.d(TAG, "Ad showed fullscreen content.")
    }

    override fun onAdImpression() {
        // Called when an impression is recorded for an ad.
        Log.d(TAG, "Ad recorded an impression.")
    }

    override fun onAdClicked() {
        // Called when ad is clicked.
        Log.d(TAG, "Ad was clicked.")
    }
}
