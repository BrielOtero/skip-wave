package com.goldev.skipwave.component

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.ai.AiEntity
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

data class AiComponent(
    val nearbyEntitites: MutableSet<Entity> = mutableSetOf(),
    var treePath: String = "",
) {
    lateinit var behaviorTree: BehaviorTree<AiEntity>
    var target: Entity = NO_TARGET

    companion object {
        val NO_TARGET = Entity(-1)

        class AiComponentListener(
            private val world: World,
            @Qualifier("gameStage") private val gameStage: Stage,
        ) : ComponentListener<AiComponent> {

            private val treeParser = BehaviorTreeParser<AiEntity>()

            override fun onComponentAdded(entity: Entity, component: AiComponent) {
                component.behaviorTree = treeParser.parse(
                    Gdx.files.internal(component.treePath),
                    AiEntity(entity, world, gameStage)
                )
            }

            override fun onComponentRemoved(entity: Entity, component: AiComponent) = Unit
        }
    }
}