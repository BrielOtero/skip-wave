package com.goldev.skipwave.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.goldev.skipwave.actors.FlipImage
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier

class ImageComponent : Comparable<ImageComponent> {
    lateinit var image: FlipImage

    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }

    companion object {
        class ImageComponentListener(
            @Qualifier("gameStage") private val gameStage: Stage
        ) : ComponentListener<ImageComponent> {

            override fun onComponentAdded(entity: Entity, component: ImageComponent) {
                gameStage.addActor(component.image)
            }

            override fun onComponentRemoved(entity: Entity, component: ImageComponent) {
                gameStage.root.removeActor(component.image)
            }

        }
    }
}