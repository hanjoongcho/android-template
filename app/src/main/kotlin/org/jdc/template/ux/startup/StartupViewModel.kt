package org.jdc.template.ux.startup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.analytics.HitBuilders
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jdc.template.Analytics
import org.jdc.template.BuildConfig
import org.jdc.template.livedata.SingleLiveEvent
import org.jdc.template.util.CoroutineContextProvider
import timber.log.Timber
import javax.inject.Inject

class StartupViewModel
@Inject constructor(
        private val cc: CoroutineContextProvider,
        private val analytics: Analytics
) : ViewModel() {

    val startupProgress = MutableLiveData<StartupProgress>()
    val onStartupFinishedEvent = SingleLiveEvent<Void>()

    fun startup() {
        launch(cc.commonPool) {
            analytics.send(HitBuilders.EventBuilder().setCategory(Analytics.CATEGORY_APP).setAction(Analytics.ACTION_APP_LAUNCH).setLabel(BuildConfig.BUILD_TYPE).build())

            // do startup work here...
            showProgress("Doing stuff")

            onStartupFinishedEvent.postCall()
        }
    }

    private fun showProgress(message: String) = async(cc.ui) {
        Timber.i("Startup progress: [%s]", message)
        val currentProgress = startupProgress.value
        if (currentProgress == null) {
            startupProgress.postValue(StartupProgress(0, message))
        } else {
            startupProgress.postValue(StartupProgress(currentProgress.progress + 1, message))
        }
    }

    companion object {
        const val TOTAL_STARTUP_PROGRESS = 3
    }

    data class StartupProgress(val progress: Int, val message: String = "", val indeterminate: Boolean = false)

}