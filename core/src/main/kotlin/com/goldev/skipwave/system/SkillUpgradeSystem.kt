package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.*
import ktx.log.logger

/**
 * System that takes care of the skills upgrade in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @property lifeCmps Entities with LifeComponent in the world.
 * @property moveCmps Entities with MoveComponent in the world.
 * @property attackCmps Entities with AttackComponent in the world.
 * @constructor Create empty Skill upgrade system.
 */
class SkillUpgradeSystem(
    @Qualifier("gameStage") private val gameStage: Stage,
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val moveCmps: ComponentMapper<MoveComponent>,
    private val attackCmps: ComponentMapper<AttackComponent>,

    ) : IntervalSystem(), EventListener {

    /**
     *  A family of entities that have the PlayerComponent.
     */
    private var playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    /**
     *  A family of entities that have the WeaponComponent.
     */
    private var weaponEntities = world.family(allOf = arrayOf(WeaponComponent::class))

    /**
     *  All the skills.
     */
    private var skillsModel = Skill.values()

    /**
     *  The number of skills that are displayed on the screen.
     */
    private val numSkill: Int = 3

    init {
        skillsModel.forEach { skill ->
            skill.resetSkillLevel()
        }
    }

    /**
     * Called every tick of SkillUpgradeSystem
     */
    override fun onTick() {
    }

    /**
     * It handles events
     *
     * @param event The event to handle.
     * @return If true, the event is consumed by the method and not sent to the next one.
     */
    override fun handle(event: Event): Boolean {
        when (event) {
            is EntityLevelEvent -> {
                log.debug { "Skill System Level Event" }
                val shuffledSkills = (skillsModel.indices).shuffled().take(numSkill)
                gameStage.fire(
                    SkillEvent(
                        skillsModel[shuffledSkills[0]],
                        skillsModel[shuffledSkills[1]],
                        skillsModel[shuffledSkills[2]]
                    )
                )
                gameStage.fire(GamePauseEvent())
            }

            is SkillApplyEvent -> {
                when (event.skill.skillName) {
                    "Life" -> {
                        log.debug { "Life before ${lifeCmps[playerEntities.first()].max}" }
                        lifeCmps[playerEntities.first()].max += event.skill.onLevelUP
                        log.debug { "Life after ${lifeCmps[playerEntities.first()].max}" }
                    }

                    "Regeneration" -> {
                        log.debug { "Regeneration before ${lifeCmps[playerEntities.first()].regeneration}" }
                        lifeCmps[playerEntities.first()].regeneration += event.skill.onLevelUP
                        log.debug { "Regeneration after ${lifeCmps[playerEntities.first()].regeneration}" }
                    }

                    "Speed" -> {
                        log.debug { "Speed before ${moveCmps[playerEntities.first()].speed}" }
                        moveCmps[playerEntities.first()].speed += (event.skill.onLevelUP / 10)
                        log.debug { "Speed after ${moveCmps[playerEntities.first()].speed}" }
                    }

                    "Cooldown" -> {
                        weaponEntities.forEach { weapon ->
                            log.debug { "Cooldown before %.2f".format(attackCmps[weapon].maxCooldown) }
                            log.debug { "Cooldown change %.2f".format(((event.skill.onLevelUP * -1) / 10)) }
                            attackCmps[weapon].maxCooldown -= ((event.skill.onLevelUP * -1) / 10)
                            log.debug { "Cooldown after %.2f".format(attackCmps[weapon].maxCooldown) }

                        }
                    }

                    "Damage" -> {

                        weaponEntities.forEach { weapon ->
                            log.debug { "Damage before ${attackCmps[weapon].damage}" }
                            attackCmps[weapon].damage += event.skill.onLevelUP.toInt()
                            log.debug { "Damage after ${attackCmps[weapon].damage}" }
                        }
                    }
                }
                skillsModel[event.skill.skillEntityId].skillLevel += 1

            }

            else -> return false
        }
        return true
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeSystem>()
    }
}

