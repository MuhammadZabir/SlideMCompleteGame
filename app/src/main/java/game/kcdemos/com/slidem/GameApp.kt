package game.kcdemos.com.slidem

import android.app.Application

/*Global Constants*/
const val SHAREDPREF_FILENAME = "game.kcdemos.com.slidem.game_prefs"
const val GAME_START_CLICK_RESET_VALUE: Int = 0
const val GAME_DIFF_LEVEL_MIN: Int = 3
const val GAME_DIFF_LEVEL_KEY: String = "Game_Diff_Level"

internal var gameLevel = GAME_DIFF_LEVEL_MIN
internal var numClicked = GAME_START_CLICK_RESET_VALUE
internal val GAME_DB_NAME = "slidem_db"

class GameApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}