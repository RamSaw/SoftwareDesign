package ru.hse.spb.model.engine

import ru.hse.spb.model.engine.strategy.ConfusedStrategy

/**
 * This class adds confusion action to player.
 */
class ConfusionPlayerDecorator(private val player: BasePlayer) : BasePlayer(player.position) {
    override val equipment : List<Equipment>
        get() = player.equipment
    override val level: Int
        get() = player.level
    override val health: Int
        get() = player.health

    private var confuseTime = 5

    override fun inclineDamage() = player.inclineDamage()

    override fun attack(other: GameCharacter) {
        player.attack(other)

        if (other::class.java == Mob::class.java) {
            confuseAfterAttack(other as Mob)
        }
    }

    private fun confuseAfterAttack(mob: Mob) {
        mob.setStrategy(ConfusedStrategy(confuseTime))
    }

    override fun levelUp() {
        player.levelUp()
        confuseTime += AMPLIFIER
    }

    override fun takeOnOffEquipment(equipmentId: Int) = player.takeOnOffEquipment(equipmentId)

    override fun takeDamage(dmg: Int) = player.takeDamage(dmg)
}