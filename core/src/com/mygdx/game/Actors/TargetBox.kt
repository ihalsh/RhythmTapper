package com.mygdx.game.Actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Align.*
import com.mygdx.game.Utils.Assets.boxAnimation
import com.mygdx.game.Utils.Assets.labelStyle

class TargetBox(x: Float,
                y: Float,
                s: Stage,
                letter: String,
                color: Color) : BaseActor(x = x, y = y, stage = s, animation = boxAnimation) {

    init {
        setSize(64f, 64f)
    }

    // add a centered label containing letter with given color
    val letterLabel = Label(letter, labelStyle).apply {
        setSize(64f, 64f)
        setAlignment(center)
        setColor(color)
        this@TargetBox.addActor(this)
    }
}