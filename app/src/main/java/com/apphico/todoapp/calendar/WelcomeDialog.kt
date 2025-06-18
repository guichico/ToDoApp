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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.components.buttons.NormalButton
import com.apphico.designsystem.components.dialogs.DefaultDialog
import com.apphico.designsystem.theme.LightBlue
import com.apphico.designsystem.theme.MediumBlue
import com.apphico.designsystem.theme.SemiMediumBlue
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.todoapp.R

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
                    painter = painterResource(R.drawable.ic_splash),
                    contentDescription = "Bem-vindo",
                )

                Text(
                    text = "Bem-vindo(a) ao Taskhico!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Organize suas tarefas, gerencie suas metas e planeje seu dia com facilidade.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.small))

                NormalButton(
                    text = "Explorar",
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