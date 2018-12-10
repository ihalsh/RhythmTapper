package com.mygdx.game.Utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.TimeUtils
import ktx.log.info

object Assets : Disposable, AssetErrorListener {

    val assetManager: AssetManager = AssetManager().apply {
        setErrorListener(Assets)
        setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(InternalFileHandleResolver()))
    }

    init {
        val startTime = TimeUtils.millis()
        with(assetManager) {
            //            logger = com.badlogic.gdx.utils.Logger("AssetManager", com.badlogic.gdx.Application.LOG_INFO)
//            load("turtle-1.png", Texture::class.java)
//            load("whirlpool.png", Pixmap::class.java)
//            load("OpenSans.ttf", FreeTypeFontGenerator::class.java)
//            load("Water_Drop.ogg", Sound::class.java)
//            load("Master_of_the_Feast.ogg", Music::class.java)
            finishLoading()
        }
        info { "Assets loading time: ${TimeUtils.timeSinceMillis(startTime)} milliseconds" }
    }

    private val ktxLogger = ktx.log.logger<Assets>()

    override fun dispose() {
        assetManager.dispose()
        info { "Assets disposed...Ok" }
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        ktxLogger.error(throwable) { "Couldn't load asset: ${asset.fileName}" }
    }
}