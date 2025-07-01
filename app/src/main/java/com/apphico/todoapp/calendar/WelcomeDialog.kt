package com.apphico.todoapp.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.components.dialogs.DefaultDialog
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.LightBlue
import com.apphico.designsystem.theme.MainText
import com.apphico.designsystem.theme.MediumBlue
import com.apphico.designsystem.theme.SemiMediumBlue
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White

@Composable
fun WelcomeDialog(
    onExploreClicked: () -> Unit
) {
    var isWelcomeDialogOpen by remember { mutableStateOf(true) }

    if (isWelcomeDialogOpen) {
        DefaultDialog(
            onDismissRequest = { isWelcomeDialogOpen = false },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            val colorStops = arrayOf(
                0.0f to MediumBlue,
                0.30f to SemiMediumBlue,
                1f to LightBlue
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colorStops = colorStops))
                    .padding(ToDoAppTheme.spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.medium)
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .rotate(10f),
                    painter = painterResource(com.apphico.todoapp.R.drawable.ic_splash),
                    contentDescription = null,
                )

                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MainText,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.welcome_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

                NormalButton(
                    text = stringResource(R.string.explore),
                    textColor = White,
                    containerColor = MainText,
                    onClick = {
                        isWelcomeDialogOpen = false
                        onExploreClicked()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeDialogPreview() {
    ToDoAppTheme {
        WelcomeDialog(
            onExploreClicked = {}
        )
    }
}