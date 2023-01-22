package com.gabriel.ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

class SkillUpgradeModel(
    world: World,
    @Qualifier("gameStage") val gameStage: Stage,
) :PropertyChangeSource(),EventListener{

    override fun handle(event: Event): Boolean {
        TODO("Not yet implemented")
    }
}