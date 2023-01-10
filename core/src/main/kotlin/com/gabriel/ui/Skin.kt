package com.gabriel.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.set
import ktx.style.skin
import ktx.style.touchpad

enum class Drawables(
    val atlasKey: String,
) {
//    CHAR_INFO_BGD("char_info"),
    LIFE_BAR("life_progress"),
    LIFE_UNDER("life_under"),
    FRAME_BGD("frame_bgd"),
    FRAME_FGD("frame_fgd"),
    JOY_EXT("joy_ext"),
    JOY_INT("joy_int"),
}

operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)

enum class Labels {
    FRAME;

    val skinKey = this.name.lowercase()
}

enum class Fonts(
    val atlasRegionKey: String,
    val scaling: Float,
) {
    DEFAULT("font", 0.25f);

    val skinKey = "Font_${this.name.lowercase()}"
    val fontPath = "ui/${this.atlasRegionKey}.fnt"
}

operator fun Skin.get(font: Fonts): BitmapFont = this.getFont(font.skinKey)


fun loadSkin() {
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui_new.atlas")) { skin ->
        Fonts.values().forEach { fnt ->
            skin[fnt.skinKey] = BitmapFont(Gdx.files.internal(fnt.fontPath), skin.getRegion(fnt.atlasRegionKey)).apply {
                data.setScale(fnt.scaling)
                data.markupEnabled = true
            }
        }

        touchpad {
            knob = skin[Drawables.JOY_INT]
            knob.minWidth=30f
            knob.minHeight=30f
            background = skin[Drawables.JOY_EXT]
        }

        label(Labels.FRAME.skinKey) {
            font = skin[Fonts.DEFAULT]
            background = skin[Drawables.FRAME_FGD].apply {
                leftWidth = 2f
                rightWidth = 2f
                topHeight = 1f
            }
        }
    }
}

fun disposeSkin() {
    Scene2DSkin.defaultSkin.disposeSafely()
}