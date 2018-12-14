package com.mygdx.game.Actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Align.*
import com.mygdx.game.Utils.Assets.boxAnimation
import com.mygdx.game.Utils.Assets.labelStyle
import com.badlogic.gdx.scenes.scene2d.actions.Actions



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

    fun pulse() {
        val pulse = Actions.sequence(
                Actions.scaleTo(1.2f, 1.2f, 0.05f),
                Actions.scaleTo(1.0f, 1.0f, 0.05f))
        addAction(pulse)
    }
}