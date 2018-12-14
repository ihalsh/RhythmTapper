package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.mygdx.game.Actors.BaseActor
import com.mygdx.game.Actors.BaseActor.Companion.setWorldBounds
import com.mygdx.game.Actors.FallingBox
import com.mygdx.game.Actors.Message
import com.mygdx.game.Actors.TargetBox
import com.mygdx.game.Data.SongData
import com.mygdx.game.Utils.Assets
import com.mygdx.game.Utils.Assets.almostAnimation
import com.mygdx.game.Utils.Assets.goodAnimation
import com.mygdx.game.Utils.Assets.greatAnimation
import com.mygdx.game.Utils.Assets.missAnimation
import com.mygdx.game.Utils.Assets.perfectAnimation
import com.mygdx.game.Utils.Assets.spaceAnimation
import com.mygdx.game.Utils.FileUtils.isTouchDownEvent
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.log.info
import java.util.*


class RhythmScreen(
        private val mainStage: Stage = Stage(),
        private val uiStage: Stage = Stage(),
        private val uiTable: Table = Table().apply {
            setFillParent(true)
            uiStage.addActor(this)
        }) : KtxScreen, KtxInputAdapter {

    private val keyArray = arrayOf("F", "G", "H", "J")
    private val keyList = ArrayList<String>().apply { addAll(keyArray) }

    private val colorArray = arrayOf(RED, YELLOW, GREEN, BLUE)
    private val colorList = ArrayList<Color>().apply { addAll(colorArray) }

    private val targetList = ArrayList<TargetBox>()
    private val fallingLists = ArrayList<ArrayList<FallingBox>>()

    private var gameMusic: Music? = null
    private var songData: SongData? = null
    private val leadTime = 3f
    private var advanceTimer = 0f
    private val spawnHeight = 650f
    private var noteSpeed = 0f

    //adds space
    private val ocean = BaseActor(0f, 0f, mainStage, animation = spaceAnimation)
            .apply { setSize(800f, 600f) }

    private val message = Message(0f, 0f, uiStage).apply { setOpacity(0f) }
    private val scoreLabel = Label("Score: 0\nMax: 0", Assets.labelStyle)
            .apply { setAlignment(Align.right) }
    private var score = 0
    private var maxScore = 0
    private val timeLabel = Label("Time: 0\nEnd: 0", Assets.labelStyle)
            .apply { setAlignment(Align.right) }
    private var songDuration = 0f

    override fun show() {
        //Handle input from everywhere
        val im = Gdx.input.inputProcessor as InputMultiplexer
        im.addProcessor(this)
        im.addProcessor(uiStage)
        im.addProcessor(mainStage)

        setWorldBounds(baseActor = ocean)

        //Position elements
        val targetTable = Table().apply {
            setFillParent(true)
            add().colspan(4).expandY()
            row()
            mainStage.addActor(this)
        }

        for (i in 0..3) {
            val tb = TargetBox(0f, 0f, mainStage, keyList[i], colorList[i])
            targetList.add(tb)
            targetTable.add(tb).pad(32f)
            fallingLists.add(ArrayList())
        }

        noteSpeed = (spawnHeight - targetList[0].y) / leadTime

        //startButton
        val startButton = TextButton("Start", Assets.textButtonStyle).apply {
            addListener {
                if (!isTouchDownEvent(it)) return@addListener false

                message.displayCountdown()

                val dataFileHandle = Gdx.files.internal("FunkyJunky.key")
                songData = SongData().apply {
                    readFromFile(dataFileHandle)
                    resetIndex()
                    val songFileHandle = Gdx.files.internal(this.songName)
                    gameMusic = Gdx.audio.newMusic(songFileHandle)
                    score = 0
                    maxScore = 100 * keyTimeCount()
                    scoreLabel.setText("Score: $score\n Max: $maxScore")
                    timeLabel.setText("Time: 0\n End: ${songDuration.toInt()}")
                }
                isVisible = false
                true
            }
        }

        //Position interface elements
        with(uiTable) {
            pad(10f)
            add(startButton).width(200f).left()
            add(timeLabel).width(150f)
            add(scoreLabel).width(200f).right()
            row()
            uiTable.add(message).colspan(3).expandX().expandY()
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (songData == null)
            return false

        val keyString = Input.Keys.toString(keycode)
        if (keyList.contains(keyString)) {
            val i = keyList.indexOf(keyString)
            val tb = targetList[i]
            tb.pulse() //pulse effect
            val fallingList = fallingLists[i]
            if (fallingList.size == 0) {
                with(message) {
                    animation = missAnimation
                    pulseFade()
                }
            } else {
                val fb = fallingList[0]
                val distance = Math.abs(fb.y - tb.y)
                when {
                    distance < 8 -> {
                        message.animation = perfectAnimation
                        score += 100
                    }
                    distance < 16 -> {
                        message.animation = greatAnimation
                        score += 80
                    }
                    distance < 24 -> {
                        message.animation = goodAnimation
                        score += 50
                    }
                    distance < 32 -> {
                        message.animation = almostAnimation
                        score += 20
                    }
                    else -> message.animation = missAnimation
                }
                message.pulseFade()
                scoreLabel.setText("Score: $score\nMax: $maxScore")
                // remove from stage immediately
                with(fb) {
                    fallingList.remove(this)
                    setSpeed(0f)
                    flashOut()
                }
            }
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

        if (songData == null)
            return

        if (advanceTimer < leadTime && advanceTimer + delta > leadTime)
            gameMusic!!.play()

        if (advanceTimer < leadTime)
            advanceTimer += delta
        else
            advanceTimer = leadTime + gameMusic!!.position

        while (!songData!!.isFinished() && advanceTimer >= songData!!.getCurrentKeyTime().time) {
            val key = songData!!.getCurrentKeyTime().key
            val i = keyList.indexOf(key)

            with(FallingBox(targetList[i].x, spawnHeight, mainStage)) {
                setSpeed(noteSpeed)
                setMotionAngle(270f)
                color = colorList[i]
                fallingLists[i].add(this)
            }
            songData!!.advanceIndex()
            info { "songData size: ${songData!!.keyTimeList.size}, index: ${songData!!.keyTimeIndex}"}
        }

        if (gameMusic!!.isPlaying)
            timeLabel.setText("Time: ${gameMusic!!.position.toInt()}\nEnd: ${songDuration.toInt()}")

        //Do if any key is pressed
        for (i in 0..3) {
            val key = keyList[i]
            val fallingList = fallingLists[i]
            if (fallingList.size > 0) {
                val fb = fallingList[0]
                val tb = targetList[i]
                if (fb.y < tb.y && !fb.overlaps(tb)) {
                    with(message) {
                        animation = missAnimation
                        pulseFade()
                    }
                    // remove from stage immediately
                    with(fb) {
                        fallingList.remove(this)
                        setSpeed(0f)
                        flashOut()
                    }
                }
            }
        }
        if (songData!!.isFinished() && !gameMusic!!.isPlaying) {
            message.displayCongratulations()
            songData = null
        }
    }
}