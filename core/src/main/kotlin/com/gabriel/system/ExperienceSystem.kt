package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.gabriel.component.ExperienceComponent
import com.gabriel.component.LevelComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EnemyDeathEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger

@AllOf([ExperienceComponent::class])
class ExperienceSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
) : EventListener, IteratingSystem() {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    override fun onTickEntity(entity: Entity) {
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is EnemyDeathEvent -> {
                //Adds experience to player when an enemy has died
                log.debug { "Experience before ${experienceCmps[playerEntities.first()].experience}" }

                experienceCmps[playerEntities.first()].experience += event.experienceCmp.dropExperience

                log.debug { "Experience after ${experienceCmps[playerEntities.first()].experience}" }
            }

            else -> return false
        }
        return true
    }

    companion object {
        private val log = logger<ExperienceSystem>()
    }


}