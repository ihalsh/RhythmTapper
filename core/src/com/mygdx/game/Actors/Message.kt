package com.mygdx.game.Actors

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.mygdx.game.Utils.Assets.almostAnimation
import com.mygdx.game.Utils.Assets.blipSound
import com.mygdx.game.Utils.Assets.congratulationsAnimation
import com.mygdx.game.Utils.Assets.countdown1Animation
import com.mygdx.game.Utils.Assets.countdown2Animation
import com.mygdx.game.Utils.Assets.countdown3Animation
import com.mygdx.game.Utils.Assets.countdownGoAnimation
import com.mygdx.game.Utils.Assets.toneSound

class Message(
        x: Float,
        y: Float,
        s: Stage) : BaseActor(x, y, s, almostAnimation) {

    fun pulseFade() {
        setOpacity(1f)
        clearActions()
        val pulseFade = sequence(
                scaleTo(1.1f, 1.1f, 0.05f),
                scaleTo(1.0f, 1.0f, 0.05f),
                delay(1f),
                fadeOut(0.5f))
        addAction(pulseFade)
    }

    fun displayCountdown() {
        val countdown = sequence(
                Actions.run { animation = countdown3Animation },
                Actions.run { blipSound.play() },
                alpha(1f),
                scaleTo(1.1f, 1.1f, 0.05f), scaleTo(1.0f, 1.0f, 0.05f),
                delay(0.5f), fadeOut(0.4f),
                Actions.run { animation = countdown2Animation },
                Actions.run { blipSound.play() },
                alpha(1f),
                scaleTo(1.1f, 1.1f, 0.05f), scaleTo(1.0f, 1.0f, 0.05f),
                delay(0.5f), fadeOut(0.4f),
                Actions.run { animation = countdown1Animation },
                Actions.run { blipSound.play() },
                alpha(1f),
                scaleTo(1.1f, 1.1f, 0.05f), scaleTo(1.0f, 1.0f, 0.05f),
                delay(0.5f), fadeOut(0.4f),
                Actions.run { animation = countdownGoAnimation },
                Actions.run { toneSound.play() },
                alpha(1f),
                fadeOut(1f))
        addAction(countdown)
    }

    fun displayCongratulations() {
        setOpacity(0f)
        animation = congratulationsAnimation
        setScale(2f)
        val delayedFade = sequence(
                delay(1f),
                fadeIn(4f))
        addAction(delayedFade)
    }
}