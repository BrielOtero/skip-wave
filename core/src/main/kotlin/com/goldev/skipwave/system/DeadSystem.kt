package com.goldev.skipwave.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.EnemyDeathEvent
import com.goldev.skipwave.event.EntityDeathEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*
import com.goldev.skipwave.component.*
import ktx.log.logger

@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val enemyCmps: ComponentMapper<EnemyComponent>,
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    private val playerCmps:ComponentMapper<PlayerComponent>,
    @Qualifier("gameStage") private val gameStage: Stage
) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]
        if (deadCmp.reviveTime == 0f) {

//            if (deadCmp.waitForAnimation) {
//                if (animationCmps[entity].isAnimationDone) {
//                    deadCmp.waitForAnimation=false
//                    world.remove(entity)
//                }
//                return
//            }

            gameStage.fire(EntityDeathEvent(animationCmps[entity].model))
            if (entity in enemyCmps) {
                gameStage.fire(EnemyDeathEvent(experienceCmps[entity]))
            }
//            if (animationCmps[entity].isAnimationDone) {
//                world.remove(entity)
//            } else {
//                deadCmp.waitForAnimation = true
//            }
                world.remove(entity)

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
    companion object{
        private val log = logger<DeadSystem>()
    }
}