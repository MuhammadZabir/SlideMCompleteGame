package game.kcdemos.com.slidem

import android.annotation.SuppressLint
import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.MotionEvent
import android.view.View
import game.kcdemos.com.slidem.database.DBHelper


@SuppressLint("ViewConstructor")


class SlideMCanvas(context: Context, var numBlocks: Int) : View(context) {

    private var gameDBHelper: DBHelper? = null
    private val gamePaintInstance = Paint()
    private var gameContainerWidth: Int = 0

    private var boardSize = 0
    private var gameBoard = Array(numBlocks) { Array(numBlocks) { SlideMTile(context, 0, 0F, 0F, 0F) } }
    private var emptyBlockIndex = Point(numBlocks - 1, numBlocks - 1)

    init {
        gamePaintInstance.isAntiAlias = true
    }

    fun initGame(BoardSize: Int) {
        numBlocks = BoardSize
        gameDBHelper = DBHelper(context)

        gameBoard = Array(numBlocks) { Array(numBlocks) { SlideMTile(context, 0, 0F, 0F, 0F) } }

        emptyBlockIndex = Point(numBlocks - 1, numBlocks - 1)
        boardSize = gameContainerWidth / numBlocks
        var xLine = 0
        var yLine = 0
        var tileId = 1
        for (i in 0 until gameBoard.size) {
            for (j in 0 until gameBoard[0].size) {
                gameBoard[i][j] = SlideMTile(context, tileId, xLine.toFloat(), yLine.toFloat(), boardSize.toFloat())
                tileId++
                tileId %= numBlocks * numBlocks
                xLine += boardSize
            }
            xLine = 0
            yLine += boardSize
        }
        shuffleBlocks()
    }

    private fun shuffleBlocks() {
        val iteration = 100
        for (i in 0 until iteration) {
            val options = mutableListOf<Point>()
            if (emptyBlockIndex.x + 1 < numBlocks) {
                options.add(Point(emptyBlockIndex.x + 1, emptyBlockIndex.y))
            }
            if (emptyBlockIndex.x - 1 >= 0) {
                options.add(Point(emptyBlockIndex.x - 1, emptyBlockIndex.y))
            }
            if (emptyBlockIndex.y + 1 < numBlocks) {
                options.add(Point(emptyBlockIndex.x, emptyBlockIndex.y + 1))
            }
            if (emptyBlockIndex.y - 1 >= 0) {
                options.add(Point(emptyBlockIndex.x, emptyBlockIndex.y - 1))
            }
            options.shuffle()
            val selectedIndex = options[0]
            switchBlock(selectedIndex.x, selectedIndex.y)
        }
    }

    private fun isGameComplete(): Boolean {
        var count = 1
        for (i in 0 until gameBoard.size) {
            for (j in 0 until gameBoard[0].size) {
                if (gameBoard[i][j].tileId != count && count != numBlocks * numBlocks) {
                    return false
                }
                count++
            }
        }

        val id = gameDBHelper!!.insertScore(gameLevel, numClicked)
        return true
    }

    private fun switchBlock(i: Int, j: Int) {
        val ID = gameBoard[i][j].tileId
        gameBoard[i][j].tileId = 0
        gameBoard[emptyBlockIndex.x][emptyBlockIndex.y].tileId = ID
        emptyBlockIndex = Point(i, j)
    }

    @SuppressLint("ResourceType")
    private fun moveBlock(a: Int, b: Int) {

        numClicked += 1

        switchBlock(a, b)
        invalidate()
        if (isGameComplete()) {

            try {
                GameDialog.Builder(getActivity())
                        .setTitle("Slide'M")
                        .setMessage("Do you want to play again?.")
                        .setPositiveBtnText("Yes")
                        .setNegativeBtnText("No")
                        .setPositiveBtnBackground(resources.getString(R.color.colorAccent))
                        .setNegativeBtnBackground(resources.getString(R.color.colorDeAccent))
                        .setGifResource(R.drawable.branding)
                        .isCancellable(false)
                        .OnPositiveClicked(onDialogYesClick())
                        .OnNegativeClicked(onDialogNoClick())
                        .build();
            } catch (e: Exception) {
                Log.d("xxx", e.toString())
            }
        }
    }

    fun onDialogYesClick(): GameDialogListener {
        return object : GameDialogListener {
            override fun OnClick() {
                numClicked = GAME_START_CLICK_RESET_VALUE
                initGame(numBlocks)
                invalidate()
            }
        }
    }

    fun onDialogNoClick(): GameDialogListener {
        return object : GameDialogListener {
            override fun OnClick() {
                (context as MainActivity).fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    private fun getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until gameBoard.size) {
            for (j in 0 until gameBoard[0].size) {
                gameBoard[i][j].onDraw(canvas!!, gamePaintInstance)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        gameContainerWidth = measuredWidth

        if (gameContainerWidth == 0) {
            return
        }

        initGame(numBlocks)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {

            MotionEvent.ACTION_DOWN -> {
                Log.d("event down: ", event.x.toString() + ":" + event.y.toString())
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (boardSize == 0) {
                    return false
                }

                try {
                    val i = (event.y / boardSize).toInt()
                    val j = (event.x / boardSize).toInt()

                    if (i + 1 < numBlocks && i + 1 == emptyBlockIndex.x && j == emptyBlockIndex.y) {
                        moveBlock(i, j)
                    } else if (i - 1 >= 0 && i - 1 == emptyBlockIndex.x && j == emptyBlockIndex.y) {
                        moveBlock(i, j)
                    } else if (j + 1 < numBlocks && i == emptyBlockIndex.x && j + 1 == emptyBlockIndex.y) {
                        moveBlock(i, j)
                    } else if (j - 1 >= 0 && i == emptyBlockIndex.x && j - 1 == emptyBlockIndex.y) {
                        moveBlock(i, j)
                    }

                    Log.d("event up: ", event.x.toString() + ":" + event.y.toString())
                } catch (e: ArrayIndexOutOfBoundsException) {
                    //do nothing
                }
            }
        }

        return super.onTouchEvent(event)
    }
}
