package designsystem.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import designsystem.SSurface
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Action(
    val isActive: Boolean = false,
    val clickable: Boolean = true,
    val content: @Composable () -> Unit
)

@Composable
fun SHorizontalCompoundButton(
    modifier: Modifier = Modifier,
    actions: List<Action> = emptyList()
) {
    val density = LocalDensity.current
    val containerHeight = remember { mutableStateOf(0.dp) }
    val activeIndex = rememberUpdatedState(actions.indexOfFirst { it.isActive })

    Row(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(BorderStroke(1.dp, Color(0xffCCCCCC)), RoundedCornerShape(6.dp))
            .onSizeChanged {
                containerHeight.value = (it.height / density.density).dp
            }
    ) {
        actions.mapIndexed { i, action ->
            val isNotFirstIndex = i != 0
            val isPreviousIndexActive = activeIndex.value > -1 && i == activeIndex.value + 1
            val isCurrentIndexActive = action.isActive

            if (isNotFirstIndex) {
                if (isPreviousIndexActive) {
                    ActiveStroke(
                        modifier = Modifier
                            .height(containerHeight.value),
                    )
                } else {
                    NormalStroke(
                        Modifier
                            .height(containerHeight.value),
                    )
                }
                if (isPreviousIndexActive || isCurrentIndexActive) {
                    SeparatorStroke(
                        modifier = Modifier
                            .height(containerHeight.value),
                    )
                } else {
                    NormalStroke(
                        Modifier
                            .height(containerHeight.value),
                    )
                }
                if (!isCurrentIndexActive && !isPreviousIndexActive) {
                    SeparatorStroke(
                        modifier = Modifier
                            .height(containerHeight.value),
                    )
                } else if (isPreviousIndexActive) {
                    NormalStroke(
                        modifier = Modifier
                            .height(containerHeight.value),
                    )
                } else if (isCurrentIndexActive) {
                    ActiveStroke(
                        modifier = Modifier
                            .height(containerHeight.value),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(if (action.isActive) Color(0xfff2f2f2) else Color.White),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                action.content.invoke()
            }
        }
    }
}

@Composable
fun ActiveStroke(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .width(1.dp)
            .background(color = Color(0xffe8e8e8))
    )
}

@Composable
fun NormalStroke(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .width(1.dp)
            .background(color = Color(0xffebebeb))
    )
}

@Composable
fun SeparatorStroke(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .width(1.dp)
            .background(
                color = Color(0xffcccccc)
            )
    )
}

@Preview
@Composable
private fun HorizontalCompoundButtonPreview() {
    SSurface {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SHorizontalCompoundButton(
                modifier = Modifier
                    .width(300.dp),
                actions = listOf(
                    Action(
                        isActive = true
                    ) {
                        Text("Button1")
                    },
                    Action {
                        Text("Button2")
                    },
                    Action {
                        Text("Button3")
                    },
                    Action {
                        Text("Button3")
                    }
                )
            )
            SHorizontalCompoundButton(
                modifier = Modifier
                    .width(300.dp),
                actions = listOf(
                    Action {
                        Text("Button1")
                    },
                    Action(
                        isActive = true
                    ) {
                        Text("Button2")
                    },
                    Action {
                        Text("Button3")
                    },
                    Action {
                        Text("Button3")
                    }
                )
            )
            SHorizontalCompoundButton(
                modifier = Modifier
                    .width(300.dp),
                actions = listOf(
                    Action {
                        Text("Button1")
                    },
                    Action {
                        Text("Button2")
                    },
                    Action(
                        isActive = true
                    ) {
                        Text("Button3")
                    },
                    Action {
                        Text("Button3")
                    }
                )
            )
            SHorizontalCompoundButton(
                modifier = Modifier
                    .width(300.dp),
                actions = listOf(
                    Action {
                        Text("Button1")
                    },
                    Action {
                        Text("Button2")
                    },
                    Action {
                        Text("Button3")
                    },
                    Action(
                        isActive = true
                    ) {
                        Text("Button3")
                    }
                )
            )
        }
    }
}