package com.example.dietcalculator.dao.fooddatadownloader

import com.example.dietcalculator.dao.IDatabaseConnector

interface IRemoteFoodDataDownloader {

    fun downloadFoodData(n: Int = 1000, chunckSize: Int = 100)

    fun addDelegate(delegate: IRemoteFoodDataDownloaderDelegate)

    fun removeDelegate(delegate: IRemoteFoodDataDownloaderDelegate)


}