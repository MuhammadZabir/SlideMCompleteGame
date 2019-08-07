package game.kcdemos.com.slidem

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import game.kcdemos.com.slidem.database.GameDataModel
import java.text.ParseException
import java.text.SimpleDateFormat


class GameStatsAdapter(private val context: Context, private val gameScoresList: List<GameDataModel>) : RecyclerView.Adapter<GameStatsAdapter.GameViewHolder>() {

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ScoreDate: TextView = view.findViewById(R.id.game_date_txt)
        var ScoreLevel: TextView = view.findViewById(R.id.game_level_txt)
        var ScoreValue: TextView = view.findViewById(R.id.game_score_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_layout, parent, false)

        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val gameScoreItem = gameScoresList[position]

        if (position % 2 == 1) {
//            holder.itemView.setBackgroundColor(Color.parseColor("#50FFFFFF"));
            holder.itemView.setBackgroundResource(R.drawable.score_item_bg);
        }

        holder.ScoreLevel.text = gameScoreItem.ScoreLevel.toString()
        holder.ScoreValue.text = gameScoreItem.ScoreValue.toString()
        holder.ScoreDate.text = showShortDate(gameScoreItem.ScoredOn!!)
    }

    override fun getItemCount(): Int {
        return gameScoresList.size
    }

    private fun showShortDate(dateStr: String): String {
        try {
            val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = fmt.parse(dateStr)
            val fmtOut = SimpleDateFormat("MMM d, yyyy")
            return fmtOut.format(date).toString()
        } catch (e: ParseException) {
            //We can log this exception in real app
            return ""
        }
    }
}