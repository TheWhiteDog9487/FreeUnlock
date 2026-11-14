package xyz.thewhitedog9487.freeunlock.UI.Icon.MaterialIcons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val arrow_upward: ImageVector
    get() {
        if (_arrow_upward != null) {
            return _arrow_upward!!
        }
        _arrow_upward =
            ImageVector.Builder(
                name = "arrow_upward",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(11f, 20f)
                        verticalLineTo(7.82f)
                        lineToRelative(-5.6f, 5.6f)
                        lineTo(4f, 12f)
                        lineTo(12f, 4f)
                        lineToRelative(8f, 8f)
                        lineToRelative(-1.4f, 1.42f)
                        lineTo(13f, 7.82f)
                        verticalLineTo(20f)
                        horizontalLineTo(11f)
                        close()
                    }
                }
                .build()
        return _arrow_upward!!
    }

private var _arrow_upward: ImageVector? = null