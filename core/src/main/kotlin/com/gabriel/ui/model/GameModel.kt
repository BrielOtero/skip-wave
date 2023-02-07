package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.gabriel.component.*
import com.gabriel.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.log.logger

class GameModel(
    world: World,
    val bundle: I18NBundle,
    @Qualifier("gameStage") val gameStage: Stage,
) : PropertyChangeSource(), EventListener {

    private val playerCmps: ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeCmps: ComponentMapper<LifeComponent> = world.mapper()
    private val experienceCmps: ComponentMapper<ExperienceComponent> = world.mapper()
    private val levelCmps: ComponentMapper<WaveComponent> = world.mapper()

    var playerLife by propertyNotify(0f)
    var playerLifeMax by propertyNotify(0f)
    var playerLifeBar by propertyNotify(0f)

    var playerExperience by propertyNotify(0f)
    var playerExperienceToNextLevel by propertyNotify(50f)
    var playerExperienceBar by propertyNotify(0f)
    private var playerExperienceTempValue = 0f

    var playerLevel by propertyNotify(1)

    var lootText by propertyNotify("")


    init {
        gameStage.addListener(this)
    }


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
                    playerExperienceBar = playerExperience / playerExperienceToNextLevel
                }
            }

            is EntityLevelEvent -> {
                val isPlayer = event.entity in playerCmps
                val experienceCmp = experienceCmps[event.entity]
                val levelCmp = levelCmps[event.entity]
                if (isPlayer) {
                    playerExperienceToNextLevel = experienceCmp.experienceToNextLevel - experienceCmp.experience
                    playerExperienceTempValue = experienceCmp.experience
                    playerExperience = 0f
                    playerLevel = levelCmp.wave
                    playerExperienceBar = playerExperience / playerExperienceToNextLevel
                }

            }

            is EntityLootEvent -> {
                lootText = "You found something [#00ff00]useful[] !"
            }

            else -> return false
        }
        return true
    }
    companion object{
        private val log = logger<GameModel>()
    }
}