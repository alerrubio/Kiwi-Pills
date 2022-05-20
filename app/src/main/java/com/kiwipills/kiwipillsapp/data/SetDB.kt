package com.kiwipills.kiwipillsapp.data

//CLASE UTILIZADA PARA DEFINIR EL ESQUEMA DE NUESTRA BASE DE DATOS
class SetDB {

    //DECLARAMOS  EL NOMBRE Y VERSION DE TAL FOR QUE PUEDA SER VISIBLES PARA CUALQUIER CLASE
    companion object{
        val DB_NAME =  "kiwipillsDB"
        val DB_VERSION =  1
    }

    //VAMOS ES DEFINIR EL ESQUEMA DE UNA DE LAS TABLAS
    abstract class tblMedicament{
        //DEFINIMOS LOS ATRIBUTOS DE LA CLASE USANDO CONTANTES
        companion object{
            val TABLE_NAME = "Medicaments"
            val COL_ID =  "id"
            val COL_USER_ID =  "user_id"
            val COL_TITLE =  "name"
            val COL_DESCRIPTION = "description"
            val COL_STARTDATE = "startDate"
            val COL_ENDDATE = "endDate"
            val COL_STARTTIME = "startTime"
            val COL_DURATION = "duration"
            val COL_HOURSINTERVAL = "hoursInterval"
            val COL_MONDAY = "monday"
            val COL_THUESDAY = "thuesday"
            val COL_WEDNESDAY = "wednesday"
            val COL_THURSDAY = "thursday"
            val COL_FRIDAY = "friday"
            val COL_SATURDAY = "saturday"
            val COL_SUNDAY = "sunday"
            val COL_IMG =  "image"
            val COL_ALARM_ID =  "alarmIds"
            val COL_DRAFT =  "draft"

        }
    }

}