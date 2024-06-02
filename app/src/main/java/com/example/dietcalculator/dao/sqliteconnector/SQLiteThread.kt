package com.example.dietcalculator.dao.sqliteconnector

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class SQLiteThread: Thread() {

    private var operations : Queue<MethodCall> = ConcurrentLinkedQueue<MethodCall>()
    private val OPERATION_DELAY: Long = 200 //ms
    private var hasCompleted: AtomicBoolean = AtomicBoolean(false)

    override fun run() {
        while(!hasCompleted.get()){
            val call = operations.poll()
            call?.call()
            sleep(OPERATION_DELAY)
        }
    }

    fun addOperation(call: MethodCall){
        this.operations.add(call)
    }

    fun stopThread(){
        this.hasCompleted.set(true)
    }



}