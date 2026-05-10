package com.goldev.skipwave.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.goldev.skipwave.component.*
import com.goldev.skipwave.event.EntityDamageEvent
import com.goldev.skipwave.event.EntityDeathEvent
import com.goldev.skipwave.event.PlayerDeathEvent
import com.goldev.skipwave.event.fire
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.assets.disposeSafely

/**
 * System that takes care of the life in the game.
 *
 * @property gameStage The stage that the game is being rendered on.
 * @constructor Create empty Life system
 */
class LifeSystem(
    private val gameStage: Stage = inject("gameStage"),
) : IteratingSystem(
    family = family { all(LifeComponent).none(DeadComponent) }
) {
    /**
     *  The font for floating text.
     */
    private val damageFont =
        BitmapFont(Gdx.files.internal("ui/thaleah_fat.fnt")).apply { data.setScale(0.33f) }

    /**
     *  Label style for the floating text.
     */
    private val floatingTextStyle = LabelStyle(damageFont, Color.WHITE)

    /**
     *  Label style for the floating text when is player.
     */
    private val floatingTextStylePlayer = LabelStyle(damageFont, Color.RED)

    /**
     * If the entity is dead, then fire a PlayerDeathEvent and set the revive time to 7 seconds
     *
     * @param entity The entity that is being updated.
     */
    override fun onTickEntity(entity: Entity) {
        val lifeCmp = entity[LifeComponent]
        lifeCmp.life = (lifeCmp.life + lifeCmp.regeneration * deltaTime).coerceAtMost(lifeCmp.max)
        gameStage.fire(EntityDamageEvent(entity))

        if (lifeCmp.takeDamage > 0f) {
            val physicCmp = entity[PhysicComponent]
            lifeCmp.life -= lifeCmp.takeDamage
            gameStage.fire(EntityDamageEvent(entity))
            floatingText(
                entity,
                lifeCmp.takeDamage.toInt().toString(),
                physicCmp.body.position,
                physicCmp.size
            )
            lifeCmp.takeDamage = 0f
        }

        if (lifeCmp.isDead) {
            gameStage.fire(EntityDeathEvent(entity[AnimationComponent].model))
            entity.getOrNull(AnimationComponent)?.let { aniCmp ->
                aniCmp.nextAnimation(AnimationType.DEATH)
                aniCmp.playMode = Animation.PlayMode.NORMAL
            }

            entity.configure {
                it += DeadComponent().apply {
                    if (entity has PlayerComponent) {
                        // revive player after 7 seconds
                        reviveTime = 700f
                        gameStage.fire(PlayerDeathEvent(entity))
                    }
                }
            }
        }
    }

    /**
     * Create a new entity with a FloatingTextComponent and a Label component, and set the text and
     * style of the label.
     *
     * @param entity The entity that is being damaged.
     * @param text The text to display.
     * @param entityPosition The position of the entity that is being damaged.
     * @param entitySize The size of the entity that is being damaged.
     */
    private fun floatingText(
        entity: Entity,
        text: String,
        entityPosition: Vector2,
        entitySize: Vector2
    ) {
        world.entity { newEntity ->
            val style = if (entity has PlayerComponent) floatingTextStylePlayer else floatingTextStyle

            newEntity += FloatingTextComponent().apply {
                txtLocation.set(entityPosition.x, entityPosition.y - entitySize.y * 0.5f)
                lifeSpan = 1.5f
                label = Label(text, style)
            }
        }
    }

    /**
     * When the game is closed, dispose LifeSystem resources.
     */
    override fun onDispose() {
        damageFont.disposeSafely()
    }
}
