package com.example.dietcalculator.androidviews

interface WeekCalendarListener {

    fun onDaySelected(view: WeekCalendarView, day: WeekDays)

    fun onDayTouched(view: WeekCalendarView, day: WeekDays)

}
