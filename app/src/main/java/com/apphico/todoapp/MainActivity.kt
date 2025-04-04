package com.apphico.todoapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.task.navigateToAddEditTask
import com.apphico.todoapp.utils.getTask
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var intentListener: Consumer<Intent>? = null
    private var pendingIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()

        // TODO Deprecated
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        setContent {
            ToDoAppTheme {
                val navController = rememberNavController()

                DisposableEffect(navController) {
                    intentListener = Consumer { intent ->
                        processNewIntent(intent, navController)
                        pendingIntent = null
                    }
                    addOnNewIntentListener(intentListener!!)
                    onDispose {
                        intentListener?.let {
                            removeOnNewIntentListener(it)
                            intentListener = null
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold(
                        navController = navController
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intentListener == null && intent.data.toString() == AlarmHelper.ALARM_NOTIFICATION_DEEPLINK) {
            pendingIntent = intent
        }
    }

    private fun processNewIntent(
        intent: Intent,
        navController: NavHostController
    ) {
        when (intent.action) {
            AlarmHelper.OPEN_TASK_ACTION -> {
                navController.navigateToAddEditTask(task = intent.getTask(), isFromIntent = true)
            }
        }
    }
}