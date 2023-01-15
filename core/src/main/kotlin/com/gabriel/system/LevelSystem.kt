package com.gabriel.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.gabriel.component.ExperienceComponent
import com.gabriel.component.LevelComponent
import com.gabriel.component.LifeComponent
import com.gabriel.component.PlayerComponent
import com.gabriel.event.EnemyDeathEvent
import com.gabriel.event.EntityDeathEvent
import com.github.quillraven.fleks.*
import ktx.log.logger

@AllOf([LevelComponent::class])
class LevelSystem(
    private val experienceCmps: ComponentMapper<ExperienceComponent>,
    private val levelCmps: ComponentMapper<LevelComponent>,

    ) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        with(experienceCmps[entity]) {
        log.debug { "Level ${levelCmps[entity].level} Experience ${experience} Experience To Next Level ${experienceToNextLevel}" }
            if (experience >= experienceToNextLevel) {
                with(levelCmps[entity]) {
                    level += 1

                    log.debug { "ExperienceToNextLevel before ${experienceToNextLevel}" }

                    experienceToNextLevel =
                        ((50f * (Math.pow(((level + 1).toDouble()), 2.0) - (5 * (level + 1)) + 8)).toFloat())

                    log.debug { "ExperienceToNextLevel after ${experienceToNextLevel}" }
                }
            }
        }
    }

    companion object {
        private val log = logger<LevelSystem>()
    }



}