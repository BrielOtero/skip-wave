package com.goldev.skipwave.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.AnimationComponent
import com.goldev.skipwave.component.LootComponent
import com.goldev.skipwave.event.EntityLootEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*

@AllOf([LootComponent::class])
class LootSystem(
    private val lootCmps: ComponentMapper<LootComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        with(lootCmps[entity]) {
            if (interactEntity == null) {
                return
            }

            gameStage.fire(EntityLootEvent(animationCmps[entity].model))
            configureEntity(entity) { lootCmps.remove(it) }
            animationCmps.getOrNull(entity)?.let { aniCmp ->
                aniCmp.nextAnimation(com.goldev.skipwave.component.AnimationType.OPEN)
                aniCmp.playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}