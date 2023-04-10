package net.technotalks.rekolink

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.java_websocket.WebSocket
import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshakeBuilder
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class SocketServer(port: Int, private val plugin: JavaPlugin) : WebSocketServer(InetSocketAddress(port))  {
    private lateinit var webSocket: WebSocket

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        Bukkit.getLogger().info("WebSocket connection opened from " + conn.getRemoteSocketAddress())
        webSocket = conn
        val authToken = handshake?.getFieldValue("token")
        if (authToken == null || authToken == "") {
            conn.close(1008, "No token provided")
        } else {
            //conn.send(authToken)
            val token = plugin.config.getString("token")
            if (token != authToken) {
                conn.close(1008, "Invalid token")
            }
            if (token == "Default") {
                Bukkit.getLogger().warning("****************************")
                Bukkit.getLogger().warning("[Reko-Link] WEBSOCKET PASSWORD IS DEFAULT")
                Bukkit.getLogger().warning("[Reko-Link] THIS IS A SECURITY RISK")
                Bukkit.getLogger().warning("[Reko-Link] CHANGE IMMEDIATELY IN CONFIG")
                Bukkit.getLogger().warning("****************************")
            }
        }
    }
    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        Bukkit.getLogger()
            .info("WebSocket connection closed from " + conn.getRemoteSocketAddress() + " (reason: " + reason + ")")
    }
    override fun onMessage(conn: WebSocket?, message: String) {
        Bukkit.broadcastMessage("[Discord] $message")
        conn?.send("Received: $message")
    }
    override fun onError(conn: WebSocket?, ex: Exception) {
        Bukkit.getLogger().severe("WebSocket error: " + ex.message)
    }
    override fun onStart() {
        Bukkit.getLogger().info("WebSocket server started on port $port")
        if (plugin.config.getString("token") == "Default") {
            Bukkit.getLogger().warning("[Reko-Link] WEBSOCKET PASSWORD IS DEFAULT")
            Bukkit.getLogger().warning("[Reko-Link] THIS IS A SECURITY RISK")
            Bukkit.getLogger().warning("[Reko-Link] CHANGE IMMEDIATELY IN CONFIG")
        }
    }
    fun sendMessage(message: String) {
        if (this::webSocket.isInitialized && webSocket.isOpen) {
            webSocket.send(message)
            Bukkit.getLogger().info("Sent message through web socket: $message")
        } else {
            Bukkit.getLogger().warning("WebSocket connection is not open, cannot send message.")
        }
    }
}