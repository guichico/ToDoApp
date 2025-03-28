package com.apphico.designsystem.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.R
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun NoInternetConnectionView() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.oops),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier
                    .padding(
                        vertical = ToDoAppTheme.spacing.small
                    ),
                text = stringResource(R.string.no_internet_message),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            ToDoAppIcon(
                modifier = Modifier
                    .size(98.dp)
                    .padding(ToDoAppTheme.spacing.extraLarge),
                icon = ToDoAppIcons.icNoInternetConnection,
                contentDescription = "no internet connection"
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun EmptyViewPreview() {
    ToDoAppTheme {
        NoInternetConnectionView()
    }
}