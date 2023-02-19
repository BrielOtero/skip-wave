package com.goldev.skipwave.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable

/* It's an image that can be flipped horizontally */
class FlipImage : Image() {
    /* A variable that is used to flip the image. */
    var flipX = false

    /**
     * It draws the image to the screen. Flip the image if necessary
     *
     * @param batch Batch - The batch to draw the image with
     * @param parentAlpha The parent's alpha value.
     */
    override fun draw(batch: Batch, parentAlpha: Float) {
        validate()
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha)

        val toDraw = drawable
        if (toDraw is TransformDrawable && (scaleX != -1f || scaleY != 1f || rotation != 0f)) {
            toDraw.draw(
                batch,
                if (flipX) x + imageX + imageWidth * scaleX else x + imageX,
                y + imageY,
                originX - imageX,
                originY - imageY,
                imageWidth,
                imageHeight,
                if (flipX) -scaleX else scaleX,
                scaleY,
                rotation
            )
        } else {
            toDraw?.draw(
                batch,
                if (flipX) x + imageX + imageWidth * scaleX else x + imageX,
                y + imageY,
                if (flipX) -imageWidth * scaleX else imageWidth * scaleX,
                imageHeight * scaleY,
            )
        }
    }
}