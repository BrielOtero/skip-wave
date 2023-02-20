package com.goldev.skipwave.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.actors.FlipImage
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier

/**
 *  It's a component that holds a reference to a Image of the entity
 */
class ImageComponent : Comparable<ImageComponent> {
    /**
     *  It's a variable that is not initialized yet that contains the image.
     */
    lateinit var image: FlipImage

    /**
     * If the y values are different, return the result of comparing the y values. Otherwise, return
     * the result of comparing the x values
     *
     * @param other The other ImageComponent to compare to.
     * @return The difference between the y values of the two images.
     */
    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }

    companion object {
        /**
         * When an ImageComponent is added to an entity, add the image to the game stage.
         *
         *  @property  gameStage The stage that the game is being rendered on.
         *  @constructor Creates an ImageComponentListener
         */
        class ImageComponentListener(
            @Qualifier("gameStage") private val gameStage: Stage
        ) : ComponentListener<ImageComponent> {

            /**
             * When a component is added to an entity, add the image to the game stage.
             *
             * @param entity The entity that the component was added to.
             * @param component The component that was added to the entity.
             */
            override fun onComponentAdded(entity: Entity, component: ImageComponent) {
                gameStage.addActor(component.image)
            }

            /**
             * When an entity is removed from the engine, remove the image from the stage.
             *
             * @param entity The entity that the component was added to.
             * @param component The component that was added to the entity
             */
            override fun onComponentRemoved(entity: Entity, component: ImageComponent) {
                gameStage.root.removeActor(component.image)
            }

        }
    }
}