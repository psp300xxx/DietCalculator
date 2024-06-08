package com.example.dietcalculator.fragments

import com.example.dietcalculator.model.Food
import java.util.function.Predicate

class StringCondition(private var inputString: String?): Predicate<String> {

    constructor(): this(null){}

    override fun test(p0: String): Boolean {
        var result = true
        inputString?.let {
            ins ->
            result = p0.uppercase().contains(ins.uppercase())
        }
        return result
    }

    fun getInputString(): String?{
        return inputString
    }

    fun setInputString(s: String?){
        this.inputString = s
    }
}