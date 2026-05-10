package com.goldev.skipwave.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.goldev.skipwave.component.AnimationComponent
import com.goldev.skipwave.component.AnimationComponent.Companion.NO_ANIMATION
import com.goldev.skipwave.component.ImageComponent
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger

/**
 * System that takes care of the animations in the game.
 *
 * @property textureAtlas The texture atlas with the animation.
 * @constructor Create empty Animation system
 */
class AnimationSystem(
    private val textureAtlas: TextureAtlas = inject(),
) : IteratingSystem(
    family = family { all(AnimationComponent, ImageComponent) }
) {

    /**
     *  A map variable with the animations.
     */
    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    /**
     * If the entity has a next animation, set it to the current animation, reset the state time, and
     * set the next animation to NO_ANIMATION. Otherwise, just increment the state time
     *
     * @param entity The entity that the component is attached to.
     */
    override fun onTickEntity(entity: Entity) {
        val aniCmp = entity[AnimationComponent]

        if (aniCmp.nextAnimation == NO_ANIMATION) {
            aniCmp.stateTime += deltaTime

        } else {
            aniCmp.animation = animation(aniCmp.nextAnimation)
            aniCmp.stateTime = 0f
            aniCmp.nextAnimation = NO_ANIMATION
        }

        aniCmp.animation.playMode = aniCmp.playMode
        entity[ImageComponent].image.drawable = aniCmp.animation.getKeyFrame(aniCmp.stateTime)
    }

    /**
     * It returns an animation from the cache or creates a new one if it doesn't exist.
     *
     * @param aniKeyPath The name of the animation.
     * @return  An animation loaded in the atlas texture.
     */
    private fun animation(aniKeyPath: String): Animation<TextureRegionDrawable> {
        return cachedAnimations.getOrPut(aniKeyPath) {
            log.debug { "New animation is created for '$aniKeyPath'" }
            val regions = textureAtlas.findRegions(aniKeyPath)

            if (regions.isEmpty) {
                gdxError("There aren't texture regions for $aniKeyPath")
            }

            Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) })
        }
    }

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<AnimationSystem>()

        /**
         *  It's the default frame duration for the animations.
         */
        private const val DEFAULT_FRAME_DURATION = 1 / 8f
    }
}
