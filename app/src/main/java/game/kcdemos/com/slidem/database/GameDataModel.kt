package game.kcdemos.com.slidem.database


class GameDataModel {

    var ScoreId: Int = 0
    var ScoreLevel: Int = 0
    var ScoreValue: Int = 0
    var ScoredOn: String? = null //Short Date String

    constructor()

    constructor(score_id: Int, score_level: Int,score_value: Int, scored_on: String) {
        this.ScoreId = score_id
        this.ScoreLevel = score_level
        this.ScoreValue = score_value
        this.ScoredOn= scored_on
    }

    companion object {
        val SCORE_TABLE_NAME = "score_table"

        val SCORE_ID_COLUMN = "score_id"
        val SCORE_LEVEL_COLUMN = "score_level"
        val SCORE_VALUE_COLUMN = "score_value"
        val SCORE_DATE_COLUMN = "score_date"

        val CREATE_TABLE = (
                "CREATE TABLE "
                        + SCORE_TABLE_NAME + "("
                        + SCORE_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + SCORE_LEVEL_COLUMN + " INTEGER,"
                        + SCORE_VALUE_COLUMN + " INTEGER,"
                        + SCORE_DATE_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                        + ")")
    }
}