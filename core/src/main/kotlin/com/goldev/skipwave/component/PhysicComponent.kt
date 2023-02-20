package com.goldev.skipwave.component

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Shape2D
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.goldev.skipwave.SkipWave.Companion.UNIT_SCALE
import com.goldev.skipwave.system.CollisionSpawnSystem.Companion.SPAWN_AREA_SIZE
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateCfg
import ktx.app.gdxError
import ktx.box2d.*
import ktx.math.vec2

/**
 *  It's a component that holds a reference to a Box2D body
 */
class PhysicComponent {
    /**
     *  It's a vector that holds the previous position of the body.
     */
    val prevPos = vec2()

    /**
     *  It's a vector that holds the impulse that will be applied to the body.
     */
    val impulse = vec2()

    /**
     *  It's a vector that holds the offset of the body.
     */
    val offset = vec2()

    /**
     *  It's a vector that holds the size of the body.
     */
    val size = vec2()

    /**
     *  It's a variable that holds a reference to a Box2D body.
     */
    lateinit var body: Body

    companion object {
        /**
         *  It's a vector that holds the offset of the body.
         */
        private val TMP_VEC = vec2()

        /**
         *  It's a function that creates a physic component from a shape.
         *
         *  @param world The world that manages all physics entities
         *  @param x The x position
         *  @param y The y position
         *  @param shape The shape for entity
         */
        fun EntityCreateCfg.physicCmpFromShape2D(
            world: World,
            x: Int,
            y: Int,
            shape: Shape2D
        ): PhysicComponent {
            when (shape) {
                is Rectangle -> {
                    val bodyX = x + shape.x * UNIT_SCALE
                    val bodyY = y + shape.y * UNIT_SCALE
                    val bodyW = shape.width * UNIT_SCALE
                    val bodyH = shape.height * UNIT_SCALE

                    return add {
                        body = world.body(BodyType.StaticBody) {
                            position.set(bodyX, bodyY)
                            fixedRotation = true
                            allowSleep = false
                            loop(
                                vec2(0f, 0f),
                                vec2(bodyW, 0f),
                                vec2(bodyW, bodyH),
                                vec2(0f, bodyH)
                            )
                            //circle(SPAWN_AREA_SIZE + 2f) { isSensor = true }
                            TMP_VEC.set(bodyW * 0.5f, bodyH * 0.5f)
                            box(SPAWN_AREA_SIZE + 4f, SPAWN_AREA_SIZE + 4f, TMP_VEC) {
                                isSensor = true
                            }
                        }
                    }
                }

                else -> gdxError("Shape $shape is not supported!")
            }
        }

        /**
         *  It's a function that creates a physic component from a image.
         *
         *  @param world The world that manages all physics entities
         *  @param image The image of the entity
         *  @param bodyType The bodyType of the entity
         *  @param fixtureAction The fixtureAction of the entity
         */
        fun EntityCreateCfg.physicCmpFromImage(
            world: World,
            image: Image,
            bodyType: BodyType,
            fixtureAction: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
        ): PhysicComponent {
            val x = image.x
            val y = image.y
            val w = image.width
            val h = image.height

            return add {
                body = world.body(bodyType) {
                    position.set(x + w * 0.5f, y + h * 0.5f)
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(this@add, w, h)
                }
            }
        }

        /**
         * When a PhysicComponent is added to an Entity, set the body's userData to the Entity.
         */
        class PhysicComponentListener : ComponentListener<PhysicComponent> {
            /**
             * When a component is added to an entity, set the user data of the component's body to the
             * entity
             *
             * @param entity The entity that the component was added to.
             * @param component The component that was added to the entity
             */
            override fun onComponentAdded(entity: Entity, component: PhysicComponent) {
                component.body.userData = entity
            }

            /**
             * When a PhysicComponent is removed from an Entity, destroy the Body and set its userData
             * to null.
             *
             * @param entity The entity that the component was removed from.
             * @param component The component that was removed from the entity.
             */
            override fun onComponentRemoved(entity: Entity, component: PhysicComponent) {
                val body = component.body
                body.world.destroyBody(body)
                body.userData = null
            }

        }
    }
}