package game.kcdemos.com.slidem

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_game_start.*

class GameStartFragment : Fragment() {

    var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = this.activity.getSharedPreferences(SHAREDPREF_FILENAME, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_game_start, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainActivity: MainActivity = activity as MainActivity
        btnStart.setOnClickListener {

            //Load last game level - if not start at 3
            gameLevel = sharedPref?.getInt(GAME_DIFF_LEVEL_KEY, GAME_DIFF_LEVEL_MIN)!!
            mainActivity.showGameFragment(gameLevel)
        }

        btnStats.setOnClickListener {
            mainActivity.showStatsFragment()
        }
    }
}
