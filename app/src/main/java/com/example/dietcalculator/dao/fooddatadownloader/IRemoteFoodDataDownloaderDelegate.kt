package com.example.dietcalculator.dao.fooddatadownloader

import com.example.dietcalculator.model.Food

interface IRemoteFoodDataDownloaderDelegate {

    fun onFoodChunckDownloaded(downloader: IRemoteFoodDataDownloader, foods: Collection<Food>, downloaded: Int,target: Int)

    fun errorRaised(downloader: IRemoteFoodDataDownloader, exception: Throwable)

    fun onDownloadCompleted(downloader: IRemoteFoodDataDownloader, partially: Boolean, downloadedFood: Int)

    fun onDownloadInterrupted(downloader: IRemoteFoodDataDownloader)

}