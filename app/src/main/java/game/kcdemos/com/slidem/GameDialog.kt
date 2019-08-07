package game.kcdemos.com.slidem


import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import pl.droidsonroids.gif.GifImageView


class GameDialog private constructor(builder: Builder) {

    private val title: String?
    private val message: String?
    private val positiveBtnText: String?
    private val negativeBtnText: String?
    private val pBtnColor: String?
    private val nBtnColor: String?
    private val activity: Activity
    private val pListener: GameDialogListener?
    private val nListener: GameDialogListener?
    private val cancel: Boolean
    internal var gifImageResource: Int = 0
    internal var gifImageResourceScale: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE


    init {
        this.title = builder.title
        this.message = builder.message
        this.activity = builder.activity!!
        this.pListener = builder.pListener
        this.nListener = builder.nListener
        this.pBtnColor = builder.pBtnColor
        this.nBtnColor = builder.nBtnColor
        this.positiveBtnText = builder.positiveBtnText
        this.negativeBtnText = builder.negativeBtnText
        this.gifImageResource = builder.gifImageResource
        this.gifImageResourceScale = builder.gifImageResourceScale
        this.cancel = builder.cancel
    }


    class Builder(val activity: Activity?) {
        var title: String? = null
        var message: String? = null
        var positiveBtnText: String? = null
        var negativeBtnText: String? = null
        var pBtnColor: String? = null
        var nBtnColor: String? = null
        var pListener: GameDialogListener? = null
        var nListener: GameDialogListener? = null
        var cancel: Boolean = false
        internal var gifImageResource: Int = 0
        internal var gifImageResourceScale: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }


        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setPositiveBtnText(positiveBtnText: String): Builder {
            this.positiveBtnText = positiveBtnText
            return this
        }

        fun setPositiveBtnBackground(pBtnColor: String): Builder {
            this.pBtnColor = pBtnColor
            return this
        }


        fun setNegativeBtnText(negativeBtnText: String): Builder {
            this.negativeBtnText = negativeBtnText
            return this
        }

        fun setNegativeBtnBackground(nBtnColor: String): Builder {
            this.nBtnColor = nBtnColor
            return this
        }

        //set Positive listener
        fun OnPositiveClicked(pListener: GameDialogListener): Builder {
            this.pListener = pListener
            return this
        }

        //set Negative listener
        fun OnNegativeClicked(nListener: GameDialogListener): Builder {
            this.nListener = nListener
            return this
        }

        fun isCancellable(cancel: Boolean): Builder {
            this.cancel = cancel
            return this
        }

        fun setGifResource(gifImageResource: Int): Builder {
            this.gifImageResource = gifImageResource
            return this
        }

        fun setGifResourceScale(gifScaleMode: ImageView.ScaleType): Builder {
            this.gifImageResourceScale = gifScaleMode
            return this
        }

        fun build(): GameDialog {
            val message1: TextView
            val title1: TextView
//            val iconImg: ImageView
            val nBtn: Button
            val pBtn: Button
            val gifImageView: GifImageView
            val dialog: Dialog
            dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(cancel)
            dialog.setContentView(R.layout.gamedialog_layout)
            dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

            //getting resources
            title1 = dialog.findViewById(R.id.title) as TextView
            message1 = dialog.findViewById(R.id.message) as TextView
            nBtn = dialog.findViewById(R.id.negativeBtn) as Button
            pBtn = dialog.findViewById(R.id.positiveBtn) as Button
            gifImageView = dialog.findViewById(R.id.gifImageView)
            gifImageView.setImageResource(gifImageResource)
            gifImageView.scaleType = gifImageResourceScale

            title1.text = title
            message1.text = message
            if (positiveBtnText != null)
                pBtn.text = positiveBtnText
            if (negativeBtnText != null)
                nBtn.text = negativeBtnText
            if (pBtnColor != null) {
                val bgShape = pBtn.background as GradientDrawable
                bgShape.setColor(Color.parseColor(pBtnColor))
            }
            if (nBtnColor != null) {
                val bgShape = nBtn.background as GradientDrawable
                bgShape.setColor(Color.parseColor(nBtnColor))
            }
            if (pListener != null) {
                pBtn.setOnClickListener {
                    pListener!!.OnClick()
                    dialog.dismiss()
                }
            } else {
                pBtn.setOnClickListener { dialog.dismiss() }
            }

            if (nListener != null) {
                nBtn.visibility = View.VISIBLE
                nBtn.setOnClickListener {
                    nListener!!.OnClick()

                    dialog.dismiss()
                }
            }

            dialog.show()
            return GameDialog(this)

        }
    }

}


interface GameDialogListener {
    fun OnClick()
}
