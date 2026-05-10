package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.*
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import com.goldev.skipwave.preferences.GamePreferences
import ktx.log.logger

/**
 * System that takes care of the skills upgrade in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @property bundle The bundle with text to show in the UI.
 * @constructor Create empty Skill upgrade system.
 */
class SkillUpgradeSystem(
    private val gameStage: Stage = inject("gameStage"),
    val bundle: I18NBundle = inject(),
) : IntervalSystem(), EventListener {

    /**
     *  A family of entities that have the PlayerComponent.
     */
    private var playerEntities = world.family { all(PlayerComponent) }

    /**
     *  A family of entities that have the WeaponComponent.
     */
    private var weaponEntities = world.family { all(WeaponComponent) }

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
                with(world) {
                    val player = playerEntities.first()
                    when (event.skill.skillEntityId) {
                        0 -> {
                            log.debug { "Life before ${player[LifeComponent].max}" }
                            player[LifeComponent].max += event.skill.onLevelUP
                            player[LifeComponent].life = player[LifeComponent].max
                            log.debug { "Life after ${player[LifeComponent].max}" }
                        }

                        1 -> {
                            log.debug { "Regeneration before ${player[LifeComponent].regeneration}" }
                            player[LifeComponent].regeneration += event.skill.onLevelUP
                            log.debug { "Regeneration after ${player[LifeComponent].regeneration}" }
                        }

                        2 -> {
                            log.debug { "Speed before ${player[MoveComponent].speed}" }
                            player[MoveComponent].speed += (event.skill.onLevelUP / 10)
                            log.debug { "Speed after ${player[MoveComponent].speed}" }
                        }

                        3 -> {
                            weaponEntities.forEach { weapon ->
                                log.debug { "Cooldown before %.2f".format(weapon[AttackComponent].maxCooldown) }
                                log.debug { "Cooldown change %.2f".format(((event.skill.onLevelUP * -1) / 10)) }
                                weapon[AttackComponent].maxCooldown -= ((event.skill.onLevelUP * -1) / 10)
                                log.debug { "Cooldown after %.2f".format(weapon[AttackComponent].maxCooldown) }

                            }
                        }

                        4 -> {

                            weaponEntities.forEach { weapon ->
                                log.debug { "Damage before ${weapon[AttackComponent].damage}" }
                                weapon[AttackComponent].damage += event.skill.onLevelUP.toInt()
                                log.debug { "Damage after ${weapon[AttackComponent].damage}" }
                            }
                        }
                    }
                }
                skillsModel[event.skill.skillEntityId].skillLevel += 1

            }

            else -> return false
        }
        return true
    }


    /**
     *  It's an enum class that holds all the player's skills.
     *
     *  @property skillEntityId The id of skill.
     *  @property skillLevel The level of skill.
     *  @property onLevelUP The value to apply on level up.
     *  @constructor Creates Skill with default values
     */
    enum class Skill(
        val skillEntityId: Int = 0,
        var skillLevel: Int = 0,
        var onLevelUP: Float = 0f,
    ) {
        PLAYER_LIFE(0, 0, 250f),
        PLAYER_REGENERATION(1, 0, 2f),
        PLAYER_SPEED(2, 0, 1f),
        PLAYER_COOLDOWN(3, 0, -1f),
        PLAYER_DAMAGE(4, 0, 10f);

        /**
         * Resets the skillLevel variable to 0
         */
        fun resetSkillLevel() {
            skillLevel = 0
        }

        /**
         *  It's a property with the key of texture atlas
         *
         *  @return The key of the texture atlas.
         */
        var atlasKey: String = this.toString().lowercase()
    }

    companion object {

        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeSystem>()
    }
}

