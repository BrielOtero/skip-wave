package com.goldev.skipwave.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

/**
 *  It's an enum class that contains all the possible animation models that can be used in the game
 */
enum class AnimationModel {
    UNDEFINED,
    PLAYER,
    CHEST,
    SLASH_RIGHT,
    SLASH_LEFT,

    //ENEMIES
    AXOLOT,
    AXOLOT_SHINY,
    BAMBOO,
    BAMBOO_SHINY,
    BUTTERFLY,
    BUTTERFLY_SHINY,
    CYCLOPE,
    CYCLOPE_SHINY,
    DRAGON,
    DRAGON_SHINY,
    FISH,
    FISH_SHINY,
    FLAM,
    FLAM_SHINY,
    MOUSE,
    MOUSE_SHINY,
    OCTOPUS,
    OCTOPUS_SHINY,
    RACOON,
    RACOON_SHINY,
    SKULL,
    SKULL_SHINY,
    SPIRIT,
    SPIRIT_SHINY;

    /**
     *  It's a property with the key of texture atlas
     *
     *  @return The key of the texture atlas.
     */
    val atlasKey: String = this.toString().lowercase()
}

/**
 *  It's an enum class that contains all the possible animation types that can be used in the game
 */
enum class AnimationType {
    IDLE, RUN, ATTACK, DEATH, OPEN;

    /**
     *  It's a property with the key of texture atlas
     *
     *  @return The key of the texture atlas.
     */
    val atlasKey: String = this.toString().lowercase()
}

/**
 * AnimationComponent holds the current animation, the time the animation has been playing, and the play mode of the
 * animation. It also has a nextAnimation property that is used to queue up the next animation to play.
 *
 * @property  model The model of the animation.
 * @property  stateTime This is the time that the animation has been running for.
 * @property  playMode The play mode of the animation.
 * @constructor Creates an AnimationComponent with default values
 */
data class AnimationComponent(
    var model: AnimationModel = AnimationModel.UNDEFINED,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    /**
     *  It's a lateinit variable that is used to store the animation.
     */
    lateinit var animation: Animation<TextureRegionDrawable>

    /**
     *  It's a variable that is used to store the next animation to be played.
     */
    var nextAnimation: String = ""

    /**
     *  It's a property that returns if the animation is done.
     *
     *  @return True if the animation is finished.
     */
    val isAnimationDone: Boolean
        get() = animation.isAnimationFinished(stateTime)

    /**
     * If the model is the same, then just change the animation type.
     *
     * @param model The model that the animation is for.
     * @param type This is the type of animation you want to play.
     */
    fun nextAnimation(model: AnimationModel, type: AnimationType) {
        this.model = model
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    /**
     * If the current animation is not the same as the next animation, then set the current animation
     * to the next animation.
     *
     * @param type The type of animation to play.
     */
    fun nextAnimation(type: AnimationType) {
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    companion object {
        /**
         *  It's a constant that is used to store the value of no animation.
         */
        const val NO_ANIMATION = ""
    }
}