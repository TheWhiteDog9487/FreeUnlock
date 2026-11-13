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
public val adb: ImageVector
    get() {
        if (_adb != null) {
            return _adb!!
        }
        _adb =
            ImageVector.Builder(
                name = "adb",
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
                        moveTo(5f, 11f)
                        verticalLineTo(10f)
                        quadTo(5f, 8.2f, 5.81f, 6.71f)
                        reflectiveQuadTo(8f, 4.27f)
                        lineTo(6.13f, 2.4f)
                        lineTo(7f, 1.5f)
                        lineTo(9.13f, 3.63f)
                        quadTo(9.78f, 3.32f, 10.51f, 3.16f)
                        reflectiveQuadTo(12f, 3f)
                        reflectiveQuadToRelative(1.49f, 0.16f)
                        reflectiveQuadToRelative(1.39f, 0.46f)
                        lineTo(17f, 1.5f)
                        lineToRelative(0.88f, 0.9f)
                        lineTo(16f, 4.27f)
                        quadToRelative(1.38f, 0.95f, 2.19f, 2.44f)
                        reflectiveQuadTo(19f, 10f)
                        verticalLineToRelative(1f)
                        horizontalLineTo(5f)
                        close()
                        moveTo(15.71f, 8.71f)
                        quadTo(16f, 8.42f, 16f, 8f)
                        quadTo(16f, 7.57f, 15.71f, 7.29f)
                        reflectiveQuadTo(15f, 7f)
                        reflectiveQuadTo(14.29f, 7.29f)
                        reflectiveQuadTo(14f, 8f)
                        quadToRelative(0f, 0.42f, 0.29f, 0.71f)
                        reflectiveQuadTo(15f, 9f)
                        reflectiveQuadTo(15.71f, 8.71f)
                        close()
                        moveToRelative(-6f, 0f)
                        quadTo(10f, 8.42f, 10f, 8f)
                        quadTo(10f, 7.57f, 9.71f, 7.29f)
                        reflectiveQuadTo(9f, 7f)
                        quadTo(8.58f, 7f, 8.29f, 7.29f)
                        reflectiveQuadTo(8f, 8f)
                        quadTo(8f, 8.42f, 8.29f, 8.71f)
                        quadTo(8.58f, 9f, 9f, 9f)
                        quadTo(9.43f, 9f, 9.71f, 8.71f)
                        close()
                        moveTo(7.04f, 20.96f)
                        quadTo(5f, 18.93f, 5f, 16f)
                        verticalLineTo(12f)
                        horizontalLineTo(19f)
                        verticalLineToRelative(4f)
                        quadToRelative(0f, 2.93f, -2.04f, 4.96f)
                        reflectiveQuadTo(12f, 23f)
                        quadTo(9.08f, 23f, 7.04f, 20.96f)
                        close()
                    }
                }
                .build()
        return _adb!!
    }

private var _adb: ImageVector? = null