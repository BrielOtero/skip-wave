package com.gabriel.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.gabriel.component.*
import com.gabriel.event.EntityDeathEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.*
import ktx.assets.disposeSafely

@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    private val lifeCmps: ComponentMapper<LifeComponent>,
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,
) : IteratingSystem() {
    private val damageFont = BitmapFont(Gdx.files.internal("damage.fnt")).apply { data.setScale(0.33f) }
    private val floatingTextStyle = LabelStyle(damageFont, Color.WHITE)

    override fun onTickEntity(entity: Entity) {
        val lifeCmp = lifeCmps[entity]
        lifeCmp.life = (lifeCmp.life + lifeCmp.regeneration * deltaTime).coerceAtMost(lifeCmp.max)

        if (lifeCmp.takeDamage > 0f) {
            val physicCmp = physicCmps[entity]
            lifeCmp.life -= lifeCmp.takeDamage
            floatingText(lifeCmp.takeDamage.toInt().toString(), physicCmp.body.position, physicCmp.size)
            lifeCmp.takeDamage = 0f
        }

        if (lifeCmp.isDead) {
            gameStage.fire(EntityDeathEvent(animationCmps[entity].model))
            animationCmps.getOrNull(entity)?.let { aniCmp ->
                aniCmp.nextAnimation(AnimationType.DEATH)
                aniCmp.playMode = Animation.PlayMode.NORMAL
            }

            configureEntity(entity) {
                deadCmps.add(it) {
                    if (it in playerCmps) {
                        // revive player after 7 seconds
                        reviveTime = 7f
                    }
                }
            }
        }
    }

    private fun floatingText(text: String, entityPosition: Vector2, entitySize: Vector2) {
        world.entity {
            add<FloatingTextComponent> {
                txtLocation.set(entityPosition.x, entityPosition.y - entitySize.y * 0.5f)
                lifeSpan = 1.5f
                label = Label(text, floatingTextStyle)
            }
        }
    }

    override fun onDispose() {
        damageFont.disposeSafely()
    }
}