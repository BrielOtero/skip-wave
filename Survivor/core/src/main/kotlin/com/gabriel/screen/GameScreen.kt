package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.FloatingTextComponent
import com.gabriel.component.ImageComponent
import com.gabriel.component.PhysicComponent
import com.gabriel.component.StateComponent
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
    private val gameStage: Stage = Stage(ExtendViewport(16f, 9f))
    private val uiStage: Stage = Stage(ExtendViewport(1280f, 720f))
    private val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/game.atlas"))
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld: World = World {
        inject(gameStage)
        inject("uiStage", uiStage)
        inject(textureAtlas)
        inject(phWorld)

        componentListener<ImageComponent.Companion.ImageComponentListener>()
        componentListener<PhysicComponent.Companion.PhysicComponentListener>()
        componentListener<FloatingTextComponent.Companion.FloatingTextComponentListener>()
        componentListener<StateComponent.Companion.StateComponentListener>()

        system<EntitySpawnSystem>()
        system<CollisionSpawnSystem>()
        system<CollisionDespawnSystem>()
        system<MoveSystem>()
        system<AttackSystem>()
        system<LootSystem>()
        system<DeadSystem>()
        system<LifeSystem>()
        system<PhysicSystem>()
        system<AnimationSystem>()
        system<StateSystem>()
        system<CameraSystem>()
        system<FloatingTextSystem>()
        system<RenderSystem>()
        system<DebugSystem>()

    }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }

        currentMap = TmxMapLoader().load(Gdx.files.internal("maps/demo.tmx").path())
        gameStage.fire(MapChangeEvent(currentMap!!))

        PlayerKeyboardInputProcessor(eWorld)
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        eWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        gameStage.disposeSafely()
        uiStage.disposeSafely()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}