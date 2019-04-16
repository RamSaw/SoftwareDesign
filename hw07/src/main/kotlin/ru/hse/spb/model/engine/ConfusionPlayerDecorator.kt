package ru.hse.spb.model.engine

/**
 * This class adds confusion action to player.
 */
class ConfusionPlayerDecorator(private val player: BasePlayer):BasePlayer(player.getCurrentPosition()) {
    private var confuseTime = 5

    override fun inclineDamage() = player.inclineDamage()

    fun confuseAfterAttack(mob: Mob){
        mob.getConfused(confuseTime)
    }

    override fun levelUp() {
        player.levelUp()
        confuseTime += AMPLIFIER
    }

    override fun takeOffEquipment() = player.takeOffEquipment()

    override fun takeOnEquipment() = player.takeOnEquipment()
}