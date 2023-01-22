package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.*
import com.gabriel.event.EntityDamageEvent
import com.gabriel.event.EntityExperienceEvent
import com.gabriel.event.EntityLevelEvent
import com.gabriel.event.EntityLootEvent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

class GameModel(
    world: World,
    @Qualifier("gameStage") val gameStage: Stage,
) : PropertyChangeSource(), EventListener {

    private val playerCmps: ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeCmps: ComponentMapper<LifeComponent> = world.mapper()
    private val experienceCmps: ComponentMapper<ExperienceComponent> = world.mapper()
    private val levelCmps: ComponentMapper<LevelComponent> = world.mapper()

    var playerLife by propertyNotify(0f)
    var playerLifeMax by propertyNotify(0f)
    var playerLifeBar by propertyNotify(0f)

    var playerExperience by propertyNotify(0f)
    var playerExperienceToNextLevel by propertyNotify(0f)
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
                    playerLevel = levelCmp.level
                }

            }

            is EntityLootEvent -> {
                lootText = "You found something [#00ff00]useful[] !"
            }

            else -> return false
        }
        return true
    }
}