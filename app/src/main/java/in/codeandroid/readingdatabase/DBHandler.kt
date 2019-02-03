package `in`.codeandroid.readingdatabase

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import java.util.*
import kotlin.collections.ArrayList


class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE NumberList (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "number INTEGER," +
                "isOddNumber INTEGER)"
        db?.execSQL(query)

        db?.beginTransaction()
        for (i in 0..1000) {
            val cv = ContentValues()
            cv.put("number", i)
            cv.put("isOddNumber", (i % 2 == 1))
            db?.insert("NumberList", null, cv)
        }
        db?.setTransactionSuccessful()
        db?.endTransaction()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion == 2) {
            if (db != null)
                UpdateDB(db, context).execute()
        }
    }


    class UpdateDB(val db: SQLiteDatabase, val context: Context) : AsyncTask<Void, Void, Void>() {

        lateinit var dialog: AlertDialog

        override fun onPreExecute() {
            val builder = AlertDialog.Builder(context)
            builder.setView(LayoutInflater.from(context).inflate(R.layout.dialog_layout, null))
            dialog = builder.create()
            dialog.show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Void? {

            val result: MutableList<Number> = ArrayList()
            val getAllData = "SELECT * from  NumberList"
            val queryResult = db.rawQuery(getAllData, null)
            if (queryResult.moveToFirst()) {
                do {
                    val number = Number()
                    number.id = queryResult.getInt(queryResult.getColumnIndex("id"))
                    number.number = queryResult.getInt(queryResult.getColumnIndex("number"))
                    result.add(number)
                } while (queryResult.moveToNext())
            }
            queryResult.close()

            val addColumn = "ALTER TABLE NumberList ADD COLUMN isOddNumber INTEGER"
            db.execSQL(addColumn)


            db.beginTransaction()
            for (i in 0..(result.size - 1)) {
                val cv = ContentValues()
                cv.put("isOddNumber", ((result[i].number % 2) == 1))
                db.update("NumberList", cv, "id=?", arrayOf(result[i].id.toString()))
            }
            db.setTransactionSuccessful()
            db.endTransaction()

            Thread.sleep(10000)

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (::dialog.isInitialized)
                dialog.dismiss()
        }
    }

    fun getAllData(): MutableList<Number> {
        Log.d("DBHandler", "getAllData")
        val result: MutableList<Number> = ArrayList()

        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from NumberList", null)
        if (queryResult.moveToFirst()) {
            do {
                val number = Number()
                number.id = queryResult.getInt(queryResult.getColumnIndex("id"))
                number.number = queryResult.getInt(queryResult.getColumnIndex("number"))
                number.isOddNumber = queryResult.getInt(queryResult.getColumnIndex("isOddNumber")) == 1
                result.add(number)
            } while (queryResult.moveToNext())
        }
        queryResult.close()

        return result
    }

    fun getWritableDb(): SQLiteDatabase {
        return writableDatabase
    }

}