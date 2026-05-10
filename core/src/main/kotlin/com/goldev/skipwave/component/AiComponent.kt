package com.goldev.skipwave.component

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.ai.AiEntity
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

/**
 * Ai Component holds a reference to a behavior tree, and a set of nearby entities.
 *
 * @property nearbyEntitites A set of entities that are nearby the entity that has this component.
 * @property treePath The path to the behavior tree file.
 * @constructor Creates an AiComponent
 */
data class AiComponent(
    val nearbyEntitites: MutableSet<Entity> = mutableSetOf(),
    var treePath: String = "",
) : Component<AiComponent> {
    /**
     * It is a variable that is not initialized with Behavior Tree of AiEntity.
     */
    lateinit var behaviorTree: BehaviorTree<AiEntity>

    /**
     *  It is a variable with the target
     */
    var target: Entity = NO_TARGET

    /**
     * When this component is added to an entity, the behavior tree file is parsed and the result is
     * stored in the component.
     *
     * @param entity The entity that the component was added to.
     */
    override fun World.onAdd(entity: Entity) {
        val gameStage = inject<Stage>("gameStage")
        behaviorTree = treeParser.parse(
            Gdx.files.internal(treePath),
            AiEntity(entity, this, gameStage)
        )
    }

    override fun type() = AiComponent

    companion object : ComponentType<AiComponent>() {
        val NO_TARGET = Entity.NONE

        /**
         *  Creating a BehaviorTreeParser object.
         */
        private val treeParser = BehaviorTreeParser<AiEntity>()
    }
}
