package com.apphico.designsystem.components.progress

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.apphico.designsystem.theme.ProgressBlue
import com.apphico.designsystem.theme.White
import java.text.DecimalFormat

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: () -> Float,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp),
            progress = progress,
            color = if (isSystemInDarkTheme()) ProgressBlue.copy(alpha = 0.5f) else ProgressBlue,
            trackColor = if (isSystemInDarkTheme()) ProgressBlue else ProgressBlue.copy(alpha = 0.5f),
            strokeCap = StrokeCap.Butt,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = "${DecimalFormat("0.00").format(progress() * 100)}%",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            color = White
        )
    }
}

class ProgressBarPreviewProvider : PreviewParameterProvider<Float> {
    override val values = sequenceOf(0.025f, 0.25f, 0.5f, 1f)
}

@PreviewLightDark
@Composable
fun ProgressBarPreview(
    @PreviewParameter(ProgressBarPreviewProvider::class) progress: Float
) {
    ProgressBar(
        progress = { progress }
    )
}