package com.goldev.skipwave.system

import com.goldev.skipwave.component.AiComponent
import com.goldev.skipwave.component.DeadComponent
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