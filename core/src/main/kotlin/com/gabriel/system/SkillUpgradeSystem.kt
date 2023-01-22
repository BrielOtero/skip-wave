package com.gabriel.system

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.gabriel.component.SkillComponent
import com.gabriel.ui.model.SkillModel
import com.github.quillraven.fleks.IntervalSystem

class SkillUpgradeSystem : IntervalSystem(), EventListener {
    private val skillsModel = mutableMapOf<String, SkillComponent>()

    init {
        skillsModel.getOrPut(,)
    }

    override fun onTick() {
        TODO("Not yet implemented")
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is EntityLevelEvent -> {

            }
            else ->{}
        }
        return false
    }
}

