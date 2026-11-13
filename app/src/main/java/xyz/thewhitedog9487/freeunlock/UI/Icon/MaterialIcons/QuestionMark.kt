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
public val question_mark: ImageVector
    get() {
        if (_question_mark != null) {
            return _question_mark!!
        }
        _question_mark =
            ImageVector.Builder(
                name = "question_mark",
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
                        moveTo(10.6f, 16f)
                        quadToRelative(0f, -2.03f, 0.36f, -2.91f)
                        reflectiveQuadTo(12.5f, 11.15f)
                        quadToRelative(1.03f, -0.9f, 1.56f, -1.56f)
                        reflectiveQuadTo(14.6f, 8.07f)
                        quadToRelative(0f, -1.02f, -0.69f, -1.7f)
                        reflectiveQuadTo(12f, 5.7f)
                        quadToRelative(-1.27f, 0f, -1.94f, 0.77f)
                        reflectiveQuadTo(9.13f, 8.05f)
                        lineTo(6.55f, 6.95f)
                        quadTo(7.08f, 5.35f, 8.48f, 4.17f)
                        reflectiveQuadTo(12f, 3f)
                        quadToRelative(2.63f, 0f, 4.04f, 1.46f)
                        quadToRelative(1.41f, 1.46f, 1.41f, 3.51f)
                        quadToRelative(0f, 1.25f, -0.54f, 2.14f)
                        reflectiveQuadToRelative(-1.69f, 2.01f)
                        quadTo(14f, 13.3f, 13.74f, 13.91f)
                        reflectiveQuadTo(13.48f, 16f)
                        horizontalLineTo(10.6f)
                        close()
                        moveTo(12f, 22f)
                        quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
                        reflectiveQuadTo(10f, 20f)
                        reflectiveQuadToRelative(0.59f, -1.41f)
                        reflectiveQuadTo(12f, 18f)
                        reflectiveQuadToRelative(1.41f, 0.59f)
                        quadTo(14f, 19.18f, 14f, 20f)
                        reflectiveQuadToRelative(-0.59f, 1.41f)
                        reflectiveQuadTo(12f, 22f)
                        close()
                    }
                }
                .build()
        return _question_mark!!
    }

private var _question_mark: ImageVector? = null