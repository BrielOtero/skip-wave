package com.gabriel.screen

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn
import com.gabriel.SkipWave
import com.gabriel.SkipWave.Companion.ANIMATION_DURATION
import com.gabriel.component.*
import com.gabriel.event.*
import com.gabriel.input.PlayerKeyboardInputProcessor
import com.gabriel.input.PlayerTouchInputProcessor
import com.gabriel.preferences.saveGamePreferences
import com.gabriel.system.*
import com.gabriel.ui.model.*
import com.gabriel.ui.view.*
import com.github.quillraven.fleks.world
import ktx.actors.alpha
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.actors

class GameScreen(private val game: SkipWave) : KtxScreen, EventListener {
    private val gameStage = game.gameStage
    private val uiStage = game.uiStage
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
            add(game.gamePreferences)
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
            add<ExperienceSystem>()
            add<DeadSystem>()
            add<LifeSystem>()
            add<WaveSystem>()
            add<MapSystem>()
            add<SkillUpgradeSystem>()
            add<EnemySystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<StateSystem>()
            add<AiSystem>()
            add<CameraSystem>()
            add<FloatingTextSystem>()
            add<RenderSystem>()
            add<VibrateSystem>()
            add<AudioSystem>()
            add<DebugSystem>()
        }
    }

    init {
        gameStage.root.alpha = 0f
        uiStage.root.alpha = 0f
        gameStage.addListener(this)
        uiStage.addListener(this)
        uiStage.actors {
            gameView(GameModel(eWorld, game.bundle, gameStage,uiStage))
            skillUpgradeView(SkillUpgradeModel(eWorld, game.bundle, gameStage, uiStage))
            touchpadView(TouchpadModel(eWorld, uiStage))
            recordsView(RecordsModel(eWorld, game.bundle, game.gamePreferences, gameStage, uiStage))
            pauseView(PauseModel(game.bundle,gameStage,uiStage))
            settingsView(SettingsModel(game.bundle,game.gamePreferences,gameStage,uiStage))
        }


    }


    override fun show() {
        this.gameStage.addAction(fadeIn(ANIMATION_DURATION, Interpolation.circleIn))
        this.uiStage.addAction(fadeIn(ANIMATION_DURATION, Interpolation.circleIn))
//        uiStage.isDebugAll = true

        log.debug { "GameScreen gets shown" }

        eWorld.systems.forEach { system ->
            if (system is EventListener) {
                gameStage.addListener(system)
            }
        }
        currentMap = TmxMapLoader().load(Gdx.files.internal("maps/map_0.tmx").path())
        gameStage.fire(MapChangeEvent(currentMap!!))


        if (Gdx.app.type == Application.ApplicationType.Desktop) {
            PlayerKeyboardInputProcessor(eWorld, uiStage)
        } else {
            PlayerTouchInputProcessor(eWorld, uiStage)
        }

    }

    private fun pauseWorld(pause: Boolean) {
        val mandatorySystems = setOf(
            AnimationSystem::class,
            CameraSystem::class,
            RenderSystem::class,
            DebugSystem::class,
        )

        eWorld.systems
            .filter { it::class !in mandatorySystems }
            .forEach { it.enabled = !pause }
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is GamePauseEvent -> {
                log.debug { "PAUSE game" }
                pauseWorld(true)
            }

            is GameResumeEvent -> {
                log.debug { "RESUME game" }
                pauseWorld(false)
            }

            is SetMainMenuScreenEvent -> {
                log.debug { "SET SCREEN: MainMenu" }

                gameStage.clear()
                uiStage.clear()

                game.addScreen(MainMenuScreen(game))

                game.gameStage.root.alpha = 0f
                game.uiStage.root.alpha = 0f

                game.setScreen<MainMenuScreen>()
                game.removeScreen<GameScreen>()
                super.hide()
                this.dispose()
            }

            is NewMapEvent -> {
                currentMap= TmxMapLoader().load(event.path)
                gameStage.fire(MapChangeEvent(currentMap!!))
            }

            is SavePreferencesEvent ->{
                log.debug { "SAVE GAME" }
                log.debug { "${game.gamePreferences.settings.musicVolume}" }
                game.preferences.saveGamePreferences(game.gamePreferences)
            }

            is ExitGameEvent ->{
                log.debug { "EXIT GAME" }
                Gdx.app.exit()
            }


            else -> return false
        }
        return true
    }

//    override fun pause() = pauseWorld(true)
//
//    override fun resume() = pauseWorld(false)

    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(dt)
    }

    override fun dispose() {
        super.dispose()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}