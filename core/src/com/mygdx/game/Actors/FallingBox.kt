package com.mygdx.game.Actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mygdx.game.Utils.Assets.boxAnimation
import com.badlogic.gdx.scenes.scene2d.actions.Actions



class FallingBox(
        x: Float,
        y: Float,
        s: Stage) : BaseActor(x = x, y = y, stage = s, animation = boxAnimation) {

    init {
        setScale(0.75f, 0.75f)
    }

    override fun act(dt: Float) {
        super.act(dt)
        applyPhysics(dt)
    }

    fun flashOut() {
        val duration = 0.25f
        val flashOut = Actions.parallel(
                Actions.scaleTo(1.5f, 1.5f, duration),
                Actions.color(WHITE, duration),
                Actions.fadeOut(duration))
        addAction(flashOut)
        addAction(Actions.after(Actions.removeActor()))
    }
}