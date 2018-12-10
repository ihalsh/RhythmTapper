package com.mygdx.game

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.game.Utils.Assets.oceanTexture
import com.mygdx.game.Utils.Assets.starfishTexture
import com.mygdx.game.Utils.Assets.turtleTexture
import com.mygdx.game.Utils.Assets.winMessageTexture
import com.mygdx.game.Utils.Constants.Companion.WORLD_HEIGHT
import com.mygdx.game.Utils.Constants.Companion.WORLD_WIDTH
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.use

class GameScreen : KtxScreen {

    private val shapeRenderer = ShapeRenderer()
    private val viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    private var batch = SpriteBatch()

    private val position: Vector2 = Vector2(20f, 20f)
    private val turtleRectangle = Rectangle(position.x,
            position.y,
            turtleTexture.width.toFloat(),
            turtleTexture.height.toFloat())

    private val starfishPosition: Vector2 = Vector2(380f, 380f)
    private val starfishRectangle = Rectangle(starfishPosition.x,
            starfishPosition.y,
            starfishTexture.width.toFloat(),
            starfishTexture.height.toFloat())

    private var win: Boolean = false

    override fun render(delta: Float) {
        viewport.apply()
        clearScreen(BLACK.r, BLACK.g, BLACK.b)

        // check user input
        when {
            input.isKeyPressed(LEFT) -> when {
                input.isKeyPressed(UP) -> position.mulAdd(Vector2(-2f, 2f), 1f)
                input.isKeyPressed(DOWN) -> position.mulAdd(Vector2(-2f, -2f), 1f)
                else -> position.x -= 2f
            }
            input.isKeyPressed(RIGHT) -> when {
                input.isKeyPressed(UP) -> position.mulAdd(Vector2(2f, 2f), 1f)
                input.isKeyPressed(DOWN) -> position.mulAdd(Vector2(2f, -2f), 1f)
                else -> position.x += 2f
            }
            input.isKeyPressed(UP) -> position.y += 2f
            input.isKeyPressed(DOWN) -> position.y -= 2f
        }

        // update turtle rectangle location
        turtleRectangle.setPosition(position.x, position.y)

        // check win condition: turtle must be overlapping starfish
        if (turtleRectangle.overlaps(starfishRectangle)) win = true

        update(delta)
        draw()
    }

    private fun update(delta: Float) {}

    private fun draw() {
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            it.draw(oceanTexture, 0f, 0f)
            it.draw(turtleTexture, position.x, position.y)
            if (win) it.draw(winMessageTexture,
//                    viewport.worldWidth - winMessageTexture.width / 2,
                    180f,
                    180f)
            else it.draw(starfishTexture, starfishPosition.x, starfishPosition.y)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        shapeRenderer.dispose()
    }
}