package com.apphico.todoapp.focus

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.FocusMode
import com.apphico.designsystem.components.list.MainLazyList
import com.apphico.designsystem.focus.FocusCard
import com.apphico.designsystem.theme.ToDoAppTheme

@Composable
fun FocusScreen(
    focusViewModel: FocusViewModel = hiltViewModel(),
    navigateToAddEditFocus: (FocusMode?) -> Unit
) {
    val routines = focusViewModel.routines.collectAsState()

    FocusScreenContent(
        routines = routines,
        navigateToAddEditFocus = navigateToAddEditFocus
    )
}

@Composable
fun FocusScreenContent(
    routines: State<List<FocusMode>>,
    navigateToAddEditFocus: (FocusMode?) -> Unit
) {
    MainLazyList(
        listState = rememberLazyListState(),
        onAddClicked = {}
    ) {
        items(routines.value) {
            FocusCard(
                focus = it,
                onClick = { navigateToAddEditFocus(null) }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun FocusScreenPreview() {
    ToDoAppTheme {
        FocusScreenContent(
            routines = remember { mutableStateOf(emptyList<FocusMode>()) },
            navigateToAddEditFocus = {}
        )
    }
}