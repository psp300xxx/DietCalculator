package com.example.dietcalculator.datastructures

import java.util.Collections
import java.util.function.Consumer

class TreeImpl<T>(private var value: T) : ITree<T> {

    private var children: List< ITree<T> >? = null
    private var parent: ITree<T>? = null

    constructor(value: T, parent: ITree<T>) : this(value) {
        this.parent = parent
    }

    override fun getValue(): T {
        return value
    }

    override fun getChildren(): List<ITree<T>> {
        if(children==null){
            return Collections.emptyList()
        }
        return Collections.unmodifiableList(children)
    }

    override fun getParent(): ITree<T>? {
        return parent
    }

    override fun addChildren(value: T): ITree<T> {
        if( children==null ){
            children = ArrayList<ITree<T>>()
        }
        var childNode: ITree<T> =  TreeImpl<T>(value, this)
        children = children!! + childNode
        return childNode
    }

}