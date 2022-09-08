//SQLite 를 통해 DB 를 사용할 수 있게 해주는 클래스 모음: MainDBHelper, LogDBHelper, DiaryDBHelper
package com.example.farmmanager


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



//데이터를 튜플단위로 사용하기 위한 데이터클래스
data class Main(var variable:Long, var value: String)
data class Logging(var id:Long?, var content: String, var datetime:Long)
data class Diarying(var id: Long?, var content: String, var datetime: Long)



//메인 DB 를 관리하기 위한 클래스
class MainDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    //SQLiteOpenHelper: SQLite 와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
    //onCreate 와 onUpgrade 메서드는 자동으로 생성


    //onCreate: DB가 생성되어있지 않다면 이 메서드를 통해 DB 생성
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 구문: main(variable:long, value: text)
        val create = "create table main (variable long primary key, value text)"
        db?.execSQL(create)
    }


    //onUpgrade: 버전 변경사항이 있을 경우에 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS main") //logging 이라는 테이블이 이미 있으면 제거
        onCreate(db) //테이블 재생성
    }


    //select: Main 이 담긴 리스트를 리턴
    @SuppressLint("Range") //설정 SDK 버전 이후의 API 를 경고없이 사용할 수 있게 해주는 어노테이션
    fun select(): MutableList<Main> {
        val list = mutableListOf<Main>() //리턴할 리스트 생성

        val cursor = readableDatabase.rawQuery("select * from main", null)
        //readableDatabase.rawQuery(): 쿼리를 담아서 실행하면 cursor 형태로 값이 반환
        //cursor: 데이터 요소 + DB 에서의 위치에 대한 데이터
        //따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능

        //moveToNext() 다음 줄에 사용가능한 레코드가 있으면 true 를 리턴하고 커서를 다음위치로 이동 / 없으면 false
        while (cursor.moveToNext()) {
            //테이블에 정의된 3개의 컬럼에서 값을 꺼낸 다음 변수에 담는다.
            val variable = cursor.getLong(cursor.getColumnIndex("variable"))
            val value = cursor.getString(cursor.getColumnIndex("value"))

            list.add(Main(variable, value))
        }

        cursor.close()
        readableDatabase.close()

        return list
    }


    //insert: Main 타입 매개변수를 받으면, 그 내부값들을 DB에 저장
    fun insert(main: Main) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("variable", main.variable)
        values.put("value", main.value)

        //DB의 logging 테이블에 ContentValues 내 정보들을 삽입
        writableDatabase.insert("main", null, values)
        writableDatabase.close()
    }


    //update: DB의 데이터를 새로운 Main 으로 갱신
    fun update(main: Main) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("variable", main.variable)
        values.put("value", main.value)

        writableDatabase.update("main", values, "id=${main.variable}", null)
        writableDatabase.close()
    }


    //delete: Main 타입 매개변수를 받으면, DB 에서 일치하는 데이터를 제거 => 제거할 일이 없음
//    fun delete(logging: Logging) {
//        val delete = "delete from memo where id = ${logging.id}"
//
//        writableDatabase.execSQL(delete)
//        writableDatabase.close()
//    }
}



