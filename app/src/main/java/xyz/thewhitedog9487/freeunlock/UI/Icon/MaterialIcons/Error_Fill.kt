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
public val error_fill: ImageVector
    get() {
        if (_error != null) {
            return _error!!
        }
        _error =
            ImageVector.Builder(
                name = "error",
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
                        moveTo(12.71f, 16.71f)
                        quadTo(13f, 16.43f, 13f, 16f)
                        reflectiveQuadTo(12.71f, 15.29f)
                        reflectiveQuadTo(12f, 15f)
                        reflectiveQuadToRelative(-0.71f, 0.29f)
                        reflectiveQuadTo(11f, 16f)
                        reflectiveQuadToRelative(0.29f, 0.71f)
                        reflectiveQuadTo(12f, 17f)
                        reflectiveQuadToRelative(0.71f, -0.29f)
                        close()
                        moveTo(11f, 13f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(7f)
                        horizontalLineTo(11f)
                        verticalLineToRelative(6f)
                        close()
                        moveToRelative(1f, 9f)
                        quadTo(9.93f, 22f, 8.1f, 21.21f)
                        quadTo(6.28f, 20.43f, 4.93f, 19.08f)
                        quadTo(3.58f, 17.73f, 2.79f, 15.9f)
                        reflectiveQuadTo(2f, 12f)
                        quadTo(2f, 9.92f, 2.79f, 8.1f)
                        quadTo(3.58f, 6.27f, 4.93f, 4.93f)
                        quadTo(6.28f, 3.57f, 8.1f, 2.79f)
                        quadTo(9.93f, 2f, 12f, 2f)
                        reflectiveQuadToRelative(3.9f, 0.79f)
                        reflectiveQuadToRelative(3.17f, 2.14f)
                        quadToRelative(1.35f, 1.35f, 2.14f, 3.17f)
                        quadTo(22f, 9.92f, 22f, 12f)
                        reflectiveQuadToRelative(-0.79f, 3.9f)
                        reflectiveQuadToRelative(-2.14f, 3.17f)
                        quadToRelative(-1.35f, 1.35f, -3.17f, 2.14f)
                        reflectiveQuadTo(12f, 22f)
                        close()
                    }
                }
                .build()
        return _error!!
    }

private var _error: ImageVector? = null