package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.EnemyDeathEvent
import com.goldev.skipwave.event.EntityDeathEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.log.logger

/**
 * System that takes care of the deaths in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Dead system
 */
class DeadSystem(
    private val gameStage: Stage = inject("gameStage"),
) : IteratingSystem(
    family = family { all(DeadComponent) }
) {

    /**
     * If the entity is dead, and the death animation is done, remove the entity from the world
     *
     * @param entity The entity that is being processed
     * @return The return value of the last expression in the block.
     */
    override fun onTickEntity(entity: Entity) {
        val deadCmp = entity[DeadComponent]
        if (deadCmp.reviveTime == 0f) {

            if (deadCmp.waitForAnimation) {
                if (entity[AnimationComponent].isAnimationDone) {
                    deadCmp.waitForAnimation = false
                    world -= entity
                }
                return
            }

            gameStage.fire(EntityDeathEvent(entity[AnimationComponent].model))
            if (entity has EnemyComponent) {
                gameStage.fire(EnemyDeathEvent(entity[ExperienceComponent]))
            }

            if (entity[AnimationComponent].isAnimationDone) {
                world -= entity
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

            with(entity[LifeComponent]) { life = max }
            entity.configure { it -= DeadComponent }
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<DeadSystem>()
    }
}
