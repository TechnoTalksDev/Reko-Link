package net.technotalks.rekolink
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class Events(private val socketServer: SocketServer, private val plugin: JavaPlugin) : Listener {
    private val config = plugin.config

    @EventHandler
    fun onChatMessage(e: AsyncChatEvent) {
        val msgComponent= e.message() as TextComponent
        val message = msgComponent.content()
        val playerName = e.player.name
        val enabled = config.getBoolean("chatToDiscord")
        if (enabled) {
            socketServer.sendMessage("{\"type\": \"chat\", \"player\": \"$playerName\", \"msg\": \"$message\" }")
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val message = "**"+e.player.name+"**" + " has joined the server"
        //Bukkit.getLogger().info(message)
        val enabled = config.getBoolean("joinLeaveToDiscord")
        if (enabled) {
            socketServer.sendMessage("{\"type\": \"broadcast\", \"msg\": \"$message\" }")
        }
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        val message = "**"+e.player.name+"**" + " has left the server"
        //Bukkit.getLogger().info(message)
        val enabled = config.getBoolean("joinLeaveToDiscord")
        if (enabled) {
            socketServer.sendMessage("{\"type\": \"broadcast\", \"msg\": \"$message\" }")
        }
    }

}