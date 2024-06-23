package com.example.dietcalculator.androidviews

object WeekCalendarListeners {

    private var listeners = mutableSetOf<WeekCalendarListener>()

    fun addListener(l: WeekCalendarListener){
        this.listeners.add(l)
    }

    fun removeListener(l: WeekCalendarListener){
        this.listeners.remove(l)
    }

    fun notify( block: (l: WeekCalendarListener) -> Unit ){
        listeners.forEach {
            block(it)
        }
    }

}