//로그 DB 를 관리하기 위한 클래스
class LogDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    //SQLiteOpenHelper: SQLite 와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
    //onCreate 와 onUpgrade 메서드는 자동으로 생성


    //onCreate: DB가 생성되어있지 않다면 이 메서드를 통해 DB 생성
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 구문: logging(id:long, content: text, datetime: long)
        val create = "create table logging (id long primary key,content text, datetime long)"
        db?.execSQL(create)
    }


    //onUpgrade: 버전 변경사항이 있을 경우에 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS logging") //logging 이라는 테이블이 이미 있으면 제거
        onCreate(db) //테이블 재생성
    }


    //select: Logging 이 담긴 리스트를 리턴
    @SuppressLint("Range") //설정 SDK 버전 이후의 API 를 경고없이 사용할 수 있게 해주는 어노테이션
    fun select(): MutableList<Logging> {
        val list = mutableListOf<Logging>() //리턴할 리스트 생성

        val cursor = readableDatabase.rawQuery("select * from logging", null)
        //readableDatabase.rawQuery(): 쿼리를 담아서 실행하면 cursor 형태로 값이 반환
        //cursor: 데이터 요소 + DB 에서의 위치에 대한 데이터
        //따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능

        //moveToNext() 다음 줄에 사용가능한 레코드가 있으면 true 를 리턴하고 커서를 다음위치로 이동 / 없으면 false
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


    //update: DB의 데이터를 새로운 Logging 으로 갱신 => 메인로그는 갱신할 일이 없음
//    fun update(logging: Logging) {
//        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
//        values.put("content", logging.content)
//        values.put("datetime", logging.datetime)
//
//        writableDatabase.update("logging", values, "id=${logging.id}", null)
//        writableDatabase.close()
//    }


    //delete: Logging 타입 매개변수를 받으면, DB 에서 일치하는 데이터를 제거
    fun delete(logging: Logging) {
        val delete = "delete from memo where id = ${logging.id}"

        writableDatabase.execSQL(delete)
        writableDatabase.close()
    }
}



//다이어리 DB 를 관리하기 위한 클래스
class DiaryDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    //SQLiteOpenHelper: SQLite와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
    //onCreate와 onUpgrade 메서드는 자동으로 생성


    //onCreate: DB가 생성되어있지 않다면 이 메서드를 통해 DB생성
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 구문: memo(id:long, content: text, datetime: long)
        val create = "create table diarying (id long primary key,content text,datetime long)"
        db?.execSQL(create)
    }


    //onUpgrade: 버전 변경사항이 있을 경우에 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS diarying") //diary 라는 테이블이 이미 있으면 제거
        onCreate(db) //테이블 재생성
    }


    //select: Diarying 이 담긴 리스트를 리턴
    @SuppressLint("Range") //설정 SDK 버전 이후의 API 를 경고없이 사용할 수 있게 해주는 어노테이션
    fun select(): MutableList<Diarying> {
        val list = mutableListOf<Diarying>() //리턴할 리스트 생성

        val cursor = readableDatabase.rawQuery("select * from diarying", null)
        //readableDatabase.rawQuery(): 쿼리를 담아서 실행하면 cursor 형태로 값이 반환
        //cursor: 데이터 요소 + DB 에서의 위치에 대한 데이터
        //따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능

        //moveToNext() 다음 줄에 사용가능한 레코드가 있으면 true를 리턴하고 커서를 다음위치로 이동 / 없으면 false
        while (cursor.moveToNext()) {
            //테이블에 정의된 3개의 컬럼에서 값을 꺼낸 다음 변수에 담는다.
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Diarying(id, content, datetime))
        }

        cursor.close()
        readableDatabase.close()

        return list
    }


    //insert: Memo 타입 매개변수를 받으면, 그 내부값들을 DB에 저장
    fun insert(diarying: Diarying) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content", diarying.content)
        values.put("datetime", diarying.datetime)

        //DB의 Memo 테이블에 ContentValues 내 정보들을 삽입
        writableDatabase.insert("diarying", null, values)
        writableDatabase.close()
    }


    //update: DB의 데이터를 새로운 diarying 로 갱신
    fun update(diarying: Diarying) {
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content", diarying.content)
        values.put("datetime", diarying.datetime)

        writableDatabase.update("diarying", values, "id=${diarying.id}", null)
        writableDatabase.close()
    }

    //delete: Memo 타입 매개변수를 받으면, DB에서 일치하는 데이터를 제거
    fun delete(diarying: Diarying) {
        val delete = "delete from diarying where id = ${diarying.id}"

        writableDatabase.execSQL(delete)
        writableDatabase.close()
    }
}

