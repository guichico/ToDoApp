package com.apphico.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.apphico.core_repository.calendar.alarm.AlarmHelper
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
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

        // TODO Create dark theme
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(White.toArgb(), White.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(White.toArgb(), White.toArgb()),
        )

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
                        .fillMaxSize()
                        .imePadding(),
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