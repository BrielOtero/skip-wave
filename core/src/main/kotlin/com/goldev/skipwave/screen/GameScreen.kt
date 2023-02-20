package com.goldev.skipwave.screen

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
import com.goldev.skipwave.SkipWave
import com.goldev.skipwave.SkipWave.Companion.ANIMATION_DURATION
import com.goldev.skipwave.input.PlayerTouchInputProcessor
import com.goldev.skipwave.preferences.saveGamePreferences
import com.github.quillraven.fleks.world
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.*
import com.goldev.skipwave.input.PlayerKeyboardInputProcessor
import com.goldev.skipwave.system.*
import com.goldev.skipwave.ui.model.*
import com.goldev.skipwave.ui.view.*
import ktx.actors.alpha
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import ktx.scene2d.actors

/**
 *  It's a screen that contains a game
 *
 *  @property game The property with game data.
 *  @constructor Creates GameScreen
 */
class GameScreen(private val game: SkipWave) : KtxScreen, EventListener {

    /**
     *  It's a property with stage that the game is being rendered on.
     */
    private val gameStage = game.gameStage

    /**
     *  It's a property with stage that the UI is being rendered on.
     */
    private val uiStage = game.uiStage

    /**
     *   It's a property with texture atlas that contains all the game assets.
     */
    private val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/game_assets.atlas"))

    /**
     *  It's a property that contains the current map that is being rendered.
     */
    private var currentMap: TiledMap? = null

    /**
     *  It's a property with the physics world.
     */
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    /**
     *  It's a property with the entities world.
     */
    private val eWorld = world {

        /**
         * Add into the system common variables.
         */
        injectables {
            add("gameStage", gameStage)
            add("uiStage", uiStage)
            add(textureAtlas)
            add(phWorld)
            add(game.gamePreferences)
        }

        /**
         * Add into the system the listening components
         */
        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
            add<FloatingTextComponent.Companion.FloatingTextComponentListener>()
            add<StateComponent.Companion.StateComponentListener>()
            add<AiComponent.Companion.AiComponentListener>()
        }

        /**
         * Add into the system the listening systems that the game need.
         */
        systems {
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
            add<MoveSystem>()
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
            add<ShakeSystem>()
            add<DebugSystem>()
        }
    }

    init {
        gameStage.root.alpha = 0f
        uiStage.root.alpha = 0f
        gameStage.addListener(this)
        uiStage.addListener(this)
        uiStage.actors {
            gameView(GameModel(eWorld, game.bundle, gameStage, uiStage))
            skillUpgradeView(SkillUpgradeModel(eWorld, game.bundle, gameStage, uiStage))
            recordsView(RecordsModel(eWorld, game.bundle, game.gamePreferences, gameStage, uiStage))
            pauseView(PauseModel(game.bundle, gameStage, uiStage))
            settingsView(SettingsModel(game.bundle, game.gamePreferences, gameStage, uiStage))
            tutorialView(TutorialModel(game.bundle, game.gamePreferences, gameStage, uiStage))
            touchpadView(TouchpadModel(eWorld, uiStage))
        }


    }


    /**
     * This function is called when this GameScreen appears.
     */
    override fun show() {
        this.gameStage.addAction(fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
        this.uiStage.addAction(fadeIn(ANIMATION_DURATION, Interpolation.elasticIn))
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
            PlayerTouchInputProcessor(uiStage)
        }

    }

    /**
     * Pause all systems except the mandatory ones.
     *
     * @param pause Whether to pause or unpause the world.
     */
    private fun pauseWorld(pause: Boolean) {
        val mandatorySystems = setOf(
            AnimationSystem::class,
            CameraSystem::class,
            RenderSystem::class,
            DebugSystem::class,
            AudioSystem::class,
        )

        eWorld.systems
            .filter { it::class !in mandatorySystems }
            .forEach { it.enabled = !pause }
    }

    /**
     * This function is called when the user exit the app without close.
     */
    override fun pause() {
        if (!uiStage.actors.filterIsInstance<SkillUpgradeView>().first().isVisible &&
            !uiStage.actors.filterIsInstance<PauseView>()
                .first().isVisible && game.gamePreferences.game.tutorialComplete
        ) {
            gameStage.fire(ShowPauseViewEvent())
        }

    }

    /**
     * Update the timepiece, then update the world.
     *
     * @param delta The time in seconds since the last frame.
     */
    override fun render(delta: Float) {
        val dt = delta.coerceAtMost(0.25f)
        GdxAI.getTimepiece().update(dt)
        eWorld.update(dt)
    }

    /**
     * It disposes all resources when GameScreen is closed.
     */
    override fun dispose() {
        super.dispose()
        eWorld.dispose()
        textureAtlas.disposeSafely()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
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

                game.removeScreen<GameScreen>()
                game.setScreen<MainMenuScreen>()
                super.hide()
                this.dispose()
            }

            is NewMapEvent -> {
                currentMap = TmxMapLoader().load(event.path)
                gameStage.fire(MapChangeEvent(currentMap!!))
            }

            is SavePreferencesEvent -> {
                log.debug { "SAVE GAME" }
                log.debug { "${game.gamePreferences.settings.musicVolume}" }
                game.preferences.saveGamePreferences(game.gamePreferences)
            }

            is ExitGameEvent -> {
                log.debug { "EXIT GAME" }
                Gdx.app.exit()
            }


            else -> return false
        }
        return true
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<GameScreen>()
    }
}