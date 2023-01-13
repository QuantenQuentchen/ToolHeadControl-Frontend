package com.example.toolheadadjust

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Timestamp
import com.example.toolheadadjust.ToolCfg

class SQLiteManager(
    context: Context?,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ToolHeadAdjust.db"
        private const val TABLE_NAME = "ToolCfg"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_DATA = "data"
        private const val COL_X = "x"
        private const val COL_Y = "y"
        private const val COL_LAST_ACCESS = "date"
        private const val COL_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblToolConfig = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_NAME TEXT," +
                "$COL_DATA TEXT," +
                "$COL_X REAL," +
                "$COL_Y REAL," +
                "$COL_LAST_ACCESS LONG," +
                "$COL_DESCRIPTION TEXT)"
        db?.execSQL(createTblToolConfig)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertToolCfg(toolCfg: ToolCfg): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, toolCfg.name)
        //values.put(COL_DATA, toolCfg.data)
        values.put(COL_X, toolCfg.xCord)
        values.put(COL_Y, toolCfg.yCord)
        values.put(COL_LAST_ACCESS, toolCfg.lastAccess.nanos)
        values.put(COL_DESCRIPTION, toolCfg.description)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success
    }

    fun getAllToolCfg(): ArrayList<ToolCfg> {
        val toolCfgList: ArrayList<ToolCfg> = ArrayList<ToolCfg>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val toolCfg = ToolCfg(
                    cursor.getString(cursor.getColumnIndex(COL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndex(COL_X)),
                    cursor.getInt(cursor.getColumnIndex(COL_Y)),
                    Timestamp(cursor.getLong(cursor.getColumnIndex(COL_LAST_ACCESS))),
                    cursor.getInt(cursor.getColumnIndex(COL_ID))
                )
                toolCfgList.add(toolCfg)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return toolCfgList
    }
    fun addToolCfg(toolCfg: ToolCfg) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, toolCfg.name)
        //values.put(COL_DATA, toolCfg.data)
        values.put(COL_X, toolCfg.xCord)
        values.put(COL_Y, toolCfg.yCord)
        values.put(COL_LAST_ACCESS, toolCfg.lastAccess.nanos)
        values.put(COL_DESCRIPTION, toolCfg.description)
        val success = db.insert(TABLE_NAME, null, values)
        Log.d("SQLiteManager", "addToolCfg: $success")
        db.close()
    }
    fun removeToolCfg(toolCfg: ToolCfg) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(toolCfg.id.toString()))
        db.close()
    }
    fun removeToolCfgByID(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
    }
}