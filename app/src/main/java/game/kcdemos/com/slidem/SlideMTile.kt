package game.kcdemos.com.slidem

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat

class SlideMTile(
        private val context: Context,
        var tileId: Int,
        private val xLine: Float,
        private val yLine: Float,
        private val boardSize: Float
) {

    private val textSize = boardSize / 6
    private val strokeWidth = if (boardSize / 50 > 1) {
        boardSize / 50
    } else 1F

    fun onDraw(canvas: Canvas, paint: Paint) {
        // if empty block
        if (tileId == 0) {
            paint.style = Paint.Style.FILL
            paint.color = ContextCompat.getColor(context, R.color.colorDeAccent)

            canvas.drawRect(xLine, yLine, xLine + boardSize, yLine + boardSize, paint)
            return
        }

        // fill
        paint.style = Paint.Style.FILL
        paint.color = ContextCompat.getColor(context, R.color.colorSlideTile)

        canvas.drawRect(xLine, yLine, xLine + boardSize, yLine + boardSize, paint)

        // text
        var typeFace: Typeface? = ResourcesCompat.getFont(context, R.font.sniglet)
        paint.setTypeface(typeFace);

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = textSize * context.resources.displayMetrics.scaledDensity
        paint.textAlign = Paint.Align.CENTER
        paint.color = ContextCompat.getColor(context, R.color.colorSlideText)
        canvas.drawText(tileId.toString(), xLine + boardSize / 2, yLine + boardSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint)

//        for tile border - uncomment only if needed
//        paint.style = Paint.Style.STROKE
//        paint.color = ContextCompat.getColor(context, R.color.gameBackground)
//        paint.strokeWidth = strokeWidth * context.resources.displayMetrics.scaledDensity
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//            canvas.drawRoundRect(xLine, yLine, xLine + boardSize, yLine + boardSize, roundingSize, roundingSize, paint)
//        else
//            canvas.drawRect(xLine, yLine, xLine + boardSize, yLine + boardSize, paint)
    }
}
