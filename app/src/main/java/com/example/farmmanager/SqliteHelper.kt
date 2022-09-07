package com.example.farmmanager


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//데이터를 튜플단위로 사용하기 위한 데이터클래스
data class Logging(var id:Long?, var content2: String, var datetime:Long)
data class Memo(var no: Long?, var content: String, var datetime: Long)



//현 프로그램의 전반적인 DB를 관리하기 위한 클래스
class MainDBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    //SQLiteOpenHelper: SQLite와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
    //onCreate와 onUpgrade 메서드는 자동으로 생성


    //onCreate: DB가 생성되어있지 않다면 이 메서드를 통해 DB생성
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성 구문 logging(id:long, content2: text, datetime: long)
        val create = "create table logging (id long primary key,content2 text, datetime long)"
        db?.execSQL(create)
    }


    //onUpgrade: 버전 변경사항이 있을 경우에 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS logging") //logging이라는 테이블이 이미 있으면 제거
        onCreate(db) //테이블 재생성
    }


    //select: Logging이 담긴 리스트를 리턴
    @SuppressLint("Range") //설정 SDK 버전 이후의 API를 경고없이 사용할 수 있게 해주는 어노테이션
    fun select():MutableList<Logging>{
        val list = mutableListOf<Logging>() //Select 메서드는 반환값이 존재하기때문에 반환한 값을 변수로 선언

        val cursor = readableDatabase.rawQuery("select * from logging",null)
        //readableDatabase.rawQuery(): 쿼리를 담아서 실행하면 cursor 형태로 값이 반환
        //cursor: 데이터 요소 + DB에서의 위치에 대한 데이터
        //따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능

        //moveToNext() 다음 줄에 사용가능한 레코드가 있으면 true를 리턴하고 커서를 다음위치로 이동 / 없으면 false
        while(cursor.moveToNext()){
            //테이블에 정의된 3개의 컬럼에서 값을 꺼낸 다음 변수에 담는다.
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content2 = cursor.getString(cursor.getColumnIndex("content2"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Logging(id,content2,datetime))
        }

        cursor.close()
        readableDatabase.close()

        return list
    }


    //insert: Logging 타입 매개변수를 받으면, 그 내부값들을 DB에 저장
    fun insert(logging:Logging){
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content2",logging.content2)
        values.put("datetime",logging.datetime)

        //DB의 logging 테이블에 ContentValues 내 정보들을 삽입
        writableDatabase.insert("logging",null,values)
        writableDatabase.close()
    }


    //update: Logging을 받아,
    fun update(logging:Logging){
        val values = ContentValues() //ContentValues: db에 담을 데이터로 변환하는 클래스
        values.put("content",logging.content2)
        values.put("datetime",logging.datetime)

        writableDatabase.update("memo",values,"id=${logging.id}",null)
        writableDatabase.close()
    }

    //
    fun delete(logging:Logging) {
        //val delete = "delete from Logging where id = ${logging.id}"
    }



class MemoDBHelper(context: Context, names: String, version: Int):
    SQLiteOpenHelper(context, names, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table memo (no long primary key,content text,datetime long)"


        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertMemo(memo: Memo) {
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        val wd = writableDatabase
        wd.insert("memo", null, values)
        wd.close()
    }

    fun selectMemo(): MutableList<Memo> {
        val list = mutableListOf<Memo>()

        val select = "select * from memo"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)
        while (cursor.moveToNext()) {
            val no = cursor.getLong(cursor.getColumnIndex("no"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Memo(no, content, datetime))
        }

        cursor.close()
        rd.close()
        return list
    }

    fun updateMemo(memo: Memo) {
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        val wd = writableDatabase


        wd.update("memo", values, "no = ${memo.no}", null)
        wd.close()
    }

    fun deleteMemo(memo: Memo) {
        val delete = "delete from memo where no = ${memo.no}"

        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }
}

