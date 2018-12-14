package com.mygdx.game.Utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
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
//            load("whirlpool.png", Pixmap::class.java)
//            load("Water_Drop.ogg", Sound::class.java)
//            load("Master_of_the_Feast.ogg", Music::class.java)
            load("OpenSans.ttf", FreeTypeFontGenerator::class.java)
            load("button.png", Texture::class.java)
            load("box.png", Texture::class.java)
            load("space.png", Texture::class.java)
            load("perfect.png", Texture::class.java)
            load("great.png", Texture::class.java)
            load("good.png", Texture::class.java)
            load("almost.png", Texture::class.java)
            load("miss.png", Texture::class.java)
            finishLoading()
        }
        info { "Assets loading time: ${TimeUtils.timeSinceMillis(startTime)} milliseconds" }
    }

    private val ktxLogger = ktx.log.logger<Assets>()

    //Single Texture Animation
    val boxAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("box.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }

    val spaceAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("space.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }
    val perfectAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("perfect.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }
    val greatAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("great.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }
    val goodAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("good.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }
    val almostAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("almost.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }
    val missAnimation: Animation<TextureRegion> by lazy {
        Animation(1f, TextureRegion(assetManager.get<Texture>("miss.png")))
                .apply { playMode = Animation.PlayMode.LOOP }
    }

    //Label style
    private val customFont = assetManager.get<FreeTypeFontGenerator>("OpenSans.ttf")
            .generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                size = 40
                color = Color.WHITE
                borderWidth = 2f
                borderColor = Color.BLACK
                borderStraight = true
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.Linear
            })
    val labelStyle: Label.LabelStyle = Label.LabelStyle().apply {
        font = customFont
    }

    //Text button style
    val textButtonStyle = TextButton.TextButtonStyle().apply {
        up = NinePatchDrawable(NinePatch(assetManager.get<Texture>("button.png"),
                24,
                24,
                24,
                24
        ))
        font = customFont
        fontColor = Color.GRAY
    }

    override fun dispose() {
        assetManager.dispose()
        info { "Assets disposed...Ok" }
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable) {
        ktxLogger.error(throwable) { "Couldn't load asset: ${asset.fileName}" }
    }
}