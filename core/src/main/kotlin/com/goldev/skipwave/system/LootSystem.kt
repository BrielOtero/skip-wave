package com.goldev.skipwave.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.component.AnimationComponent
import com.goldev.skipwave.component.LootComponent
import com.goldev.skipwave.event.EntityLootEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.*

/**
 * System that takes care of the loot in the game.
 *
 * @property lootCmps Entities with LootComponent in the world.
 * @property animationCmps Entities with AnimationComponent in the world.
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Loot system
 */
@AllOf([LootComponent::class])
class LootSystem(
    private val lootCmps: ComponentMapper<LootComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : IteratingSystem() {

    /**
     * If the entity has a loot component, and the loot component has an interact entity, then fire a
     * loot event, remove the loot component, and play the open animation
     *
     * @param entity The entity that is being ticked.
     */
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