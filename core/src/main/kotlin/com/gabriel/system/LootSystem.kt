package com.gabriel.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.component.AnimationComponent
import com.gabriel.component.AnimationType
import com.gabriel.component.LootComponent
import com.gabriel.event.EntityLootEvent
import com.gabriel.event.fire
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
                aniCmp.nextAnimation(AnimationType.OPEN)
                aniCmp.playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}