package com.gabriel.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.gabriel.Survivor.Companion.UNIT_SCALE
import com.gabriel.component.*
import com.gabriel.component.PhysicComponent.Companion.physicCmpFromImage
import com.gabriel.event.MapChangeEvent
import com.gabriel.screen.GameScreen
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.log.logger
import ktx.math.vec2
import ktx.tiled.*
import kotlin.math.log
import  com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import com.gabriel.actor.FlipImage

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val phWorld: World,
    private val atlas: TextureAtlas,
    private val spawnCmps: ComponentMapper<SpawnComponent>,

    ) : EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val cfg = spawnCfg(type)
            var relativeSize = size(cfg.model)

            world.entity {
                val imageCmp = add<ImageComponent> {
                    image = FlipImage().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }

                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                physicCmpFromImage(phWorld, imageCmp.image, cfg.bodyType) { phCmp, width, height ->
                    val w = width * cfg.physicScaling.x
                    val h = height * cfg.physicScaling.y

                    // hit box
                    box(w, h, cfg.physicOffset) {
                        isSensor = cfg.bodyType != StaticBody
                    }

                    if (cfg.bodyType != StaticBody) {
                        // collision box
                        val collH = h * 0.4f
                        val collOffset = vec2().apply { set(cfg.physicOffset) }
                        collOffset.y -= h * 0.5f - collH * 0.5f
                        box(w, collH, collOffset)

                    }

                }

                if (cfg.speedScaling > 0f) {
                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }

                if (cfg.model.name == "PLAYER") {
                    add<PlayerComponent>()
                }

                if (cfg.bodyType != StaticBody) {
                    // such entities will create/remove collision objects
                    add<CollisionComponent>()
                }
            }
        }
        world.remove(entity)
    }

    private fun spawnCfg(type: String): SpawnCfg = cachedCfgs.getOrPut(type) {
        when (type) {
            "PLAYER" -> SpawnCfg(
                AnimationModel.PLAYER,
                physicScaling = vec2(0.3f, 0.3f),
                physicOffset = vec2(0f, -10f * UNIT_SCALE)
            )

            "SLIME" -> SpawnCfg(
                AnimationModel.SLIME,
                physicScaling = vec2(0.3f, 0.3f),
                physicOffset = vec2(0f, -2f * UNIT_SCALE)
            )

            "CHEST" -> SpawnCfg(AnimationModel.CHEST, speedScaling = 0f, bodyType = BodyDef.BodyType.StaticBody)
            else -> gdxError("Type $type has no SpawnCfg setup.")
        }
    }

    private fun size(model: AnimationModel) = cachedSizes.getOrPut(model) {
        val regions = atlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if (regions.isEmpty) {
            gdxError("There are no regions for the idle animation of model $model")
        }
        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach { mapObj ->
                    val typeStr = mapObj.name
                        ?: gdxError("MapObject ${mapObj.id} of 'entities' layer does not have a NAME")
                    world.entity {
                        add<SpawnComponent> {
                            this.type = typeStr
                            this.location.set(mapObj.x * UNIT_SCALE, mapObj.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }
        }
        return false
    }
}