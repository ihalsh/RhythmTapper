package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.mygdx.game.Actors.BaseActor
import com.mygdx.game.Actors.BaseActor.Companion.setWorldBounds
import com.mygdx.game.Actors.FallingBox
import com.mygdx.game.Actors.TargetBox
import com.mygdx.game.Utils.Assets.spaceAnimation
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
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

    //adds space
    private val ocean = BaseActor(0f, 0f, mainStage, animation = spaceAnimation)
            .apply { setSize(800f, 600f) }

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
    }

    override fun keyDown(keyCode: Int): Boolean {

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

    }
}