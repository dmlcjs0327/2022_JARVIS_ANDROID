package com.example.a2022_jarvis_android


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Logging(var id:Long?, var content:String, var content2:String, var datetime:Long) {}


class SqliteHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase?) {

        val create = "create table logging (id long primary key,content text,content2 text, datetime long)"

        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS logging")
        onCreate(db)
    }


    fun insert(logging:Logging){
        val values = ContentValues()
        values.put("content",logging.content)
        values.put("content2",logging.content2)
        values.put("datetime",logging.datetime)
        val wd = writableDatabase
        wd.insert("logging",null,values)
        wd.close()
    }

    @SuppressLint("Range")
    fun select():MutableList<Logging>{
        val list = mutableListOf<Logging>()
        val selectAll = "select * from logging"
        val rd = readableDatabase

        val cursor = rd.rawQuery(selectAll,null)

        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val content2 = cursor.getString(cursor.getColumnIndex("content2"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Logging(id,content,content2,datetime))
        }
        cursor.close()
        rd.close()

        return list
    }

    fun update(logging:Logging){
        val values = ContentValues()

        values.put("content",logging.content)
        values.put("datetime",logging.datetime)

        val wd = writableDatabase
        wd.update("memo",values,"id=${logging.id}",null)
        wd.close()
    }

    fun delete(logging:Logging){
        val delete = "delete from Logging where id = ${logging.id}"
        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }

}