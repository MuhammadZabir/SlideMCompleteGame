package game.kcdemos.com.slidem.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import game.kcdemos.com.slidem.GAME_DB_NAME
import java.util.*


class DBHelper(context: Context) : SQLiteOpenHelper(context, GAME_DB_NAME, null, 1) {

    val GameScoreList: List<GameDataModel>
        get() {

            deleteScoreUnderTop()

            val gameScores = ArrayList<GameDataModel>()
            val selectQuery = "SELECT  * FROM " + GameDataModel.SCORE_TABLE_NAME + " ORDER BY " +
                    GameDataModel.SCORE_VALUE_COLUMN + " ASC"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val gameScore = GameDataModel()
                    gameScore.ScoreId = cursor.getInt(cursor.getColumnIndex(GameDataModel.SCORE_ID_COLUMN))
                    gameScore.ScoreLevel = cursor.getInt(cursor.getColumnIndex(GameDataModel.SCORE_LEVEL_COLUMN))
                    gameScore.ScoreValue = cursor.getInt(cursor.getColumnIndex(GameDataModel.SCORE_VALUE_COLUMN))
                    gameScore.ScoredOn = cursor.getString(cursor.getColumnIndex(GameDataModel.SCORE_DATE_COLUMN))

                    gameScores.add(gameScore)
                } while (cursor.moveToNext())
            }
            db.close()
            return gameScores
        }

    // return count
    val itemsCount: Int
        get() {
            val countQuery = "SELECT  * FROM " + GameDataModel.SCORE_TABLE_NAME
            val db = this.readableDatabase
            val cursor = db.rawQuery(countQuery, null)

            val count = cursor.count
            cursor.close()
            return count
        }

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GameDataModel.CREATE_TABLE)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + GameDataModel.SCORE_TABLE_NAME)

        // Create tables again
        onCreate(db)
    }

    fun insertScore(scoreLevel: Int, scoreValue: Int): Long {
        // get writable database as we want to write data
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(GameDataModel.SCORE_VALUE_COLUMN, scoreValue)
        values.put(GameDataModel.SCORE_LEVEL_COLUMN, scoreLevel)

        val id = db.insert(GameDataModel.SCORE_TABLE_NAME, null, values)

        // close connection - important
        db.close()

        // return new id
        return id
    }

    /*WILL/MAY NOT BE USED - BUT LET IT BE*/
    fun getScoreById(id: Long): GameDataModel {
        // get readable database as we are not inserting anything
        val db = this.readableDatabase

        val cursor = db.query(GameDataModel.SCORE_TABLE_NAME,
                arrayOf(GameDataModel.SCORE_ID_COLUMN,
                        GameDataModel.SCORE_LEVEL_COLUMN,
                        GameDataModel.SCORE_VALUE_COLUMN,
                        GameDataModel.SCORE_DATE_COLUMN),
                GameDataModel.SCORE_ID_COLUMN + "=?",
                arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()

        // prepare dialog_entry object
        val scoreItem = GameDataModel(
                cursor!!.getInt(cursor.getColumnIndex(GameDataModel.SCORE_ID_COLUMN)),
                cursor.getInt(cursor.getColumnIndex(GameDataModel.SCORE_LEVEL_COLUMN)),
                cursor.getInt(cursor.getColumnIndex(GameDataModel.SCORE_VALUE_COLUMN)),
                cursor.getString(cursor.getColumnIndex(GameDataModel.SCORE_DATE_COLUMN)))

        // close the db connection
        cursor.close()

        return scoreItem
    }

    /*WILL NOT BE USED - YET LET IT BE HERE*/
    fun updateScore(gameDataModel: GameDataModel): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(GameDataModel.SCORE_LEVEL_COLUMN, gameDataModel.ScoreLevel)
        values.put(GameDataModel.SCORE_VALUE_COLUMN, gameDataModel.ScoreValue)

        // updating row
        return db.update(GameDataModel.SCORE_TABLE_NAME, values, GameDataModel.SCORE_ID_COLUMN + " = ?",
                arrayOf(gameDataModel.ScoreId.toString()))
    }

    fun deleteScore(gameDataModel: GameDataModel) {
        val db = this.writableDatabase
        db.delete(GameDataModel.SCORE_TABLE_NAME, GameDataModel.SCORE_ID_COLUMN + " = ?",
                arrayOf(gameDataModel.ScoreId.toString()))
        db.close()
    }

    fun deleteScoreUnderTop() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + GameDataModel.SCORE_TABLE_NAME + " WHERE " + GameDataModel.SCORE_ID_COLUMN + " NOT IN (" +
                " SELECT " + GameDataModel.SCORE_ID_COLUMN + " FROM " + GameDataModel.SCORE_TABLE_NAME + " ORDER BY " +
                GameDataModel.SCORE_VALUE_COLUMN +
                " ASC LIMIT 5);")
        db.close()
    }

    fun deleteAllScores() {
        val db = this.writableDatabase
        db.delete(GameDataModel.SCORE_TABLE_NAME, GameDataModel.SCORE_ID_COLUMN + " > ?",
                arrayOf("0"))
        db.close()
    }

}