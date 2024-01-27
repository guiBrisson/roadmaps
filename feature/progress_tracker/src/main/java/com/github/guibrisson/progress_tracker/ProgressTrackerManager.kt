package com.github.guibrisson.progress_tracker

import android.content.Context
import android.content.Intent
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import android.util.Log
import androidx.core.content.getSystemService
import com.github.guibrisson.progress_tracker.model.RoadmapTracker
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.util.UUID

class ProgressTrackerManager(private val context: Context) {
    private val fileDir = context.filesDir
    private val file = File(fileDir, FILE_NAME)

    init {
        createFile()
    }

    fun emptyFileList() = file.writeText(listOf<Nothing>().toString())

    fun writeOnFile(tracker: RoadmapTracker) {
        Log.d(TAG, "writing on $FILE_NAME")
        val trackerList = readFile().toMutableList()

        var found = false
        for (t in trackerList) {
            if (t.roadmapId == tracker.roadmapId) {
                t.progress = tracker.progress
                t.isFavorite = tracker.isFavorite
                found = true
                break
            }
        }

        if (!found) {
            trackerList.add(tracker)
        }

        val json = Json.encodeToJsonElement(trackerList)
        file.writeText(json.toString())
    }

    fun readFile(): List<RoadmapTracker> {
        Log.d(TAG, "reading $FILE_NAME")
        var fileAsText = ""

        context.openFileInput(FILE_NAME).bufferedReader().useLines { lines ->
            for (line in lines) fileAsText += line
        }

        return Json.decodeFromString<List<RoadmapTracker>>(fileAsText)
    }

    private fun createFile() {
        if (!file.exists()) {
            checkAvailableBytes()
            file.createNewFile()
            emptyFileList()
        }
    }

    private fun checkAvailableBytes() {
        val storageManager = context.getSystemService<StorageManager>()!!
        val appInternalDirUuid: UUID = storageManager.getUuidForPath(fileDir)
        val availableBytes: Long = storageManager.getAllocatableBytes(appInternalDirUuid)

        if (availableBytes >= NUM_BYTES_NEED_FOR_ROADMAP_TRACKER) {
            storageManager.allocateBytes(
                appInternalDirUuid,
                NUM_BYTES_NEED_FOR_ROADMAP_TRACKER
            )
        } else {
            val intent = Intent().apply {
                action = ACTION_MANAGE_STORAGE
            }
            //TODO: do something with this intent
        }
    }

    companion object {
        // App needs 2 MB within internal storage.
        const val NUM_BYTES_NEED_FOR_ROADMAP_TRACKER = 1024 * 1024 * 2L
        const val FILE_NAME = "tracker.json"
        val TAG = ProgressTrackerManager::class.simpleName
    }
}
