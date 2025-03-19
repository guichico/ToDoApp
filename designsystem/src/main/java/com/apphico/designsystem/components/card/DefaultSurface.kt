package com.apphico.designsystem.components.card

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.emptyLambda
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun DefaultSurface(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = emptyLambda,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(CardDefaults.shape)
            .clickable(
                enabled = onClick != emptyLambda,
                onClick = onClick
            ),
        color = MaterialTheme.colorScheme.onPrimary,
        shape = CardDefaults.shape,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
        content = content
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DefaultSurfacePreview() {
    ToDoAppTheme {
        DefaultSurface(
            modifier = Modifier
                .fillMaxSize(),
            onClick = {},
            content = {}
        )
    }
}
