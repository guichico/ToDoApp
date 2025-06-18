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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.apphico.designsystem.components.buttons.SmallButton
import com.apphico.designsystem.components.dialogs.DefaultDialog
import com.apphico.designsystem.theme.Black
import com.apphico.designsystem.theme.ToDoAppTheme
import com.apphico.designsystem.theme.White
import com.apphico.designsystem.theme.isColorDark

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
                        text = "Como funciona o agendamento de tarefas?",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = ToDoAppTheme.spacing.large),
                        text = "Suas tarefas se adaptam à sua necessidade!\nVeja como as datas funcionam:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Datas são opcionais:\nVocê decide se quer adicioná-las ou não.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
                    )

                    val annotatedString = buildAnnotatedString {
                        withStyle(style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold).toSpanStyle()) {
                            append("Tarefas fixas: ")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append("Se você não definir nenhuma data, a tarefa ficará sempre no topo da sua lista.\n")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold).toSpanStyle()) {
                            append("Tarefas recorrentes: ")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append("Se você definir uma data de início, mas não uma data fim, a tarefa se repetirá indefinidamente.\n")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold).toSpanStyle()) {
                            append("Tarefas com prazo: ")
                        }
                        withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                            append("Se você definir uma data fim, mas não uma data de início, a tarefa será fixada no topo da sua lista como uma tarefa com prazo para ser concluída.")
                        }
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = ToDoAppTheme.spacing.medium),
                        text = annotatedString,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isColorDark(MaterialTheme.colorScheme.background.toArgb())) White else Black
                    )

                    SmallButton(
                        modifier = Modifier
                            .padding(top = ToDoAppTheme.spacing.medium)
                            .align(Alignment.End),
                        onClick = {
                            isDatesExplanationDialogOpen = false
                            onWasDatesExplanationClosed()
                        },
                        text = "Entendi"
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