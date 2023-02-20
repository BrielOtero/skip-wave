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
) {
    /**
     * It is a variable that is not initialized with Behavior Tree of AiEntity.
     */
    lateinit var behaviorTree: BehaviorTree<AiEntity>

    /**
     *  It is a variable with the target
     */
    var target: Entity = NO_TARGET

    companion object {
        val NO_TARGET = Entity(-1)

        /**
         *  It listens for the addition of an AiComponent to an Entity, and when it finds one,
         *  it parses the behavior tree file and stores the result in the AiComponent.
         *
         *  @property world The world that the entity belongs to.
         *  @property  gameStage The stage that the game is being rendered on.
         *  @constructor Creates an AiComponentListener
         */
        class AiComponentListener(
            private val world: World,
            @Qualifier("gameStage") private val gameStage: Stage,
        ) : ComponentListener<AiComponent> {

            /**
             *  Creating a BehaviorTreeParser object.
             */
            private val treeParser = BehaviorTreeParser<AiEntity>()

            /**
             * When a component is added to an entity, parse the behavior tree file and assign it to
             * the component
             *
             * @param entity The entity that the component was added to.
             * @param component The component that was added to the entity
             */
            override fun onComponentAdded(entity: Entity, component: AiComponent) {
                component.behaviorTree = treeParser.parse(
                    Gdx.files.internal(component.treePath),
                    AiEntity(entity, world, gameStage)
                )
            }

            /**
             * onComponentRemoved is called when a component is removed from an entity
             *
             * @param entity The entity that the component was removed from.
             * @param component The component that was removed from the entity.
             */
            override fun onComponentRemoved(entity: Entity, component: AiComponent) = Unit
        }
    }
}