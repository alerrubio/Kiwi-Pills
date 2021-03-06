package com.kiwipills.kiwipillsapp.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import java.lang.Exception

class DataDBHelper (var context: Context) : SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        try{

            val createMedicamentTable:String =  "CREATE TABLE " + SetDB.tblMedicament.TABLE_NAME + "(" +
                    SetDB.tblMedicament.COL_ID + " INTEGER PRIMARY KEY," +
                    SetDB.tblMedicament.COL_USER_ID + " INTEGER," +
                    SetDB.tblMedicament.COL_TITLE + " VARCHAR(200)," +
                    SetDB.tblMedicament.COL_DESCRIPTION + " VARCHAR(500), " +
                    SetDB.tblMedicament.COL_STARTDATE + " VARCHAR(15), " +
                    SetDB.tblMedicament.COL_ENDDATE + " VARCHAR(15), " +
                    SetDB.tblMedicament.COL_STARTTIME + " VARCHAR(15), " +
                    SetDB.tblMedicament.COL_DURATION + " INTEGER, " +
                    SetDB.tblMedicament.COL_HOURSINTERVAL + " INTEGER, " +
                    SetDB.tblMedicament.COL_MONDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_THUESDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_WEDNESDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_THURSDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_FRIDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_SATURDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_SUNDAY + " TINYINT(1), " +
                    SetDB.tblMedicament.COL_IMG + " TEXT, " +
                    SetDB.tblMedicament.COL_ALARM_ID + " TEXT, " +
                    SetDB.tblMedicament.COL_DRAFT + " TINYINT(1) " +
                    ")"

            Log.d("EL QUERY: ", createMedicamentTable)

            db?.execSQL(createMedicamentTable)

