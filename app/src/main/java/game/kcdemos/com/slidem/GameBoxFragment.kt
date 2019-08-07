package game.kcdemos.com.slidem

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_game_box.*


class GameBoxFragment : Fragment() {

    private var diffLevel: Int = GAME_DIFF_LEVEL_MIN
    var sharedPref: SharedPreferences? = null
    var gameBoardView: SlideMCanvas? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_game_box, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPref = this.activity.getSharedPreferences(SHAREDPREF_FILENAME, 0)


        diffLevel = sharedPref?.getInt(GAME_DIFF_LEVEL_KEY, 3)!!
        diff_level.text = "Difficulty Level: $diffLevel"
        game_level.progress = diffLevel

        val mainActivity: MainActivity = activity as MainActivity

        gameBoardView = SlideMCanvas(context, diffLevel)
        puzzle_container.addView(gameBoardView)

        game_level.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val min = GAME_DIFF_LEVEL_MIN

                if (i < min) {
                    seekBar.progress = min
                }
                gameLevel = game_level.progress
                sharedPref?.edit()?.putInt(GAME_DIFF_LEVEL_KEY, game_level.progress)!!.apply()
                diff_level.text = "Difficulty Level: " + seekBar.progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //todo nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //todo nothing
            }
        })

        button_new_game.setOnClickListener {
            numClicked = GAME_START_CLICK_RESET_VALUE
            sharedPref?.edit()?.putInt(GAME_DIFF_LEVEL_KEY, game_level.progress)!!.apply()
            gameBoardView!!.initGame(game_level.progress)
            gameBoardView!!.invalidate()

        }
    }
}
