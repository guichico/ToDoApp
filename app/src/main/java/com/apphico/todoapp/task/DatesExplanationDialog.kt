package com.apphico.todoapp.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.SmallButton
import com.apphico.designsystem.components.dialogs.DefaultDialog
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun DatesExplanationDialog(
    onWasDatesExplanationClosed: () -> Unit
) {
    var isDatesExplanationDialogOpen by remember { mutableStateOf(true) }

    if (isDatesExplanationDialogOpen) {
        DefaultDialog(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.large),
            onDismissRequest = { isDatesExplanationDialogOpen = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = ToDoAppTheme.spacing.large,
                        top = ToDoAppTheme.spacing.large,
                        end = ToDoAppTheme.spacing.large
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ToDoAppTheme.spacing.medium)
                ) {
                    Text(
                        text = stringResource(R.string.dates_explanation_1),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = ToDoAppTheme.spacing.large),
                        text = stringResource(R.string.dates_explanation_2),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.dates_explanation_3),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    .toSpanStyle()
                        ) {
                            append(stringResource(R.string.dates_explanation_4))
                            append(" ")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append(stringResource(R.string.dates_explanation_5))
                        }
                        withStyle(
                            style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    .toSpanStyle()
                        ) {
                            append(stringResource(R.string.dates_explanation_6))
                            append(" ")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append(stringResource(R.string.dates_explanation_7))
                        }
                        withStyle(
                            style =
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    .toSpanStyle()
                        ) {
                            append(stringResource(R.string.dates_explanation_8))
                            append(" ")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append(stringResource(R.string.dates_explanation_9))
                        }
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ToDoAppTheme.spacing.medium),
                        text = annotatedString,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    SmallButton(
                        modifier = Modifier
                            .padding(top = ToDoAppTheme.spacing.medium)
                            .align(Alignment.End),
                        onClick = {
                            isDatesExplanationDialogOpen = false
                            onWasDatesExplanationClosed()
                        },
                        text = stringResource(R.string.got_it)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeDialogPreview() {
    ToDoAppTheme {
        DatesExplanationDialog(
            onWasDatesExplanationClosed = {}
        )
    }
}
