package com.example.farmmanager


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//데이터를 튜플단위로 사용하기 위한 데이터클래스
data class Logging(var id:Long?, var content: String, var datetime:Long)
data class Memo(var id: Long?, var content: String, var datetime: Long)



//현 프로그램의 전반적인 DB를 관리하기 위한 클래스
class MainDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    //SQLiteOpenHelper: SQLite와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
    //onCreate와 onUpgrade 메서드는 자동으로 생성


    //onCreate: DB가 생성되어있지 않다면 이 메서드를 통해 DB생성
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 구문: logging(id:long, content: text, datetime: long)
        val create = "create table logging (id long primary key,content text, datetime long)"
        db?.execSQL(create)
    }


    //onUpgrade: 버전 변경사항이 있을 경우에 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS logging") //logging이라는 테이블이 이미 있으면 제거
        onCreate(db) //테이블 재생성
    }


    //select: Logging이 담긴 리스트를 리턴
    @SuppressLint("Range") //설정 SDK 버전 이후의 API를 경고없이 사용할 수 있게 해주는 어노테이션
    fun select(): MutableList<Logging> {
        val list = mutableListOf<Logging>() //리턴할 리스트 생성

        val cursor = readableDatabase.rawQuery("select * from logging", null)
        //readableDatabase.rawQuery(): 쿼리를 담아서 실행하면 cursor 형태로 값이 반환
        //cursor: 데이터 요소 + DB에서의 위치에 대한 데이터
        //따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능

        //moveToNext() 다음 줄에 사용가능한 레코드가 있으면 true를 리턴하고 커서를 다음위치로 이동 / 없으면 false
        while (cursor.moveToNext()) {
            //테이블에 정의된 3개의 컬럼에서 값을 꺼낸 다음 변수에 담는다.
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Logging(id, content, datetime))
        }

        cursor.close()
        readableDatabase.close()

        return list
    }


    //insert: Logging 타입 매개변수를 받으면, 그 내부값들을 DB에 저장
    fun insert(logging: Logging) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content", logging.content)
        values.put("datetime", logging.datetime)

        //DB의 logging 테이블에 ContentValues 내 정보들을 삽입
        writableDatabase.insert("logging", null, values)
        writableDatabase.close()
    }


    //update: DB의 데이터를 새로운 Logging으로 갱신
    fun update(logging: Logging) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content", logging.content)
        values.put("datetime", logging.datetime)

        writableDatabase.update("logging", values, "id=${logging.id}", null)
        writableDatabase.close()
    }


    //delete: Logging 타입 매개변수를 받으면, DB에서 일치하는 데이터를 제거
    fun delete(logging: Logging) {
        val delete = "delete from memo where id = ${logging.id}"

        writableDatabase.execSQL(delete)
        writableDatabase.close()
    }
}



class MemoDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    //SQLiteOpenHelper: SQLite와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
    //onCreate와 onUpgrade 메서드는 자동으로 생성


    //onCreate: DB가 생성되어있지 않다면 이 메서드를 통해 DB생성
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 구문: memo(id:long, content: text, datetime: long)
        val create = "create table memo (id long primary key,content text,datetime long)"
        db?.execSQL(create)
    }


    //onUpgrade: 버전 변경사항이 있을 경우에 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS memo") //memo라는 테이블이 이미 있으면 제거
        onCreate(db) //테이블 재생성
    }

    //select: Memo가 담긴 리스트를 리턴
    @SuppressLint("Range") //설정 SDK 버전 이후의 API를 경고없이 사용할 수 있게 해주는 어노테이션
    fun selectMemo(): MutableList<Memo> {
        val list = mutableListOf<Memo>() //리턴할 리스트 생성

        val cursor = readableDatabase.rawQuery("select * from memo", null)
        //readableDatabase.rawQuery(): 쿼리를 담아서 실행하면 cursor 형태로 값이 반환
        //cursor: 데이터 요소 + DB에서의 위치에 대한 데이터
        //따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능

        //moveToNext() 다음 줄에 사용가능한 레코드가 있으면 true를 리턴하고 커서를 다음위치로 이동 / 없으면 false
        while (cursor.moveToNext()) {
            //테이블에 정의된 3개의 컬럼에서 값을 꺼낸 다음 변수에 담는다.
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Memo(id, content, datetime))
        }

        cursor.close()
        readableDatabase.close()

        return list
    }


    //insert: Logging 타입 매개변수를 받으면, 그 내부값들을 DB에 저장
    fun insert(memo: Memo) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        //DB의 logging 테이블에 ContentValues 내 정보들을 삽입
        writableDatabase.insert("memo", null, values)
        writableDatabase.close()
    }


    //update: DB의 데이터를 새로운 Memo로 갱신
    fun update(memo: Memo) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        writableDatabase.update("memo", values, "id=${memo.id}", null)
        writableDatabase.close()
    }

    //delete: Memo 타입 매개변수를 받으면, DB에서 일치하는 데이터를 제거
    fun delete(memo: Memo) {
        val delete = "delete from memo where id = ${memo.id}"

        writableDatabase.execSQL(delete)
        writableDatabase.close()
    }
}

