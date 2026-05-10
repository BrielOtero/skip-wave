package com.goldev.skipwave.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.AnimationComponent
import com.goldev.skipwave.component.LootComponent
import com.goldev.skipwave.event.EntityLootEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject

/**
 * System that takes care of the loot in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Loot system
 */
class LootSystem(
    private val gameStage: Stage = inject("gameStage"),
) : IteratingSystem(
    family = family { all(LootComponent) }
) {

    /**
     * If the entity has a loot component, and the loot component has an interact entity, then fire a
     * loot event, remove the loot component, and play the open animation
     *
     * @param entity The entity that is being ticked.
     */
    override fun onTickEntity(entity: Entity) {
        with(entity[LootComponent]) {
            if (interactEntity == null) {
                return
            }

            gameStage.fire(EntityLootEvent(entity[AnimationComponent].model))
            entity.configure { it -= LootComponent }
            entity.getOrNull(AnimationComponent)?.let { aniCmp ->
                aniCmp.nextAnimation(com.goldev.skipwave.component.AnimationType.OPEN)
                aniCmp.playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}
