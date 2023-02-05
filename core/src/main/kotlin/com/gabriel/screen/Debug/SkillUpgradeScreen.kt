package com.gabriel.screen.Debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.*
import com.gabriel.ui.model.SkillModel
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.model.Skills
import com.gabriel.ui.view.SkillUpgradeView
import com.gabriel.ui.view.skillUpgradeView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

class SkillUpgradeScreen : KtxScreen {
    private val uiStage: Stage = Stage(ExtendViewport(180f, 320f))
    private val gameStage: Stage = Stage(ExtendViewport(8f, 16f))
    private val eWorld = world { }
    private val model = SkillUpgradeModel(eWorld,gameStage, uiStage)
    private lateinit var skillUpgradeView: SkillUpgradeView
    private val playerEntity: Entity

    init {
        playerEntity = eWorld.entity {
            add<PlayerComponent>()
            add<LifeComponent> {
                max = 5f
                life = 3f
            }
            add<ExperienceComponent>()
            add<LevelComponent>()
            add<AttackComponent>()
            add<MoveComponent>()
        }
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
    }

    override fun show() {
        uiStage.clear()
        uiStage.addListener(model)
        uiStage.actors {
            skillUpgradeView = skillUpgradeView(model)
        }
        uiStage.isDebugAll = true
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            skillUpgradeView.skill(SkillModel(-1,0, "frame_fgd","nothing",0, 0f))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            skillUpgradeView.skill(SkillModel(-1,0, "frame_fgd","nothing",0, 0f))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            skillUpgradeView.popup(Skills(Skill.PLAYER_COOLDOWN, Skill.PLAYER_LIFE, Skill.PLAYER_DAMAGE))
        }



        uiStage.act()
        uiStage.draw()
    }

    override fun dispose() {
        uiStage.disposeSafely()
    }

}