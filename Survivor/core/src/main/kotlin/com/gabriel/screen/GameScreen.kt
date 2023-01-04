package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.*
import com.gabriel.event.MapChangeEvent
import com.gabriel.event.fire
import com.gabriel.input.PlayerKeyboardInputProcessor
import com.gabriel.system.*
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import java.time.Clock.system

class GameScreen : KtxScreen {
    private val gameStage: Stage = Stage(ExtendViewport(16f, 9f))
    private val uiStage: Stage = Stage(ExtendViewport(320f, 180f))
    private val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/game.atlas"))
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld= world {
        injectables{
            add("gameStage",gameStage)
            add("uiStage", uiStage)
            add(textureAtlas)
            add(phWorld)
        }

        components{
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
            add<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            add<StateComponent.Companion.StateComponentListener>()
            add<AiComponent.Companion.AiComponentListener>()
        }

        systems{
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
            add<MoveSystem>()
            add<AttackSystem>()
            add<LootSystem>()
            add<DeadSystem>()
            add<LifeSystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<StateSystem>()
            add<AiSystem>()
            add<CameraSystem>()
            add<FloatingTextSystem>()
            add<RenderSystem>()
            add<AudioSystem>()
            add<DebugSystem>()
        }
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
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(dt)
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