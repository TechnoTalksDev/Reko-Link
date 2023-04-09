package net.technotalks.rekolink
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class Events(private val socketServer: SocketServer) : Listener {
    @EventHandler
    fun onChatMessage(e: AsyncChatEvent) {
        val msgComponent= e.message() as TextComponent
        val message = msgComponent.content()
        val playerName = e.player.name

        if (message.contains("drill", ignoreCase = true)) {
            e.isCancelled = true
            val player = e.player

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWe don't talk about the drill around these parts..."))
        }else {
            Bukkit.getLogger().info("Trying to send message through webSocket!")
            socketServer.sendMessage("<$playerName> $message")
        }
    }

}