package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.mygdx.game.Actors.Message
import com.mygdx.game.Data.SongData
import com.mygdx.game.Utils.Assets.labelStyle
import com.mygdx.game.Utils.Assets.textButtonStyle
import com.mygdx.game.Utils.FileUtils
import com.mygdx.game.Utils.FileUtils.isTouchDownEvent
import com.mygdx.game.Utils.FileUtils.showOpenDialog
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.log.info


class RecorderScreen(
        private val game: Game,
        private val mainStage: Stage = Stage(),
        private val uiStage: Stage = Stage(),
        private val uiTable: Table = Table().apply {
            setFillParent(true)
            uiStage.addActor(this)
        }) : KtxScreen, KtxInputAdapter {

    var music: Music? = null
    var songData: SongData = SongData()
    var lastSongPosition: Float = 0f
    var recording: Boolean = false

    override fun show() {
        //Handle input from everywhere
        val im = Gdx.input.inputProcessor as InputMultiplexer
        im.addProcessor(this)
        im.addProcessor(uiStage)
        im.addProcessor(mainStage)

        //Buttons
        val loadButton = TextButton("Load Music File", textButtonStyle).apply {
            uiStage.addActor(this)
            addListener {
                if (!isTouchDownEvent(it)) return@addListener false
                val musicFile = showOpenDialog()
                if (musicFile != null) {
                    music = Gdx.audio.newMusic(musicFile)
                    songData.songName = musicFile.name()
                    info { "${songData.songName} added." }
                }
                true
            }
        }

        val recordButton = TextButton("Record Keystrokes", textButtonStyle).apply {
            uiStage.addActor(this)
            addListener {
                if (!isTouchDownEvent(it)) return@addListener false
                if (!recording && music != null) {
                    music!!.play()
                    recording = true
                    lastSongPosition = 0f
                }
                true
            }
        }

        val saveButton = TextButton("Save Keystroke File", textButtonStyle).apply {
            uiStage.addActor(this)
            addListener {
                if (!isTouchDownEvent(it)) return@addListener false
                val textFile = FileUtils.showSaveDialog()
                if (textFile != null)
                    songData.writeToFile(textFile)
                true
            }
        }

        //Position all elements
        with(uiTable) {
            add(loadButton)
            row()
            add(recordButton)
            row()
            add(saveButton)
        }
    }

    override fun keyDown(keyCode: Int): Boolean {
        if (recording) {
            val key = Input.Keys.toString(keyCode)
            val time = music!!.position
            songData.addKeyTime(key, time)
        }
        return false
    }

    override fun render(delta: Float) {
        update(delta)
        clearScreen(BLACK.r, BLACK.g, BLACK.b)
        mainStage.draw()
        uiStage.draw()
    }

    private fun update(delta: Float) {
        mainStage.act(delta)
        uiStage.act()

        if (recording) {
            if (music!!.isPlaying)
                lastSongPosition = music!!.position
            else {// song just finished
                recording = false
                songData.songDuration = lastSongPosition
            }
        }
    }

    override fun dispose() {
    }
}