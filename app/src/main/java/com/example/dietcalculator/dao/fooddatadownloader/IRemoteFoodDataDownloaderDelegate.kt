package com.example.dietcalculator.dao.fooddatadownloader

import com.example.dietcalculator.model.Food

interface IRemoteFoodDataDownloaderDelegate {

    fun foodDownloaded(downloader: IRemoteFoodDataDownloader, foods: Collection<Food>)

    fun errorRaised(downloader: IRemoteFoodDataDownloader, exception: Throwable)

}