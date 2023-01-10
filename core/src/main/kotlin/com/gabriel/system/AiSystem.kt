package com.gabriel.system

import com.gabriel.component.AiComponent
import com.gabriel.component.DeadComponent
import com.github.quillraven.fleks.*

@AllOf([AiComponent::class])
@NoneOf([DeadComponent::class])
class AiSystem(
    private val aiCmps: ComponentMapper<AiComponent>,
) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        with(aiCmps[entity]) {
            behaviorTree.step()
        }
    }
}