package com.goldev.skipwave.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.actors.FlipImage
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

/**
 *  It's a component that holds a reference to a Image of the entity
 */
class ImageComponent : Component<ImageComponent>, Comparable<ImageComponent> {
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

    /**
     * When this component is added to an entity, the image is attached to the game stage.
     *
     * @param entity The entity that the component was added to.
     */
    override fun World.onAdd(entity: Entity) {
        val gameStage = inject<Stage>("gameStage")
        gameStage.addActor(image)
    }

    /**
     * When this component is removed from an entity, the image is detached from the game stage.
     *
     * @param entity The entity that the component was removed from.
     */
    override fun World.onRemove(entity: Entity) {
        val gameStage = inject<Stage>("gameStage")
        gameStage.root.removeActor(image)
    }

    override fun type() = ImageComponent

    companion object : ComponentType<ImageComponent>()
}
