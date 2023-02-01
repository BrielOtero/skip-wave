package com.gabriel.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.utils.Scaling
import com.gabriel.SkipWave.Companion.UNIT_SCALE
import com.gabriel.component.*
import com.gabriel.component.PhysicComponent.Companion.physicCmpFromImage
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.*
import  com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.gabriel.actors.FlipImage
import com.gabriel.ai.DefaultGlobalState
import com.gabriel.event.*
import com.github.quillraven.fleks.*
import ktx.log.logger
import kotlin.math.roundToInt

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val phWorld: World,
    private val atlas: TextureAtlas,
    private val spawnCmps: ComponentMapper<SpawnComponent>,
    private val playerCmps: ComponentMapper<PlayerComponent>,
    private val levelCmps: ComponentMapper<LevelComponent>,
    @Qualifier("gameStage") private val gameStage: Stage,

    ) : EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
//            log.debug { "Entity: ${spawnCmps[entity].type} Location ${spawnCmps[entity].location}" }
            val cfg = spawnCfg(model)
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
                        add<ExperienceComponent>() {
                            experienceToNextLevel = 50f
                        }
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
//                        log.debug { "$location" }
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

            //Spawn weapons
            if (cfg.entityType == EntityType.PLAYER) {
                gameStage.fire(
                    EntityAddEvent(
                        vec2((location.x / UNIT_SCALE) + 25, (location.y / UNIT_SCALE) - 5),
                        AnimationModel.SLASH_RIGHT
                    )
                )

                gameStage.fire(
                    EntityAddEvent(
                        vec2((location.x / UNIT_SCALE) - 25, (location.y / UNIT_SCALE) - 5),
                        AnimationModel.SLASH_LEFT
                    )
                )
            }

        }
        world.remove(entity)
    }


    private fun spawnCfg(model: AnimationModel): SpawnCfg = cachedCfgs.getOrPut(model.name) {
        log.debug { "Type ${model.name}" }

        //ENEMY LEVEL SCALE
        val attack: Float
        val life: Float
        if (playerEntities.isEmpty) {
            attack = 5f
            life = 0.75f
        } else {
            attack = 5f + levelCmps[playerEntities.first()].level
            life = 0.75f + (levelCmps[playerEntities.first()].level / 10)
        }


//        levelCmps[playerEntities.first()].level


        //SPAWN CONFIG
        when (model) {
            AnimationModel.PLAYER -> SpawnCfg(
                model,
                entityType = EntityType.PLAYER,
                attackExtraRange = 0.6f,
                attackScaling = 0f,
                speedScaling = 1.2f,
                lifeScaling = 1f,
                physicScaling = vec2(1f, 0.5f),
                physicOffset = vec2(0f, -5f * UNIT_SCALE),
            )


            AnimationModel.CHEST -> SpawnCfg(
                model,
                speedScaling = 0f,
                bodyType = BodyDef.BodyType.StaticBody,
                canAttack = false,
                lifeScaling = 0f,
                lootable = true,
            )

            //WEAPONS
            AnimationModel.SLASH_LEFT -> SpawnCfg(
                model,
                EntityType.WEAPON,
                bodyType = KinematicBody,
                speedScaling = 0f,
                isFlip = true,
                attackExtraRange = 0f,
                attackScaling = 5f,
                attackDelay = 1f,
                lifeScaling = 0f,
                physicScaling = vec2(1f, 1f),
                physicOffset = vec2(0f, -5f * UNIT_SCALE),
                aiTreePath = "ai/slash.tree",

                )

            AnimationModel.SLASH_RIGHT -> SpawnCfg(
                model,
                EntityType.WEAPON,
                bodyType = KinematicBody,
                speedScaling = 0f,
                attackExtraRange = 0f,
                attackScaling = 5f,
                attackDelay = 1f,
                lifeScaling = 0f,
                physicScaling = vec2(1f, 1f),
                physicOffset = vec2(0f, -5f * UNIT_SCALE),
                aiTreePath = "ai/slash.tree"

            )

            //ENEMIES
            AnimationModel.AXOLOT -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
//                physicOffset = vec2(0f, -5f * UNIT_SCALE),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.AXOLOT_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 1.2f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.BAMBOO -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.BAMBOO_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.BUTTERFLY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.BUTTERFLY_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.CYCLOPE -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.CYCLOPE_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.DRAGON -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.DRAGON_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.FISH -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.FISH_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.FLAM -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.FLAM_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.MOUSE -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.MOUSE_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.OCTOPUS -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.OCTOPUS_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.RACOON -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.RACOON_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.SKULL -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.SKULL_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.SPIRIT -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.SPIRIT_SHINY -> SpawnCfg(
                model,
                EntityType.ENEMY,
                lifeScaling = life,
                speedScaling = 0.4f,
                attackScaling = attack,
                dropExperience = 10f,
                physicScaling = vec2(0.9f, 0.9f),
                aiTreePath = "ai/enemy.tree"
            )

            AnimationModel.UNDEFINED -> SpawnCfg(model, EntityType.SPAWN)
            else -> gdxError("Type $model has no SpawnCfg setup.")
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
                MAP_SIZE = vec2(event.map.width.toFloat(), event.map.height.toFloat())
                entityLayer.objects.forEach { mapObj ->
                    val typeStr = mapObj.name
                        ?: gdxError("MapObject ${mapObj.id} of 'entities' layer does not have a NAME! MapChangeEvent")
                    world.entity {
                        add<SpawnComponent> {
                            this.model = enumValueOf<AnimationModel>(typeStr)
                            this.location.set(mapObj.x * UNIT_SCALE, mapObj.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }

            is EntityAddEvent -> {
                world.entity {
                    add<SpawnComponent> {
                        this.model = event.model
                        this.location.set(event.location.x * UNIT_SCALE, event.location.y * UNIT_SCALE)
                    }
                }
            }

            is EnemyAddEvent -> {
                world.entity {
                    add<SpawnComponent> {
                        this.model = event.model
                        this.location.set(
                            MathUtils.random(2f, MAP_SIZE.x - 2f),
                            MathUtils.random(2f, MAP_SIZE.y - 2f)
                        )
                    }
                }
            }
        }
        return false
    }

    companion object {
        var MAP_SIZE = vec2(0f, 0f)
        const val HIT_BOX_SENSOR = "Hitbox"
        const val AI_SENSOR = "AiSensor"
        private val log = logger<EntitySpawnSystem>()
    }
}