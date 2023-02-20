package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import com.goldev.skipwave.component.ExperienceComponent
import com.goldev.skipwave.component.LifeComponent
import com.goldev.skipwave.component.PlayerComponent
import com.goldev.skipwave.component.WaveComponent
import com.goldev.skipwave.event.EntityDamageEvent
import com.goldev.skipwave.event.EntityExperienceEvent
import com.goldev.skipwave.event.EntityLevelEvent
import com.goldev.skipwave.event.EntityLootEvent
import ktx.log.logger

/**
 * The model of the Game
 *
 * @param world The entities world.
 * @property bundle The bundle with text to show in the UI.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Game model.
 *
 */
class GameModel(
    world: World,
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
    @Qualifier("uiStage") val uiStage: Stage,
) : PropertyChangeSource(), EventListener {

    /**
     *  Component mapper with the entities with PlayerComponent
     */
    private val playerCmps: ComponentMapper<PlayerComponent> = world.mapper()

    /**
     *  Component mapper with the entities with lifeComponent
     */
    private val lifeCmps: ComponentMapper<LifeComponent> = world.mapper()

    /**
     *  Component mapper with the entities with ExperienceComponent
     */
    private val experienceCmps: ComponentMapper<ExperienceComponent> = world.mapper()

    /**
     *  Component mapper with the entities with WaveComponent
     */
    private val waveCmps: ComponentMapper<WaveComponent> = world.mapper()

    /**
     *  Notifiable property with the player life.
     */
    var playerLife by propertyNotify(0f)

    /**
     *  Notifiable property with the player life max.
     */
    var playerLifeMax by propertyNotify(0f)

    /**
     *  Notifiable property with the player life bar.
     */
    var playerLifeBar by propertyNotify(0f)

    /**
     *  Notifiable property with the player experience.
     */
    var playerExperience by propertyNotify(0f)

    /**
     *  Notifiable property with the player experience to next wave.
     */
    var playerExperienceToNextWave by propertyNotify(50f)

    /**
     *  Notifiable property with the player experience bar.
     */
    var playerExperienceBar by propertyNotify(0f)

    /**
     * Property used to calculate the experience bar.
     */
    private var playerExperienceTempValue = 0f

    /**
     *  Notifiable property with the player wave.
     */
    var playerWave by propertyNotify(1)

    /**
     *  Notifiable property with the loot text
     */
    private var lootText by propertyNotify("")


    init {
        gameStage.addListener(this)
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is EntityDamageEvent -> {
                val isPlayer = event.entity in playerCmps
                val lifeCmp = lifeCmps[event.entity]
                if (isPlayer) {
                    playerLife = lifeCmp.life
                    playerLifeMax = lifeCmp.max
                    playerLifeBar = lifeCmp.life / lifeCmp.max
                }
            }

            is EntityExperienceEvent -> {
                val isPlayer = event.entity in playerCmps
                val experienceCmp = experienceCmps[event.entity]
                if (isPlayer) {
                    playerExperience = experienceCmp.experience - playerExperienceTempValue
                    playerExperienceBar = playerExperience / playerExperienceToNextWave
                }
            }

            is EntityLevelEvent -> {
                val isPlayer = event.entity in playerCmps
                val experienceCmp = experienceCmps[event.entity]
                val levelCmp = waveCmps[event.entity]
                if (isPlayer) {
                    playerExperienceToNextWave =
                        experienceCmp.experienceToNextWave - experienceCmp.experience
                    playerExperienceTempValue = experienceCmp.experience
                    playerExperience = 0f
                    playerWave = levelCmp.wave
                    playerExperienceBar = playerExperience / playerExperienceToNextWave
                }

            }

            is EntityLootEvent -> {
                lootText = "You found something [#00ff00]useful[] !"
            }

            else -> return false
        }
        return true
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<GameModel>()
    }
}