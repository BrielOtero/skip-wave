package com.goldev.skipwave.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

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

    val atlasKey: String = this.toString().lowercase()
}

enum class AnimationType {
    IDLE, RUN, ATTACK, DEATH, OPEN;

    val atlasKey: String = this.toString().lowercase()
}

data class AnimationComponent(
    var model: AnimationModel = AnimationModel.UNDEFINED,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: String = ""

    val isAnimationDone: Boolean
        get() = animation.isAnimationFinished(stateTime)

    fun nextAnimation(model: AnimationModel, type: AnimationType) {
        this.model = model
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    fun nextAnimation(type: AnimationType) {
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    companion object {
        val NO_ANIMATION = ""
    }
}