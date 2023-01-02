package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.ImageComponent
import com.gabriel.component.PhysicComponent
import com.gabriel.event.MapChangeEvent
import com.gabriel.event.fire
import com.gabriel.input.PlayerKeyboardInputProcessor
import com.gabriel.system.*
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class GameScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/game.atlas"))
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld: World = World {
        inject(stage)
        inject(textureAtlas)
        inject(phWorld)

        componentListener<ImageComponent.Companion.ImageComponentListener>()
        componentListener<PhysicComponent.Companion.PhysicComponentListener>()

        system<EntitySpawnSystem>()
        system<CollisionSpawnSystem>()
        system<CollisionDespawnSystem>()
        system<MoveSystem>()
        system<PhysicSystem>()
        system<AnimationSystem>()
        system<CameraSystem>()
        system<RenderSystem>()
        system<DebugSystem>()

    }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }

        currentMap = TmxMapLoader().load(Gdx.files.internal("maps/demo.tmx").path())
        stage.fire(MapChangeEvent(currentMap!!))

        PlayerKeyboardInputProcessor(eWorld, eWorld.mapper())

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        eWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        stage.disposeSafely()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}