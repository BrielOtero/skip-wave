package com.goldev.skipwave.screen.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.goldev.skipwave.SkipWave
import com.goldev.skipwave.ui.model.SkillModel
import com.goldev.skipwave.ui.model.SkillUpgradeModel
import com.goldev.skipwave.ui.model.Skills
import com.goldev.skipwave.ui.view.SkillUpgradeView
import com.goldev.skipwave.ui.view.skillUpgradeView
import com.goldev.skipwave.system.SkillUpgradeSystem.*
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import com.goldev.skipwave.component.*
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

/**
 *  It's a screen for test SkillUpgradeView.
 *
 *  @property game The property with game data.
 *  @constructor Creates SkillUpgradeScreen
 */
class SkillUpgradeScreen(private val game: SkipWave) : KtxScreen {
    /**
     *  It's a property with stage that the game is being rendered on.
     */
    private val gameStage: Stage = Stage(ExtendViewport(8f, 16f))

    /**
     *  It's a property with stage that the UI is being rendered on.
     */
    private val uiStage: Stage = Stage(ExtendViewport(180f, 320f))

    /**
     *  Property with world for entities.
     */
    private val eWorld = world { }

    /**
     *  Property with model for the SkillUpgradeModel.
     */
    private val model = SkillUpgradeModel( game.bundle, gameStage, uiStage)

    /**
     *  It's a property that contains a SkillUpgradeView for the game.
     */
    private lateinit var skillUpgradeView: SkillUpgradeView

    /**
     *  Property that contains player entity.
     */
    private val playerEntity: Entity

    init {
        playerEntity = eWorld.entity {
            add<PlayerComponent>()
            add<LifeComponent> {
                max = 5f
                life = 3f
            }
            add<ExperienceComponent>()
            add<WaveComponent>()
            add<AttackComponent>()
            add<MoveComponent>()
        }
    }

    /**
     * When the screen is resized, update the viewport to the new width and height.
     *
     * @param width The width of the screen in pixels.
     * @param height The height of the screen in pixels.
     */
    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
    }

    /**
     * This function is called when this SkillUpgradeScreen appears.
     */
    override fun show() {
        uiStage.clear()
        uiStage.addListener(model)
        uiStage.actors {
            skillUpgradeView = skillUpgradeView(model)
        }
        uiStage.isDebugAll = true
    }

    /**
     * The render function of the screen. It is called every frame.
     *
     * @param delta The time in seconds since the last frame.
     */
    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            skillUpgradeView.skill(SkillModel(-1, 0, "frame_fgd", "nothing", 0, 0f))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            skillUpgradeView.skill(SkillModel(-1, 0, "frame_fgd", "nothing", 0, 0f))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            skillUpgradeView.popupSkills(
                Skills(
                    Skill.PLAYER_COOLDOWN,
                    Skill.PLAYER_LIFE,
                    Skill.PLAYER_DAMAGE
                )
            )
        }

        uiStage.act()
        uiStage.draw()
    }
    /**
     * It disposes all resources when SkillUpgradeScreen is closed.
     */
    override fun dispose() {
        uiStage.disposeSafely()
    }

}