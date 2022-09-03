package com.example.farmmanager


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Logging(var id:Long?, var content2: String, var datetime:Long) {}
//Logging이라는 데이터클래스를 정의하여 삽입, 조회, 수정, 삭제 할때 편리하게 사용

class SqliteHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
//sqliteopenhelper 클래스를 상속받아서 사용 (sqliteopenhelper은 SQLite와 액티비티를 연결시켜 DB를 파일로 생성하고, 코트린 코드에서 사용할수있도록 도와줌)
//onCreate와 onUpgrade 메서드는 자동으로 생성
    override fun onCreate(db: SQLiteDatabase?) {

        val create = "create table logging (id long primary key,content2 text, datetime long)"

        db?.execSQL(create)
    } //DB가 생성되어있지 않다면 이 메서드를 통해 DB생성

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS logging")
        onCreate(db)
    } //버전 변경사항이 있을 경우에만 호출된다


    fun insert(logging:Logging){
        val values = ContentValues() //ContentValues 클래스는 키, 값 형태로 사용되어짐 - ("컬럼명",값)
        values.put("content2",logging.content2)
        values.put("datetime",logging.datetime)
        val wd = writableDatabase
        wd.insert("logging",null,values)
        wd.close()
        //values에 작성한 값을 전달하여 insert한 뒤, close를 호출해 닫아줌
    }



    @SuppressLint("Range")
    fun select():MutableList<Logging>{
        val list = mutableListOf<Logging>() //Select 메서드는 반환값이 존재하기때문에 반환한 값을 변수로 선언
        val selectAll = "select * from logging"
        val rd = readableDatabase

        val cursor = rd.rawQuery(selectAll,null)
        //DB의 rawQuery()메서드에 앞서 작성해둔 쿼리를 담아서 실행하면 커서 형태로 값이 반환된다.
        //커서(cursor)은 데이터셋을 처리할 때 현재 위치르 포함하는 데이터 요소이다. 따라서 커서를 사용하면 쿼리를 통해 데이터셋을 반복하며 하나씩 처리 가능하다.

        while(cursor.moveToNext()){ //moveToNext() 메서드가 실행되면 다음 줄에 사용가능한 레코드가 있는지 여부를 반환하고 해당 커서를 다음위치로 이동시킴. 레코드 없으면 반복문 종료
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content2 = cursor.getString(cursor.getColumnIndex("content2"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))

            list.add(Logging(id,content2,datetime))
        }//테이블에 정의된 3개의 컬럼에서 값을 꺼낸 다음 변수에 담는다.
        cursor.close()
        rd.close()

        return list
    }



    fun update(logging:Logging){
        val values = ContentValues()

        values.put("content",logging.content2)
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