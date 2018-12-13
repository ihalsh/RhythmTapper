package com.mygdx.game.Actors

import com.badlogic.gdx.scenes.scene2d.Stage
import com.mygdx.game.Utils.Assets.boxAnimation

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
}