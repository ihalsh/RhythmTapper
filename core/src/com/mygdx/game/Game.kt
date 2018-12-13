package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.mygdx.game.Utils.Assets
import ktx.app.KtxGame

class Game : KtxGame<Screen>() {

    override fun create() {

        // prepare for multiple classes/stages to receive discrete input
        val im = InputMultiplexer()
        Gdx.input.inputProcessor = im

        //Initialize assetManager (load assets)
        Assets

        addScreen(RecorderScreen(this))
        addScreen(RhythmScreen())

        setScreen<RhythmScreen>()
    }


    override fun dispose() {
        Assets.dispose()
    }
}
