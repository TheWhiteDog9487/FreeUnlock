package xyz.thewhitedog9487.freeunlock.UI.Composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.thewhitedog9487.freeunlock.R
import xyz.thewhitedog9487.freeunlock.UI.Icon.Icons

data class ScrollBarMetrics(
    val HasOverflow: Boolean = false,
    val AtTop: Boolean = true,
    val AtBottom: Boolean = true,
    val ThumbFraction: Float = 1f,
    val ScrollFraction: Float = 0f )

@Composable
fun rememberScrollBarMetrics(ListState: LazyListState): State<ScrollBarMetrics> {
    return remember(ListState) {
        derivedStateOf {
            val LayoutInfo = ListState.layoutInfo
            val Items = LayoutInfo.visibleItemsInfo
            val ViewportSize = (LayoutInfo.viewportEndOffset - LayoutInfo.viewportStartOffset).toFloat()

            if (Items.isEmpty() || ViewportSize <= 0f) ScrollBarMetrics()
            else {
                val FirstItem = Items.first()
                val LastItem = Items.last()
                val HasOverflow = FirstItem.index > 0 ||
                    LastItem.index < LayoutInfo.totalItemsCount - 1 ||
                    LastItem.offset + LastItem.size > LayoutInfo.viewportEndOffset

                if (HasOverflow == false) ScrollBarMetrics()
                else {
                    val AverageItemSize = Items.sumOf { it.size }.toFloat() / Items.size
                    val ContentSize = AverageItemSize * LayoutInfo.totalItemsCount
                    val MaxScroll = (ContentSize - ViewportSize).coerceAtLeast(1f)
                    val Scrolled = FirstItem.index * AverageItemSize - FirstItem.offset

                    ScrollBarMetrics(
                        HasOverflow = true,
                        AtTop = ListState.canScrollBackward == false,
                        AtBottom = ListState.canScrollForward == false,
                        ThumbFraction = (ViewportSize / ContentSize).coerceIn(0f, 1f),
                        ScrollFraction = (Scrolled / MaxScroll).coerceIn(0f, 1f) ) } } } } }

@Composable
fun VerticalScrollBar(
    ListState: LazyListState,
    Metrics: ScrollBarMetrics,
    modifier: Modifier = Modifier) {

    val TrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
    val ThumbColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    val Density = LocalDensity.current
    val MinThumbHeight = with(Density) { 24.dp.toPx() }
    val ScrollBarWidth = with(Density) { 6.dp.toPx() }

    AnimatedVisibility(
        visible = Metrics.HasOverflow,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier ) {

        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(16.dp)
                .pointerInput(ListState, MinThumbHeight) {

                    detectDragGestures { Change, DragAmount ->
                        Change.consume()
                        val LayoutInfo = ListState.layoutInfo
                        val Items = LayoutInfo.visibleItemsInfo
                        if (Items.isEmpty()) return@detectDragGestures

                        val ViewportSize = (LayoutInfo.viewportEndOffset - LayoutInfo.viewportStartOffset).toFloat()
                        val AverageItemSize = Items.sumOf { it.size }.toFloat() / Items.size
                        val ContentSize = AverageItemSize * LayoutInfo.totalItemsCount
                        if (ContentSize <= ViewportSize) return@detectDragGestures

                        val TrackHeight = size.height.toFloat()
                        val ThumbHeight = (TrackHeight * (ViewportSize / ContentSize))
                            .coerceAtLeast(MinThumbHeight)
                        val MaxThumbTravel = (TrackHeight - ThumbHeight).coerceAtLeast(1f)
                        val MaxScroll = ContentSize - ViewportSize

                        ListState.dispatchRawDelta(
                            DragAmount.y / MaxThumbTravel * MaxScroll ) } } ) {

            val TrackHeight = size.height
            val TrackWidth = size.width
            val ThumbHeight = (TrackHeight * Metrics.ThumbFraction)
                .coerceAtLeast(MinThumbHeight)
                .coerceAtMost(TrackHeight)
            val ThumbTop = (TrackHeight - ThumbHeight) * Metrics.ScrollFraction
            val ThumbLeft = TrackWidth - ScrollBarWidth
            val CornerRadiusPx = CornerRadius(ScrollBarWidth / 2f)

            drawRoundRect(
                color = TrackColor,
                topLeft = Offset(ThumbLeft, 0f),
                size = Size(ScrollBarWidth, TrackHeight),
                cornerRadius = CornerRadiusPx )
            drawRoundRect(
                color = ThumbColor,
                topLeft = Offset(ThumbLeft, ThumbTop),
                size = Size(ScrollBarWidth, ThumbHeight),
                cornerRadius = CornerRadiusPx ) } } }

@Composable
fun ScrollToTopBottomButtons(
    Visible: Boolean,
    AtTop: Boolean,
    AtBottom: Boolean,
    OnScrollToTop: () -> Unit,
    OnScrollToBottom: () -> Unit,
    modifier: Modifier = Modifier) {

    AnimatedVisibility(
        visible = Visible,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
        modifier = modifier ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End ) {

            AnimatedVisibility(
                visible = AtTop == false,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn() +
                    expandVertically(expandFrom = Alignment.Bottom),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut() +
                    shrinkVertically(shrinkTowards = Alignment.Bottom) ) {

                SmallFloatingActionButton(
                    onClick = OnScrollToTop,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer ) {
                    Icon(
                        imageVector = Icons.ArrowUpward,
                        contentDescription = stringResource(R.string.LogHistory_ScrollToTop) ) } }

            AnimatedVisibility(
                visible = AtBottom == false,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn() +
                    expandVertically(expandFrom = Alignment.Top),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut() +
                    shrinkVertically(shrinkTowards = Alignment.Top) ) {

                SmallFloatingActionButton(
                    onClick = OnScrollToBottom,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer ) {
                    Icon(
                        imageVector = Icons.ArrowDownward,
                        contentDescription = stringResource(R.string.LogHistory_ScrollToBottom) ) } } } } }
