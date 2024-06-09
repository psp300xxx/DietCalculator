package com.example.dietcalculator.dao.fooddatadownloader

interface IRemoteFoodDataDownloader {

    fun downloadFoodData(n: Int = 1000, chunckSize: Int = 100)

    fun addDelegate(delegate: IRemoteFoodDataDownloaderDelegate)

}