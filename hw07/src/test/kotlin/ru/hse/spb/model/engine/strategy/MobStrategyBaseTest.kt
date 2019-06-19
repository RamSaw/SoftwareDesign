package ru.hse.spb.model.engine.strategy

import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.engine.BasePlayer
import ru.hse.spb.model.engine.Mob
import kotlin.math.abs

open class MobStrategyBaseTest {
    protected val mob: Mob = mock(Mob::class.java)
    protected val map: Map = Map.load(javaClass.getResource(MAP_PATH).path)
    protected val model: Model = mock(Model::class.java)
    protected val player: BasePlayer = mock(BasePlayer::class.java)

    private val mobPosition: Map.MapPosition = Map.MapPosition(MOB_X, MOB_Y)
    private val playerPosition: Map.MapPosition = Map.MapPosition(PLAYER_X, PLAYER_Y)

    @BeforeEach
    fun setUp() {
        `when`(mob.position).thenReturn(mobPosition)
        `when`(model.player).thenReturn(player)
        `when`(player.position).thenReturn(playerPosition)
    }

    companion object {
        private const val MAP_PATH = "/maps/strategy_base_test.txt"
        private const val MOB_X = 2
        private const val MOB_Y = 2
        private const val PLAYER_X = 4
        private const val PLAYER_Y = 4

        fun dist(first: Map.MapPosition, second: Map.MapPosition) : Int {
            return abs(first.x - second.x) + abs(first.y - second.y)
        }
    }
}
