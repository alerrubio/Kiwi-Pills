package com.kiwipills.kiwipillsapp.service.Models

import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class Medicament(
    var id:Int? =  null,
    var name:String? = null,
    var description:String? = null,
    var startDate:LocalDate? = null,
    var startTime:LocalTime? = null,
    var duration:Int? = null,
    var hoursInterval:Int? = null,

    var monday:Boolean? = false,
    var thuesday:Boolean? = false,
    var wednesday:Boolean? = false,
    var thursday:Boolean? = false,
    var friday:Boolean? = false,
    var saturday:Boolean? = false,
    var sunday:Boolean? = false,
){

}
