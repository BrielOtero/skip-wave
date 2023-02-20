package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.EnemyDeathEvent
import com.goldev.skipwave.event.EntityDeathEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*
import com.goldev.skipwave.component.*
import ktx.log.logger

/**
 * System that takes care of the deaths in the game.
 *
 * @property deadCmps Entities with DeadComponent in the world.
 * @property lifeCmps Entities with LifeComponent in the world.
 * @property animationCmps Entities with AnimationComponent in the world.
 * @property enemyCmps Entities with EnemyComponent in the world.
 * @property experienceCmps Entities with ExperienceComponent in the world.
 * @property playerCmps Entities with PlayerComponent in the world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Dead system
 */
@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val enemyCmps: ComponentMapper<EnemyComponent>,
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>,
    @Qualifier("gameStage") private val gameStage: Stage

) : IteratingSystem() {

    /**
     * If the entity is dead, and the death animation is done, remove the entity from the world
     *
     * @param entity The entity that is being processed
     * @return The return value of the last expression in the block.
     */
    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]
        if (deadCmp.reviveTime == 0f) {

            if (deadCmp.waitForAnimation) {
                if (animationCmps[entity].isAnimationDone) {
                    deadCmp.waitForAnimation = false
                    world.remove(entity)
                }
                return
            }

            gameStage.fire(EntityDeathEvent(animationCmps[entity].model))
            if (entity in enemyCmps) {
                gameStage.fire(EnemyDeathEvent(experienceCmps[entity]))
            }

            if (animationCmps[entity].isAnimationDone) {
                world.remove(entity)
            } else {
                deadCmp.waitForAnimation = true
            }
//            world.remove(entity)

            return
        }


        deadCmp.reviveTime -= deltaTime
        if (deadCmp.reviveTime <= 0f) {

//            if(entity in playerCmps){
//                log.debug { "PLAYER DEATH EVENT" }
//                gameStage.fire(PlayerDeathEvent())
//                return;
//            }

            with(lifeCmps[entity]) { life = max }
            configureEntity(entity) { deadCmps.remove(entity) }
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<DeadSystem>()
    }
}