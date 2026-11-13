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
public val commit: ImageVector
    get() {
        if (_commit != null) {
            return _commit!!
        }
        _commit =
            ImageVector.Builder(
                name = "commit",
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
                        moveTo(8.81f, 15.86f)
                        quadTo(7.45f, 14.73f, 7.1f, 13f)
                        horizontalLineTo(2f)
                        verticalLineTo(11f)
                        horizontalLineTo(7.1f)
                        quadTo(7.45f, 9.27f, 8.81f, 8.14f)
                        reflectiveQuadTo(12f, 7f)
                        reflectiveQuadToRelative(3.19f, 1.14f)
                        reflectiveQuadTo(16.9f, 11f)
                        horizontalLineTo(22f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(16.9f)
                        quadToRelative(-0.35f, 1.72f, -1.71f, 2.86f)
                        reflectiveQuadTo(12f, 17f)
                        quadTo(10.18f, 17f, 8.81f, 15.86f)
                        close()
                        moveTo(12f, 15f)
                        quadToRelative(1.25f, 0f, 2.13f, -0.88f)
                        reflectiveQuadTo(15f, 12f)
                        reflectiveQuadTo(14.13f, 9.88f)
                        reflectiveQuadTo(12f, 9f)
                        reflectiveQuadTo(9.88f, 9.88f)
                        reflectiveQuadTo(9f, 12f)
                        reflectiveQuadToRelative(0.88f, 2.13f)
                        reflectiveQuadTo(12f, 15f)
                        close()
                    }
                }
                .build()
        return _commit!!
    }

private var _commit: ImageVector? = null