            Log.e("Aviso","Se creo database local")

        }catch (e: Exception){
            Log.e("Aviso","Ya existe Database local")
            Log.e("Execption", e.toString())
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertMedicament(medicament:Medicament):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblMedicament.COL_ID,medicament.id)
        values.put(SetDB.tblMedicament.COL_USER_ID,medicament.user_id)
        values.put(SetDB.tblMedicament.COL_TITLE,medicament.name)
        values.put(SetDB.tblMedicament.COL_DESCRIPTION,medicament.description)
        values.put(SetDB.tblMedicament.COL_STARTDATE,medicament.startDate)
        values.put(SetDB.tblMedicament.COL_ENDDATE,medicament.endDate)
        values.put(SetDB.tblMedicament.COL_STARTTIME,medicament.startTime)
        values.put(SetDB.tblMedicament.COL_DURATION,medicament.duration)
        values.put(SetDB.tblMedicament.COL_HOURSINTERVAL,medicament.hoursInterval)
        values.put(SetDB.tblMedicament.COL_MONDAY,medicament.monday)
        values.put(SetDB.tblMedicament.COL_THUESDAY,medicament.thuesday)
        values.put(SetDB.tblMedicament.COL_WEDNESDAY,medicament.wednesday)
        values.put(SetDB.tblMedicament.COL_THURSDAY,medicament.thursday)
        values.put(SetDB.tblMedicament.COL_FRIDAY,medicament.friday)
        values.put(SetDB.tblMedicament.COL_SATURDAY,medicament.saturday)
        values.put(SetDB.tblMedicament.COL_SUNDAY,medicament.sunday)
        values.put(SetDB.tblMedicament.COL_IMG,medicament.image)
        values.put(SetDB.tblMedicament.COL_ALARM_ID,medicament.alarmIds)
        values.put(SetDB.tblMedicament.COL_DRAFT,medicament.draft)

        try {
            val result =  dataBase.insert(SetDB.tblMedicament.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                //Toast.makeText(this.context, "No se pudo agregar medicamento", Toast.LENGTH_SHORT).show()
                boolResult =  false
            }
            else {
                //Toast.makeText(this.context, "Medicamento agregado", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    fun getListOfMedicaments(draft: Boolean):MutableList<Medicament>{
        val List:MutableList<Medicament> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> = arrayOf(SetDB.tblMedicament.COL_ID,
            SetDB.tblMedicament.COL_ID,
            SetDB.tblMedicament.COL_USER_ID,
            SetDB.tblMedicament.COL_TITLE,
            SetDB.tblMedicament.COL_DESCRIPTION,
            SetDB.tblMedicament.COL_STARTDATE,
            SetDB.tblMedicament.COL_ENDDATE,
            SetDB.tblMedicament.COL_STARTTIME,
            SetDB.tblMedicament.COL_DURATION,
            SetDB.tblMedicament.COL_HOURSINTERVAL,
            SetDB.tblMedicament.COL_MONDAY,
            SetDB.tblMedicament.COL_THUESDAY,
            SetDB.tblMedicament.COL_WEDNESDAY,
            SetDB.tblMedicament.COL_THURSDAY,
            SetDB.tblMedicament.COL_FRIDAY,
            SetDB.tblMedicament.COL_SATURDAY,
            SetDB.tblMedicament.COL_SUNDAY,
            SetDB.tblMedicament.COL_IMG,
            SetDB.tblMedicament.COL_ALARM_ID,
            SetDB.tblMedicament.COL_DRAFT
        )

        var borrador = 0;
        if(draft == true){
            borrador = 1
        }

        //dataBase.rawQuery()
        val data =  dataBase.query(SetDB.tblMedicament.TABLE_NAME,
            columns,
            SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_DRAFT + " = ?",
            arrayOf(Globals.UserLogged.id.toString(), borrador.toString()),
            null,
            null,
            SetDB.tblMedicament.COL_ID + " ASC")


        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            val id_column = data.getColumnIndex(SetDB.tblMedicament.COL_ID)
            val user_id_column = data.getColumnIndex(SetDB.tblMedicament.COL_USER_ID)
            val title_column = data.getColumnIndex(SetDB.tblMedicament.COL_TITLE)
            val description_column = data.getColumnIndex(SetDB.tblMedicament.COL_DESCRIPTION)
            val startdate_column = data.getColumnIndex(SetDB.tblMedicament.COL_STARTDATE)
            val enddate_column = data.getColumnIndex(SetDB.tblMedicament.COL_ENDDATE)
            val starttime_column = data.getColumnIndex(SetDB.tblMedicament.COL_STARTTIME)
            val duration_column = data.getColumnIndex(SetDB.tblMedicament.COL_DURATION)
            val hoursinterval_column = data.getColumnIndex(SetDB.tblMedicament.COL_HOURSINTERVAL)
            val monday_column = data.getColumnIndex(SetDB.tblMedicament.COL_MONDAY)
            val thuesday_column = data.getColumnIndex(SetDB.tblMedicament.COL_THUESDAY)
            val wednesday_column = data.getColumnIndex(SetDB.tblMedicament.COL_WEDNESDAY)
            val thursday_column = data.getColumnIndex(SetDB.tblMedicament.COL_THURSDAY)
            val friday_column = data.getColumnIndex(SetDB.tblMedicament.COL_FRIDAY)
            val saturday_column = data.getColumnIndex(SetDB.tblMedicament.COL_SATURDAY)
            val sunday_column = data.getColumnIndex(SetDB.tblMedicament.COL_SUNDAY)
            val image_column = data.getColumnIndex(SetDB.tblMedicament.COL_IMG)
            val alarm_id_column = data.getColumnIndex(SetDB.tblMedicament.COL_ALARM_ID)
            val draft_column = data.getColumnIndex(SetDB.tblMedicament.COL_DRAFT)




            do{
                val id = data.getString(id_column).toInt()
                val userId = data.getString(user_id_column).toInt()
                val title = data.getString(title_column)
                val description = data.getString(description_column)
                val startdate = data.getString(startdate_column)
                val enddate = data.getString(enddate_column)
                val starttime = data.getString(starttime_column)
                val duration = data.getString(duration_column).toInt()
                val hoursinterval = data.getString(hoursinterval_column).toInt()

                val monday = if (data.getInt(monday_column) == 1) true else false
                val thuesday = if (data.getInt(thuesday_column) == 1) true else false
                val wednesday = if (data.getInt(wednesday_column) == 1) true else false
                val thursday = if (data.getInt(thursday_column) == 1) true else false
                val friday = if (data.getInt(friday_column) == 1) true else false
                val saturday = if (data.getInt(saturday_column) == 1) true else false
                val sunday = if (data.getInt(sunday_column) == 1) true else false

                val image = data.getString(image_column)
                val alarm_id = data.getString(alarm_id_column)
                val draft = if (data.getInt(draft_column) == 1) true else false

                val medicament = Medicament(
                    id,
                    userId,
                    title,
                    description,
                    startdate,
                    enddate,
                    starttime,
                    duration,
                    hoursinterval,
                    monday,
                    thuesday,
                    wednesday,
                    thursday,
                    friday,
                    saturday,
                    sunday,
                    image,
                    alarm_id,
                    draft
                )

                List.add(medicament)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  List
    }

    fun getListOfMedicamentsByDay(p_day: Int):MutableList<Medicament>{
        val List:MutableList<Medicament> = ArrayList()
        val p_user_id = Globals.UserLogged.id.toString()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> = arrayOf(
            SetDB.tblMedicament.COL_ID,
            SetDB.tblMedicament.COL_USER_ID,
            SetDB.tblMedicament.COL_TITLE,
            SetDB.tblMedicament.COL_DESCRIPTION,
            SetDB.tblMedicament.COL_STARTDATE,
            SetDB.tblMedicament.COL_ENDDATE,
            SetDB.tblMedicament.COL_STARTTIME,
            SetDB.tblMedicament.COL_DURATION,
            SetDB.tblMedicament.COL_HOURSINTERVAL,
            SetDB.tblMedicament.COL_MONDAY,
            SetDB.tblMedicament.COL_THUESDAY,
            SetDB.tblMedicament.COL_WEDNESDAY,
            SetDB.tblMedicament.COL_THURSDAY,
            SetDB.tblMedicament.COL_FRIDAY,
            SetDB.tblMedicament.COL_SATURDAY,
            SetDB.tblMedicament.COL_SUNDAY,
            SetDB.tblMedicament.COL_IMG,
            SetDB.tblMedicament.COL_ALARM_ID,
            SetDB.tblMedicament.COL_DRAFT
        )

        var query = ""
        when (p_day) {
            2 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_MONDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
            3 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_THUESDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
            4 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_WEDNESDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
            5 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_THURSDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
            6 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_FRIDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
            7 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_SATURDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
            1 -> query = SetDB.tblMedicament.COL_USER_ID + " = ? AND " + SetDB.tblMedicament.COL_SUNDAY + " = 1 AND " + SetDB.tblMedicament.COL_DRAFT + " = 0"
        }

        val data =  dataBase.query(
            SetDB.tblMedicament.TABLE_NAME,
            columns,
            query,
            arrayOf(p_user_id),
            null,
            null,
            SetDB.tblMedicament.COL_STARTTIME + " DESC"
        )


        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            val id_column = data.getColumnIndex(SetDB.tblMedicament.COL_ID)
            val user_id_column = data.getColumnIndex(SetDB.tblMedicament.COL_USER_ID)
            val title_column = data.getColumnIndex(SetDB.tblMedicament.COL_TITLE)
            val description_column = data.getColumnIndex(SetDB.tblMedicament.COL_DESCRIPTION)
            val startdate_column = data.getColumnIndex(SetDB.tblMedicament.COL_STARTDATE)
            val enddate_column = data.getColumnIndex(SetDB.tblMedicament.COL_ENDDATE)
            val starttime_column = data.getColumnIndex(SetDB.tblMedicament.COL_STARTTIME)
            val duration_column = data.getColumnIndex(SetDB.tblMedicament.COL_DURATION)
            val hoursinterval_column = data.getColumnIndex(SetDB.tblMedicament.COL_HOURSINTERVAL)
            val monday_column = data.getColumnIndex(SetDB.tblMedicament.COL_MONDAY)
            val thuesday_column = data.getColumnIndex(SetDB.tblMedicament.COL_THUESDAY)
            val wednesday_column = data.getColumnIndex(SetDB.tblMedicament.COL_WEDNESDAY)
            val thursday_column = data.getColumnIndex(SetDB.tblMedicament.COL_THURSDAY)
            val friday_column = data.getColumnIndex(SetDB.tblMedicament.COL_FRIDAY)
            val saturday_column = data.getColumnIndex(SetDB.tblMedicament.COL_SATURDAY)
            val sunday_column = data.getColumnIndex(SetDB.tblMedicament.COL_SUNDAY)
            val image_column = data.getColumnIndex(SetDB.tblMedicament.COL_IMG)
            val alarm_id_column = data.getColumnIndex(SetDB.tblMedicament.COL_ALARM_ID)
            val draft_column = data.getColumnIndex(SetDB.tblMedicament.COL_DRAFT)


            do{
                val id = data.getString(id_column).toInt()
                val userId = data.getString(user_id_column).toInt()
                val title = data.getString(title_column)
                val description = data.getString(description_column)
                val startdate = data.getString(startdate_column)
                val enddate = data.getString(enddate_column)
                val starttime = data.getString(starttime_column)
                val duration = data.getString(duration_column).toInt()
                val hoursinterval = data.getString(hoursinterval_column).toInt()

                val monday = if (data.getInt(monday_column) == 1) true else false
                val thuesday = if (data.getInt(thuesday_column) == 1) true else false
                val wednesday = if (data.getInt(wednesday_column) == 1) true else false
                val thursday = if (data.getInt(thursday_column) == 1) true else false
                val friday = if (data.getInt(friday_column) == 1) true else false
                val saturday = if (data.getInt(saturday_column) == 1) true else false
                val sunday = if (data.getInt(sunday_column) == 1) true else false

                val image = data.getString(image_column)
                val alarm_id = data.getString(alarm_id_column)
                val draft = if (data.getInt(draft_column) == 1) true else false

                val medicament = Medicament(
                    id,
                    userId,
                    title,
                    description,
                    startdate,
                    enddate,
                    starttime,
                    duration,
                    hoursinterval,
                    monday,
                    thuesday,
                    wednesday,
                    thursday,
                    friday,
                    saturday,
                    sunday,
                    image,
                    alarm_id,
                    draft
                )

                if(medicament.draft == false){
                    List.add(medicament)
                }


                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  List
    }

    fun updateMedicament(medicamentData: Medicament):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblMedicament.COL_TITLE,medicamentData.name)
        values.put(SetDB.tblMedicament.COL_DESCRIPTION,medicamentData.description)
        values.put(SetDB.tblMedicament.COL_STARTDATE,medicamentData.startDate)
        values.put(SetDB.tblMedicament.COL_ENDDATE,medicamentData.endDate)
        values.put(SetDB.tblMedicament.COL_STARTTIME,medicamentData.startTime)
        values.put(SetDB.tblMedicament.COL_DURATION,medicamentData.duration)
        values.put(SetDB.tblMedicament.COL_HOURSINTERVAL,medicamentData.hoursInterval)
        values.put(SetDB.tblMedicament.COL_MONDAY,medicamentData.monday)
        values.put(SetDB.tblMedicament.COL_THUESDAY,medicamentData.thursday)
        values.put(SetDB.tblMedicament.COL_WEDNESDAY,medicamentData.wednesday)
        values.put(SetDB.tblMedicament.COL_THURSDAY,medicamentData.thursday)
        values.put(SetDB.tblMedicament.COL_FRIDAY,medicamentData.friday)
        values.put(SetDB.tblMedicament.COL_SATURDAY,medicamentData.saturday)
        values.put(SetDB.tblMedicament.COL_SUNDAY,medicamentData.sunday)
        values.put(SetDB.tblMedicament.COL_IMG,medicamentData.image)
        values.put(SetDB.tblMedicament.COL_ALARM_ID,medicamentData.alarmIds)
        values.put(SetDB.tblMedicament.COL_DRAFT,medicamentData.draft)


        try{

            val result =  dataBase.update(
                SetDB.tblMedicament.TABLE_NAME,
                values,
                SetDB.tblMedicament.COL_ID + "=?",
                arrayOf(medicamentData.id.toString()))

            if (result != -1 ) {
                //Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }
            else {
                //Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    fun publishMedicamentDraft(id:Int):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblMedicament.COL_DRAFT,"0")

        try{

            val result =  dataBase.update(SetDB.tblMedicament.TABLE_NAME,
                values,
                SetDB.tblMedicament.COL_ID + "=?",
                arrayOf(id.toString()))

            if (result != -1 ) {
                //Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }
            else {
                //Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    fun deleteMedicament(id:Int):Boolean{
        val db = this.writableDatabase
        var boolResult:Boolean =  false
        try{
            val where:String = SetDB.tblMedicament.COL_ID + "=?"
            val _success = db.delete(
                SetDB.tblMedicament.TABLE_NAME,
                where,
                arrayOf(id.toString())
            )
            db.close()

            boolResult = Integer.parseInt("$_success") != -1

        }catch (e: Exception){

            Log.e("Execption", e.toString())
        }
        return  boolResult
    }

}
