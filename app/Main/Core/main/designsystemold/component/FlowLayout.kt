package designsystemold.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.ui.tooling.preview.Preview
import utils.intersects

@Immutable
class FlowLayoutScope {
    internal val leftMagnitude = mutableFloatStateOf(0f)
    internal val rightMagnitude = mutableFloatStateOf(0f)

    private val _items = MutableStateFlow<List<Pair<Any, @Composable () -> Unit>>>(emptyList())
    val items = _items.asStateFlow()

    private val _aspectRatios = MutableStateFlow<List<Pair<Any, Float>>>(emptyList())
    val aspectRatios = _aspectRatios.asStateFlow()

    fun item(key: Any, ratio: Float, bloc: @Composable () -> Unit) {
        _aspectRatios.update {
            it.dropWhile { it.first == key } + (key to ratio)
        }

        _items.update {
            it.dropWhile { it.first == key } + (key to bloc)
        }
    }
}

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    enterTransition: EnterTransition = slideInVertically { it },
    builder: FlowLayoutScope.() -> Unit = {}
) {
    val isPreviewMode = LocalInspectionMode.current
    val scope = FlowLayoutScope().apply(builder)
    val density = LocalDensity.current
    val parentWidth = remember { mutableStateOf(0.dp) }
    val sectionWidth by rememberUpdatedState(
        (parentWidth.value
                - contentPadding.calculateStartPadding(LayoutDirection.Ltr)
                - contentPadding.calculateEndPadding(LayoutDirection.Rtl)
                - horizontalGap)
                / 2
    )
    val parentRect = remember { mutableStateOf(Rect.Zero) }

    Column(
        modifier = modifier
            .onSizeChanged {
                parentWidth.value = (it.width / density.density).dp
            }
            .onGloballyPositioned {
                parentRect.value = it.boundsInWindow()
            }
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding())
                .wrapContentHeight()
        ) {
            check(sectionWidth > 0.dp) { return@Box }

            val items by scope.items.collectAsStateWithLifecycle()
            val ratios by scope.aspectRatios.collectAsStateWithLifecycle()

            items.forEachIndexed { index, item ->
                key(index, item.first) {
                    val ratio = remember { ratios.first { it.first == item.first }.second }
                    val isPreferLeft = remember { scope.leftMagnitude.value <= scope.rightMagnitude.value }
                    val topOffset =
                        remember { if (isPreferLeft) scope.leftMagnitude.value else scope.rightMagnitude.value }
                    val shouldApplyGap = remember { topOffset > 0f }
                    val isShownBefore = remember(item.first) { mutableStateOf(false) }
                    val isVisibleOnScreen = remember(item.first) { mutableStateOf(false) }

                    if (isPreferLeft)
                        scope.leftMagnitude.value += (sectionWidth.value * density.density) / ratio
                    else
                        scope.rightMagnitude.value += (sectionWidth.value * density.density) / ratio

                    if (shouldApplyGap) {
                        if (isPreferLeft)
                            scope.leftMagnitude.value += verticalGap.value * density.density
                        else
                            scope.rightMagnitude.value += verticalGap.value * density.density
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = (topOffset / density.density).dp)
                            .padding(top = if (shouldApplyGap) verticalGap else 0.dp)
                            .padding(
                                start = if (isPreferLeft) contentPadding.calculateLeftPadding(LayoutDirection.Rtl) else 0.dp,
                                end = if (!isPreferLeft) contentPadding.calculateLeftPadding(LayoutDirection.Ltr) else 0.dp,
                            )
                            .width(sectionWidth)
                            .aspectRatio(ratio)
                            .align(if (isPreferLeft) Alignment.TopStart else Alignment.TopEnd)
                            .onGloballyPositioned {
                                val bounds = it.boundsInWindow()
                                val boundsVerRange = bounds.top..bounds.bottom
                                val parentVerRange = parentRect.value.top..parentRect.value.bottom
                                isVisibleOnScreen.value = boundsVerRange intersects parentVerRange
                            }
                    ) {
                        this@Column.AnimatedVisibility(
                            visible = isVisibleOnScreen.value && !isPreviewMode,
                            enter = if (!isShownBefore.value) enterTransition else EnterTransition.None,
                            exit = ExitTransition.None
                        ) {
                            item.second.invoke()

                            LaunchedEffect(Unit) {
                                isShownBefore.value = true
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    FlowLayout(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        horizontalGap = 16.dp,
        verticalGap = 16.dp,
        contentPadding = PaddingValues(16.dp),
        scrollState = rememberScrollState()
    ) {
        item(1, 1f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item1"
            )
        }
        item(2, 1 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item2"
            )
        }
        item(3, 3 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item3"
            )
        }
        item(4, 10 / 16f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item4"
            )
        }
        item(5, 3 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item5"
            )
        }
        item(6, 3 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item6"
            )
        }

        item(7, 3 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item7"
            )
        }
        item(8, 10 / 16f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item8"
            )
        }
        item(9, 3 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item9"
            )
        }
        item(10, 3 / 2f) {
            Text(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                text = "item10"
            )
        }
    }
}