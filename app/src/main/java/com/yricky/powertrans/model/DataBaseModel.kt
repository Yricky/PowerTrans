package com.yricky.powertrans.model

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yricky.powertrans.PTApp
import com.yricky.powertrans.pojo.youdao.Entries
import com.yricky.powertrans.utils.globalCxt
import java.io.File

object DataBaseModel{
    private val cacheDB = DBHelper.writableDatabase
    private val empty:List<Entries> = listOf()
    private object DBHelper:SQLiteOpenHelper(
        globalCxt,
        File(globalCxt.getExternalFilesDir("cache"), "dict.db").path,
    null,2) {

        const val TABLE_NAME = "dict"
        const val COLUMN_WORD = "word"
        const val COLUMN_EXPL = "expl"
        const val COLUMN_TIME = "time"
        const val SQL_CMD_IOR = "INSERT OR REPLACE INTO $TABLE_NAME (${COLUMN_WORD},${COLUMN_EXPL},${COLUMN_TIME}) VALUES (?,?,?)"
        const val SQL_CMD_CRE = "CREATE TABLE $TABLE_NAME($COLUMN_WORD TEXT PRIMARY KEY NOT NULL,$COLUMN_EXPL TEXT NOT NULL,$COLUMN_TIME INTEGER NOT NULL);"
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(SQL_CMD_CRE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            if(oldVersion == 1){
                db?.execSQL("alter table $TABLE_NAME add column $COLUMN_TIME INTEGER")
            }
        }
    }

    fun typeIn(entries: List<Entries>?,then:Runnable? = null){
        PTApp.tpe.execute {
            cacheDB.beginTransaction()
            entries?.forEach {
                val time = System.currentTimeMillis()
                cacheDB.execSQL(
                    DBHelper.SQL_CMD_IOR,
                    arrayOf(it.entry,it.explain,time))
            }
            cacheDB.setTransactionSuccessful()
            cacheDB.endTransaction()
            then?.run()
        }
    }

    fun query(word:String):List<Entries>{
        if(word.isEmpty())
            return empty
        val cursor = cacheDB.query(
            DBHelper.TABLE_NAME,
            arrayOf(DBHelper.COLUMN_WORD,DBHelper.COLUMN_EXPL),
            "${DBHelper.COLUMN_WORD} like ?",
            arrayOf("$word%"),
            null,
            null,
            DBHelper.COLUMN_WORD,
            "512" /* limit */
            )
        cursor.moveToFirst()
        val listSize = cursor.count.coerceAtLeast(0)
        val list = ArrayList<Entries>(listSize).apply {
            (0 until listSize).forEach{ _ ->
                add(Entries().apply {
                    entry = cursor.getString(0)
                    explain = cursor.getString(1)
                })
                cursor.moveToNext()
            }
        }
        cursor.close()
        return list
    }

}