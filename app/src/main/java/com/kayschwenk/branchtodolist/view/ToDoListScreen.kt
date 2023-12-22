package com.kayschwenk.branchtodolist.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kayschwenk.branchtodolist.data.ToDoItem
import com.kayschwenk.branchtodolist.ui.theme.BranchToDoListTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ToDoListScreen(
    todoList: State<List<ToDoItem>>,
    onCreateClick: (String) -> Unit,
    onClearClick: (ToDoItem) -> Unit
) {
    val openTextEntryDialog = remember { mutableStateOf(false) }
    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    BranchToDoListTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("To Do List")
                    }
                )
            },
        ) { innerPadding ->
            val bottomFade = Brush.verticalGradient(0.9f to Color.Red, 1f to Color.Transparent)
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (todoList.value) {
                    listOf<ToDoItem>() -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "No tasks yet!",
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 32.sp,
                                fontSize = 32.sp
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            state = lazyColumnListState,
                            modifier = Modifier
                                .fadingEdge(bottomFade)
                        ) {
                            itemsIndexed(
                                items = todoList.value,
                                key = { _, item -> item.id },
                            ) { _, toDoItem ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                                        .height(IntrinsicSize.Min)
                                        .animateItemPlacement()
                                ) {
                                    Text(
                                        text = toDoItem.task,
                                        fontSize = 20.sp,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .weight(1f)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(64.dp)
                                            .fillMaxHeight()
                                            .clip(shape = RoundedCornerShape(10.dp))
                                            .selectable(
                                                selected = false,
                                                onClick = { onClearClick(toDoItem) }
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.size(76.dp))
                            }
                        }
                    }
                }
                if (openTextEntryDialog.value) {
                    TextEntryDialog(
                        dismissText = "Cancel",
                        onDismissRequest = { openTextEntryDialog.value = false },
                        confirmText = "Create",
                        onConfirmRequest = { task: String ->
                            onCreateClick(task)
                            openTextEntryDialog.value = false
                            coroutineScope.launch {
                                if (todoList.value.isNotEmpty()) {
                                    lazyColumnListState.animateScrollToItem(todoList.value.size - 1)
                                }
                            }
                        }
                    )
                }
                AnimatedVisibility(
                    visible = openTextEntryDialog.value.not(),
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)
                ) {
                    ExtendedFloatingActionButton(
                        onClick = { openTextEntryDialog.value = true },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "To-Do"
                            )
                        },
                        text = { Text(text = "Add a task") },
                    )
                }
            }
        }
    }
}


/**
 * Fade effect applied to bottom of a container
 */
fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }