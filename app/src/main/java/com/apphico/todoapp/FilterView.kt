package com.apphico.todoapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apphico.core_model.Group
import com.apphico.core_model.Status
import com.apphico.core_model.fakeData.mockedGroups
import com.apphico.designsystem.R
import com.apphico.designsystem.components.buttons.SmallButton
import com.apphico.designsystem.components.icons.ToDoAppIcon
import com.apphico.designsystem.theme.MediumGray
import com.apphico.designsystem.theme.ToDoAppIcons
import com.apphico.designsystem.theme.ToDoAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterView(
    modifier: Modifier = Modifier,
    groupViewModel: GroupViewModel = hiltViewModel(),
    filterViewModel: FilterViewModel,
    isFilterExpanded: State<Boolean>
) {
    val groups = groupViewModel.groups.collectAsState()
    val selectedStatus = filterViewModel.selectedStatus.collectAsState()
    val selectedGroups = filterViewModel.selectedGroups.collectAsState()

    FilterViewContent(
        modifier = modifier,
        isFilterExpanded = isFilterExpanded,
        selectedStatus = selectedStatus,
        onStatusChanged = filterViewModel::onSelectedStatusChanged,
        groups = groups,
        selectedGroups = selectedGroups,
        onGroupSelected = filterViewModel::onSelectedGroupChanged,
        onSearchClicked = filterViewModel::onSearchClicked
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterViewContent(
    modifier: Modifier = Modifier,
    isFilterExpanded: State<Boolean>,
    selectedStatus: State<Status>,
    onStatusChanged: (Status) -> Unit,
    groups: State<List<Group>>,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit,
    onSearchClicked: () -> Unit
) {
    AnimatedVisibility(
        visible = isFilterExpanded.value,
        enter = slideInVertically(tween()) + expandVertically() + fadeIn(),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    vertical = ToDoAppTheme.spacing.small,
                    horizontal = ToDoAppTheme.spacing.large
                )
        ) {
            Text(
                text = stringResource(R.string.status),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.medium))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.small),
                horizontalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.small),
                maxItemsInEachRow = 3
            ) {
                Status.entries.forEach { status ->
                    StatusRow(
                        selectedStatus = selectedStatus,
                        status = status,
                        onStatusChanged = { onStatusChanged(status) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.large))
            Text(
                text = stringResource(R.string.groups),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(ToDoAppTheme.spacing.medium))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.small),
                horizontalArrangement = Arrangement.spacedBy(ToDoAppTheme.spacing.small),
                maxItemsInEachRow = 3
            ) {
                groups.value.forEach { group ->
                    GroupRow(
                        group = group,
                        selectedGroups = selectedGroups,
                        onGroupSelected = onGroupSelected
                    )
                }
            }
            SmallButton(
                modifier = Modifier
                    .padding(top = ToDoAppTheme.spacing.small)
                    .align(Alignment.End),
                onClick = onSearchClicked,
                text = stringResource(R.string.search_btn)
            )
        }
    }
}

@Composable
private fun StatusRow(
    status: Status,
    selectedStatus: State<Status>,
    onStatusChanged: () -> Unit
) {
    val isSelected = selectedStatus.value == status
    val bgColor = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(bgColor, CircleShape)
            .clip(CircleShape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MediumGray
                ),
                shape = CircleShape
            )
            .clickable { onStatusChanged() }
    ) {
        Row(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.medium)
        ) {
            ToDoAppIcon(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically),
                icon = ToDoAppIcons.icCheck,
                contentDescription = "checked",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = ToDoAppTheme.spacing.small,
                        end = ToDoAppTheme.spacing.extraSmall
                    )
                    .align(Alignment.CenterVertically),
                text = stringResource(id = status.title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun GroupRow(
    group: Group,
    selectedGroups: State<List<Group>>,
    onGroupSelected: (Group) -> Unit
) {
    val isSelected = selectedGroups.value.contains(group)
    val bgColor = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(bgColor, CircleShape)
            .clip(CircleShape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MediumGray
                ),
                shape = CircleShape
            )
            .clickable { onGroupSelected(group) }
    ) {
        Row(
            modifier = Modifier
                .padding(ToDoAppTheme.spacing.medium)
        ) {
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .aspectRatio(1f)
                    .background(Color(group.color), CircleShape)
                    .align(Alignment.CenterVertically)
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = ToDoAppTheme.spacing.small,
                        end = ToDoAppTheme.spacing.extraSmall
                    )
                    .align(Alignment.CenterVertically),
                text = group.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

class FilterViewPreviewProvider : PreviewParameterProvider<List<Group>> {
    override val values = sequenceOf(mockedGroups)
}

@PreviewLightDark
@Composable
private fun FilterViewPreview(
    @PreviewParameter(FilterViewPreviewProvider::class) groups: List<Group>
) {
    ToDoAppTheme {
        FilterViewContent(
            isFilterExpanded = remember { mutableStateOf(true) },
            selectedStatus = remember { mutableStateOf(Status.ALL) },
            onStatusChanged = {},
            groups = remember { mutableStateOf(groups) },
            selectedGroups = remember { mutableStateOf(groups.dropLast(3)) },
            onGroupSelected = {},
            onSearchClicked = {}
        )
    }
}
