package com.example.dietcalculator.datastructures

import java.util.function.Consumer

interface ITree<T> {

    fun getValue(): T

    fun getChildren(): List<ITree<T>>

    fun getParent(): ITree<T>?

    fun getRoot(): ITree<T> {
        if( getParent()==null ){
            return this
        }
        return getParent()!!.getRoot()
    }

    fun addChildren( value: T ): ITree<T>

    fun visitTree( consumer: Consumer<T>) {
        consumer.accept(getValue())
        for( child in getChildren() ){
            child.visitTree(consumer)
        }
    }

    fun leafToRootValues(): List<T>{
        var result = listOf<T>()
        var node : ITree<T>? = this
        while( node !=null ){
            result += node.getValue()
            node = node.getParent()
        }
        return result
    }

    fun hasParent(): Boolean {
        return getParent() != null
    }

}