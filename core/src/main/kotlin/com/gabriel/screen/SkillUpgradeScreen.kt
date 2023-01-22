package com.gabriel.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gabriel.component.*
import com.gabriel.ui.model.SkillModel
import com.gabriel.ui.model.SkillUpgradeModel
import com.gabriel.ui.view.SkillUpgradeView
import com.gabriel.ui.view.skillUpgradeView
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.scene2d.actors

class SkillUpgradeScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(320f, 180f))
    private val eWorld = world { }
    private val model = SkillUpgradeModel(eWorld, stage)
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
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        stage.clear()
        stage.addListener(model)
        stage.actors {
            skillUpgradeView = skillUpgradeView(model)
        }
        stage.isDebugAll = true
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            hide()
            show()
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            skillUpgradeView.skill(SkillModel(-1, "frame_fgd", 0))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            skillUpgradeView.skill(SkillModel(-1, "frame_bgd", 0))
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
        }



        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.disposeSafely()
    }

}