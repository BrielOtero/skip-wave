package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.*
import com.gabriel.event.EnemyDeathEvent
import com.gabriel.event.EntityDeathEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.*

@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val enemyCmps: ComponentMapper<EnemyComponent>,
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    @Qualifier("gameStage") private val gameStage: Stage
) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]
        if (deadCmp.reviveTime == 0f) {
            gameStage.fire(EntityDeathEvent(animationCmps[entity].model))
            if (entity in enemyCmps) {
                gameStage.fire(EnemyDeathEvent(experienceCmps[entity]))
            }
            world.remove(entity)
            return
        }

        deadCmp.reviveTime -= deltaTime
        if (deadCmp.reviveTime <= 0f) {
            with(lifeCmps[entity]) { life = max }
            configureEntity(entity) { deadCmps.remove(entity) }
        }
    }
}