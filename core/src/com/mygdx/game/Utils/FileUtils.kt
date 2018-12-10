package com.mygdx.game.Utils

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.stage.FileChooser
import ktx.log.info
import java.io.File

object FileUtils {
    private var finished: Boolean = false
    private var fileHandle: FileHandle? = null
    private const val openDialog = 1
    private const val saveDialog = 2

    fun showOpenDialog(): FileHandle? = showDialog(openDialog)

    fun showSaveDialog(): FileHandle? = showDialog(saveDialog)

    private fun showDialog(dialogType: Int): FileHandle? {
        JFXPanel()
        finished = false
        Platform.runLater {
            val file: File? = when (dialogType) {
                openDialog -> FileChooser().showOpenDialog(null)
                else -> FileChooser().showSaveDialog(null)
            }
            fileHandle = if (file != null) FileHandle(file) else null
            finished = true
        }
        // waiting for FileChooser window to close
        while (!finished) {
            delay(0.5f)
        }
        return fileHandle
    }

    //check whether a mouse event corresponds to a button click
    fun isTouchDownEvent(e: Event): Boolean = e is InputEvent && e.type == InputEvent.Type.touchDown
}