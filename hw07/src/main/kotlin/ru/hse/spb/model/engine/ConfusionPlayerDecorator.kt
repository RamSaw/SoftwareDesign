package ru.hse.spb.model.engine

import ru.hse.spb.model.engine.strategy.ConfusedStrategy

/**
 * This class adds confusion action to players.
 */
class ConfusionPlayerDecorator(private val player: BasePlayer) : BasePlayer(player.getCurrentPosition()) {
    private var confuseTime = 5

    override fun inclineDamage() = player.inclineDamage()

    fun confuseAfterAttack(mob: Mob) {
        mob.setStrategy(ConfusedStrategy(confuseTime))
    }

    override fun levelUp() {
        player.levelUp()
        confuseTime += AMPLIFIER
    }

    override fun takeOnOffEquipment(equipmentId: Int) = player.takeOnOffEquipment(equipmentId)

    override fun getCurrentLevel() = player.getCurrentLevel()

    override fun getCurrentHealth() = player.getCurrentHealth()

    override fun takeDamage(dmg: Int) = player.takeDamage(dmg)
}