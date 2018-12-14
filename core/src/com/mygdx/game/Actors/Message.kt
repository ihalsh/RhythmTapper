package com.mygdx.game.Actors

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.mygdx.game.Utils.Assets.almostAnimation
import com.mygdx.game.Utils.Assets.goodAnimation
import com.mygdx.game.Utils.Assets.greatAnimation
import com.mygdx.game.Utils.Assets.missAnimation
import com.mygdx.game.Utils.Assets.perfectAnimation

class Message(
        x: Float,
        y: Float,
        s: Stage) : BaseActor(x, y, s, almostAnimation) {

    fun pulseFade() {
        setOpacity(1f)
        clearActions()
        val pulseFade = Actions.sequence(
                Actions.scaleTo(1.1f, 1.1f, 0.05f),
                Actions.scaleTo(1.0f, 1.0f, 0.05f),
                Actions.delay(1f),
                Actions.fadeOut(0.5f))
        addAction(pulseFade)
    }
}