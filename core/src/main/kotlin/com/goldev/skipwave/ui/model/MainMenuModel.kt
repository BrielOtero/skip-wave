package com.goldev.skipwave.ui.model

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import ktx.log.logger

/**
 * The model of the Main Menu
 *
 * @property bundle The bundle with text to show in the UI.
 * @property gameStage The stage that the game is being rendered on.
 * @property uiStage The stage that the UI is being rendered on.
 * @constructor Create empty Main menu model
 */
class MainMenuModel(
    val bundle: I18NBundle,
    val gameStage: Stage,
    val uiStage: Stage,
) : PropertyChangeSource(){

    companion object {
        /**
         *  It's a logger that logs the class.
         */
        private val log = logger<SkillUpgradeModel>()
    }
}

