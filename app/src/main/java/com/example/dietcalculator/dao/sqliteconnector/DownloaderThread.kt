package com.example.dietcalculator.dao.sqliteconnector

import com.example.dietcalculator.dao.IDatabaseConnector
import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloader
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class DownloaderThread: Thread() {

    private val OPERATION_DELAY: Long = 200 //ms
    private val hasToDownload = AtomicBoolean(false)
    private var hasCompleted: AtomicBoolean = AtomicBoolean(false)
    private var downloads : Queue<Pair<IRemoteFoodDataDownloader, IDatabaseConnector?>> = ConcurrentLinkedQueue<Pair<IRemoteFoodDataDownloader, IDatabaseConnector?>>()

    override fun run() {
        while(!hasCompleted.get()){
            this.downloads.poll()?.let {
                it.second?.recreateDb(parallelize = false)
                it.first.downloadFoodData()
            }
            sleep(OPERATION_DELAY)
        }
    }

    fun triggerDownload(downloader: IRemoteFoodDataDownloader, deleteDb: IDatabaseConnector?=null){
        val pair = Pair<IRemoteFoodDataDownloader, IDatabaseConnector?>(downloader, deleteDb)
        this.downloads.add(pair)
    }

    fun stopThread(){
        this.hasCompleted.set(true)
    }
}