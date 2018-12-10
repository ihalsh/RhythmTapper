package com.mygdx.game.Data

import com.badlogic.gdx.files.FileHandle
import java.lang.Float.*

class SongData(
        var songName: String = String(),
        var songDuration: Float = 0f,
        val keyTimeList: ArrayList<KeyTimePair> = ArrayList(),
        var keyTimeIndex: Int = 0
) {

    fun addKeyTime(k: String, t: Float) {
        keyTimeList.add(KeyTimePair(k, t))
    }

    fun resetIndex() {
        keyTimeIndex = 0
    }

    fun advanceIndex() {
        keyTimeIndex++
    }

    fun getCurrentKeyTime(): KeyTimePair = keyTimeList[keyTimeIndex]

    fun keyTimeCount(): Int = keyTimeList.size

    fun isFinished(): Boolean = keyTimeIndex >= keyTimeCount()

    fun writeToFile(file: FileHandle) {
        with(file) {
            writeString("$songName\n", false)
            writeString("$songDuration\n", true)
            for (ktp in keyTimeList) {
                writeString("${ktp.key},${ktp.time}\n", true)
            }
        }
    }

    fun readFromFile(file: FileHandle) {
        val rawData = file.readString()
        val dataArray = rawData.split("\n".toRegex())
                .dropLastWhile { it.isEmpty() }.toTypedArray()
        songName = dataArray[0]
        songDuration = parseFloat(dataArray[1])
        keyTimeList.clear()
        for (i in 2 until dataArray.size) {
            val keyTimeData = dataArray[i].split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()
            val key = keyTimeData[0]
            val time = parseFloat(keyTimeData[1])
            keyTimeList.add(KeyTimePair(key, time))
        }
    }

    inner class KeyTimePair(val key: String, val time: Float)
}