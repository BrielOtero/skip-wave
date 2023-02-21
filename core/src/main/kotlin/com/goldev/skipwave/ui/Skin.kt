package com.goldev.skipwave.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.assets.disposeSafely
import ktx.freetype.generateFont
import ktx.scene2d.Scene2DSkin
import ktx.style.*

/**
 * Enumeration with all the UI images
 *
 * @property atlasKey Texture atlas key value
 * @constructor Create empty Drawables
 */
enum class Drawables(
    val atlasKey: String,
) {
    //UI
    PLAYER("player"),

    LIFE_BAR("life_progress"),
    LIFE_UNDER("life_under"),
    EXPERIENCE_BAR("experience_progress"),
    EXPERIENCE_UNDER("experience_under"),

    FRAME_BGD("frame_bgd"),
    FRAME_BGD_LIGHT("frame_bgd_light"),
    FRAME_FGD("frame_fgd"),
    FRAME_FGD_LIGHT("frame_fgd_light"),
    FRAME_FGD_DARK("frame_fgd_dark"),
    FRAME_PURPLE("frame_purple"),


    JOY_EXT("joy_ext"),
    JOY_INT("joy_int"),

    BTN_UP("btn_up"),
    BTN_DOWN("btn_down"),
    BTN_PAUSE("btn_pause"),
    SCROLL_VERTICAl("scroll_vertical"),
    BTN_SLIDER_LEFT_UP("btn_slider_left_up"),
    BTN_SLIDER_LEFT_DOWN("btn_slider_left_down"),
    BTN_SLIDER_RIGHT_UP("btn_slider_right_up"),
    BTN_SLIDER_RIGHT_DOWN("btn_slider_right_down"),


    //SKILLS
    PLAYER_LIFE("player_life"),
    PLAYER_REGENERATION("player_regeneration"),
    PLAYER_SPEED("player_speed"),
    PLAYER_COOLDOWN("player_cooldown"),
    PLAYER_DAMAGE("player_damage"),
}

/**
 * Get the texture atlas of the drawable.
 *
 * @param drawable The drawable to transform.
 * @return The texture atlas of the drawable.
 */
operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)

/**
 * Enumeration with all the Labels
 *
 * @constructor Create empty Labels
 */
enum class Labels {
    FRAME;

    /**
     *  It's a property with the key of texture atlas
     *
     *  @return The key of the texture atlas.
     */
    val skinKey = this.name.lowercase()
}

/**
 * Enumeration with all the TextButtons
 *
 * @constructor Create empty Text buttons
 */
enum class TextButtons {
    DEFAULT, TITLE;

    /**
     *  It's a property with the key of texture atlas
     *
     *  @return The key of the texture atlas.
     */
    val skinKey = this.name.lowercase()
}

/**
 * Enumeration with all the Buttons
 *
 * @constructor Create empty Buttons
 */
enum class Buttons {
    LEFT, RIGHT, PAUSE;

    /**
     *  It's a property with the key of texture atlas
     *
     *  @return The key of the texture atlas.
     */
    val skinKey = this.name.lowercase()
}

/**
 * Load the skin
 */
fun loadSkin() {
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui_new.atlas")) { skin ->
        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("ui/clarity.ttf"))
        val loadedFont = fontGenerator.generateFont {
            size = 32
            characters = FreeTypeFontGenerator.DEFAULT_CHARS
        }
        fontGenerator.disposeSafely()

        val defaultFont = loadedFont.apply { data.setScale(0.28f) }

        touchpad {
            knob = skin[Drawables.JOY_INT]
            knob.minWidth = 30f
            knob.minHeight = 30f
            background = skin[Drawables.JOY_EXT]
        }

        label(Labels.FRAME.skinKey) {
            font = defaultFont
        }

        textButton(TextButtons.DEFAULT.skinKey) {
            font = defaultFont
            up = skin[Drawables.BTN_UP]
            down = skin[Drawables.BTN_DOWN]
        }
        textButton(TextButtons.TITLE.skinKey) {
            font = defaultFont
            up = skin[Drawables.FRAME_FGD_LIGHT]
        }

        button(Buttons.LEFT.skinKey) {
            up = skin[Drawables.BTN_SLIDER_LEFT_UP]
            down = skin[Drawables.BTN_SLIDER_LEFT_DOWN]
        }
        button(Buttons.RIGHT.skinKey) {
            up = skin[Drawables.BTN_SLIDER_RIGHT_UP]
            down = skin[Drawables.BTN_SLIDER_RIGHT_DOWN]
        }
        button(Buttons.PAUSE.skinKey) {
            up = skin[Drawables.BTN_PAUSE]
        }
        scrollPane {
            vScrollKnob = skin[Drawables.SCROLL_VERTICAl]
        }
    }
}

/**
 * Dispose the skin
 */
fun disposeSkin() {
    Scene2DSkin.defaultSkin.disposeSafely()
}