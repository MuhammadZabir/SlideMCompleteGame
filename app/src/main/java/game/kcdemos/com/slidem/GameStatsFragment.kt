package game.kcdemos.com.slidem


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import game.kcdemos.com.slidem.database.DBHelper
import game.kcdemos.com.slidem.database.GameDataModel
import kotlinx.android.synthetic.main.fragment_game_stats.view.*
import java.util.*

class GameStatsFragment : Fragment() {

    //    val mainActivity: MainActivity = activity as MainActivity
    var mainGameView: View? = null
    private var mainGameDB: DBHelper? = null
    private var mainGameAdapter: GameStatsAdapter? = null
    private var gameScoresList = ArrayList<GameDataModel>()
    private var gameScoreRecyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(gameLayouyInflater: LayoutInflater, gameContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainActivity: MainActivity = activity as MainActivity

        mainGameView = gameLayouyInflater.inflate(R.layout.fragment_game_stats, gameContainer, false)
        mainGameDB = DBHelper(context)

        gameScoreRecyclerView = mainGameView!!.recycler_main
        gameScoresList.addAll(mainGameDB!!.GameScoreList)

        mainGameAdapter = GameStatsAdapter(context, gameScoresList)
        val mLayoutManager = LinearLayoutManager(context)
        gameScoreRecyclerView!!.layoutManager = mLayoutManager

        gameScoreRecyclerView!!.adapter = mainGameAdapter

        mainGameView!!.btnClearScore.setOnClickListener {
            mainGameDB!!.deleteAllScores()
            mainActivity.showMainFragment()
        }

        return mainGameView
    }

}
