package com.example.dietcalculator.dao.sqliteconnector

import com.example.dietcalculator.dao.fooddatadownloader.IRemoteFoodDataDownloader
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class DownloaderThread: Thread() {

    private val OPERATION_DELAY: Long = 200 //ms
    private val hasToDownload = AtomicBoolean(false)
    private var hasCompleted: AtomicBoolean = AtomicBoolean(false)
    private var downloads : Queue<IRemoteFoodDataDownloader> = ConcurrentLinkedQueue<IRemoteFoodDataDownloader>()

    override fun run() {
        while(!hasCompleted.get()){
            this.downloads.poll()?.let {
                it.downloadFoodData()
            }
            sleep(OPERATION_DELAY)
        }
    }

    fun triggerDownload(downloader: IRemoteFoodDataDownloader){
        this.downloads.add(downloader)
    }

    fun stopThread(){
        this.hasCompleted.set(true)
    }
}