package com.gabriel.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.Scaling
import com.gabriel.Survivor.Companion.UNIT_SCALE
import com.gabriel.component.*
import com.gabriel.component.PhysicComponent.Companion.physicCmpFromImage
import com.gabriel.event.MapChangeEvent
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.*
import  com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.actors.FlipImage
import com.gabriel.ai.DefaultGlobalState
import com.gabriel.event.EntityAddEvent
import com.gabriel.event.fire
import com.github.quillraven.fleks.*
import ktx.log.logger
import kotlin.math.roundToInt

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val phWorld: World,
    private val atlas: TextureAtlas,
    private val spawnCmps: ComponentMapper<SpawnComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            log.debug { "Entity: ${spawnCmps[entity].type} Location ${spawnCmps[entity].location}" }
            val cfg = spawnCfg(type)
            var relativeSize = size(cfg.model)


            world.entity {
                val imageCmp = add<ImageComponent> {
                    image = FlipImage().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                    image.flipX = cfg.isFlip
                }

                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                val physicCmp = physicCmpFromImage(phWorld, imageCmp.image, cfg.bodyType) { phCmp, width, height ->
                    val w = width * cfg.physicScaling.x
                    val h = height * cfg.physicScaling.y
                    phCmp.offset.set(cfg.physicOffset)
                    phCmp.size.set(w, h)

                    // hit box
                    box(w, h, cfg.physicOffset) {
                        isSensor = false
                        userData = HIT_BOX_SENSOR
                    }

                    if (cfg.bodyType != StaticBody) {
                        // collision box
                        if (cfg.entityType != EntityType.ENEMY) {

                            val collH = h * 0.4f
                            val collOffset = vec2().apply { set(cfg.physicOffset) }
                            collOffset.y -= h * 0.5f - collH * 0.5f
                            box(w, collH, collOffset)
                        }

                    }
                }

                if (cfg.speedScaling > 0f) {
                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }

                if (cfg.canAttack) {
                    add<AttackComponent> {
                        maxCooldown = cfg.attackDelay
                        damage = (DEFAULT_ATTACK_DAMAGE * cfg.attackScaling).roundToInt()
                        extraRange = cfg.attackExtraRange
                    }
                }

                if (cfg.lifeScaling > 0f) {
                    add<LifeComponent> {
                        max = DEFAULT_LIFE * cfg.lifeScaling
                        life = max
                    }
                }

                when (cfg.entityType) {
                    EntityType.PLAYER -> {
                        add<PlayerComponent>()
                        add<ExperienceComponent>()
                        add<LevelComponent>()
                        add<StateComponent>() {
                            stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
                        }
                    }

                    EntityType.ENEMY -> {
                        add<EnemyComponent>()
                        add<ExperienceComponent>() {
                            dropExperience = cfg.dropExperience
                        }
                    }

                    EntityType.WEAPON -> {
                        add<WeaponComponent>()
                        log.debug { "$location" }
                    }

                    EntityType.SPAWN -> {

                    }

                    else -> {}
                }


                if (cfg.lootable) {
                    add<LootComponent>()
                }

                if (cfg.bodyType != StaticBody) {
                    // such entities will create/remove collision objects
                    add<CollisionComponent>()
                }

                if (cfg.aiTreePath.isNotBlank()) {
                    add<AiComponent> {
                        treePath = cfg.aiTreePath
                    }
                    physicCmp.body.box(1f, 1f) {
                        isSensor = true
                        userData = AI_SENSOR
                    }
                }
            }

            if (cfg.entityType == EntityType.PLAYER) {
                log.debug { " Before A Entity: ${spawnCmps[entity].type} Location ${spawnCmps[entity].location}" }
                gameStage.fire(
                    EntityAddEvent(
                        vec2((location.x / UNIT_SCALE) + 25, (location.y / UNIT_SCALE) - 5),
                        "SLASH_RIGHT"
                    )
                )
                log.debug { " After A Entity: ${spawnCmps[entity].type} Location ${spawnCmps[entity].location}" }
                log.debug { " Before B Entity: ${spawnCmps[entity].type} Location ${spawnCmps[entity].location}" }

                gameStage.fire(
                    EntityAddEvent(
                        vec2((location.x / UNIT_SCALE) - 25, (location.y / UNIT_SCALE) - 5),
                        "SLASH_LEFT"
                    )
                )
                log.debug { " After B Entity: ${spawnCmps[entity].type} Location ${spawnCmps[entity].location}" }


            }

        }
        world.remove(entity)
    }


    private fun spawnCfg(type: String): SpawnCfg = cachedCfgs.getOrPut(type) {
        log.debug { "Type $type" }
        when (type) {
            "PLAYER" -> SpawnCfg(
                AnimationModel.PLAYER,
                entityType = EntityType.PLAYER,
                attackExtraRange = 0.6f,
                attackScaling = 1.25f,
                speedScaling = 2.25f,
                lifeScaling = 10000f,
                physicScaling = vec2(1f, 0.5f),
                physicOffset = vec2(0f, -5f * UNIT_SCALE),
            )

            "SKULL" -> SpawnCfg(
                AnimationModel.SKULL,
                EntityType.ENEMY,
                lifeScaling = 0.75f,
                speedScaling = 0.8f,
                dropExperience = 5f,
//                physicScaling = vec2(1f, 0.5f),
//                physicOffset = vec2(0f, -5f * UNIT_SCALE),
                aiTreePath = "ai/enemy.tree"
            )

            "CHEST" -> SpawnCfg(
                AnimationModel.CHEST,
                speedScaling = 0f,
                bodyType = BodyDef.BodyType.StaticBody,
                canAttack = false,
                lifeScaling = 0f,
                lootable = true,
            )

            //Weapons
            "SLASH_LEFT" -> SpawnCfg(
                AnimationModel.SLASH_LEFT,
                EntityType.WEAPON,
                isFlip = true,
                attackExtraRange = 0.6f,
                attackScaling = 5.25f,
                speedScaling = 2.25f,
                lifeScaling = 0f,
                physicScaling = vec2(1f, 1f),
                physicOffset = vec2(0f, -5f * UNIT_SCALE),
                aiTreePath = "ai/slash.tree",


                )

            "SLASH_RIGHT" -> SpawnCfg(
                AnimationModel.SLASH_RIGHT,
                EntityType.WEAPON,
                attackExtraRange = 0.6f,
                attackScaling = 5.25f,
                speedScaling = 2.25f,
                lifeScaling = 0f,
                physicScaling = vec2(1f, 1f),
                physicOffset = vec2(0f, -5f * UNIT_SCALE),
                aiTreePath = "ai/slash.tree"

            )

            "SPAWN" -> SpawnCfg(AnimationModel.UNDEFINED, EntityType.SPAWN)

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
                        ?: gdxError("MapObject ${mapObj.id} of 'entities' layer does not have a NAME! MapChangeEvent")
                    world.entity {
                        add<SpawnComponent> {
                            this.type = typeStr
                            this.location.set(mapObj.x * UNIT_SCALE, mapObj.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }

            is EntityAddEvent -> {
                world.entity {
                    add<SpawnComponent> {
                        this.type = event.name
                        this.location.set(event.location.x * UNIT_SCALE, event.location.y * UNIT_SCALE)
                    }
                }
            }
        }
        return false
    }

    companion object {
        const val HIT_BOX_SENSOR = "Hitbox"
        const val AI_SENSOR = "AiSensor"
        private val log = logger<EntitySpawnSystem>()
    }
}