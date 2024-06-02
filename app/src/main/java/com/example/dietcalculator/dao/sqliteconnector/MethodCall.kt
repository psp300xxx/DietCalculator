package com.example.dietcalculator.dao.sqliteconnector

import com.example.dietcalculator.dao.IDatabaseConnector
import java.lang.reflect.Method

class MethodCall(private val method: Method, private val params: Array<Object>, private val connector: IDatabaseConnector) {

    fun call(){
        method.isAccessible = true
        if ( params.isEmpty() ){
            method.invoke(this.connector)
        }
        else{
            method.invoke(this.connector, *params)
        }
    }

}