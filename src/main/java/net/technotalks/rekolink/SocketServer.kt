package net.technotalks.rekolink

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.java_websocket.WebSocket
import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshakeBuilder
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.security.MessageDigest

class SocketServer(port: Int, private val plugin: JavaPlugin) : WebSocketServer(InetSocketAddress(port))  {
    private lateinit var webSocket: WebSocket
    private val logger = plugin.logger
    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        webSocket = conn
        val authToken = handshake?.getFieldValue("token")
        if (authToken == null || authToken == "") {
            conn.close(1008, "No token provided")
        } else {
            val token = plugin.config.getString("token")
            val hashedToken = MessageDigest.getInstance("SHA-256").digest(token?.toByteArray()).joinToString("") {
                "%02x".format(it)
            }
            if (hashedToken != authToken) {
                conn.close(1008, "Invalid token")
            }else {
                logger.info("Connected to Reko (Discord)!")
            }
        }
    }
    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        logger.info("Disconnected from Reko (Discord)!")
    }
    override fun onMessage(conn: WebSocket?, message: String) {
        Bukkit.broadcastMessage("[Discord] $message")
    }
    override fun onError(conn: WebSocket?, ex: Exception) {
        logger.severe("WebSocket error: " + ex.message)
    }
    override fun onStart() {
        logger.info("WebSocket server started on port $port")
        if (plugin.config.getString("token") == "Default") {
            logger.warning("****************************")
            logger.warning("WEBSOCKET PASSWORD IS DEFAULT")
            logger.warning("THIS IS A SECURITY RISK")
            logger.warning("CHANGE IMMEDIATELY IN CONFIG")
            logger.warning("****************************")
        }
    }

    fun sendMessage(message: String) {
        if (this::webSocket.isInitialized && webSocket.isOpen) {
            webSocket.send(message)
            //logger.info("Sent message through web socket: $message")
        } else {
            logger.warning("WebSocket connection is not open, cannot send message.")
        }
    }
}