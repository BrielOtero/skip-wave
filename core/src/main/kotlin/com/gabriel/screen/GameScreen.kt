package com.gabriel.screen

import com.badlogic.gdx.Application
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
import com.gabriel.input.PlayerTouchInputProcessor
import com.gabriel.system.*
import com.gabriel.ui.model.GameModel
import com.gabriel.ui.model.HUDModel
import com.gabriel.ui.view.gameView
import com.gabriel.ui.view.hudView
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.actors

class GameScreen(
    gameViewport: ExtendViewport,
    uiViewport: ExtendViewport,
) : KtxScreen {
    private val gameStage: Stage = Stage(gameViewport)
    private val uiStage: Stage = Stage(uiViewport)
    private val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/game_assets.atlas"))
    private var currentMap: TiledMap? = null

    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val eWorld = world {
        injectables {
            add("gameStage", gameStage)
            add("uiStage", uiStage)
            add(textureAtlas)
            add(phWorld)
        }
        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
            add<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            add<StateComponent.Companion.StateComponentListener>()
            add<AiComponent.Companion.AiComponentListener>()
        }

        systems {
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
            add<MoveSystem>()
            add<WeaponSystem>()
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

    init {
        uiStage.actors {
            gameView(GameModel(eWorld, gameStage))
            hudView(HUDModel(eWorld, uiStage))
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

        if (Gdx.app.type == Application.ApplicationType.Desktop) {
            PlayerKeyboardInputProcessor(eWorld, uiStage)
        } else {
            PlayerTouchInputProcessor(eWorld, uiStage)
        }

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