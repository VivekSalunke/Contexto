package com.example.contexto.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.contexto.ai.OpenRouterClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContextNotificationListener : NotificationListenerService() {

    private val TAG = "ContextoListener"
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    // Triggered whenever a new notification appears on the phone
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        // Ignore notifications from our own app to prevent loops
        if (packageName == this.packageName) return

        if (title.isNotEmpty() && text.isNotEmpty()) {
            Log.d(TAG, "Captured from $packageName: $title - $text")
            Log.d(TAG, "Sending payload to OpenRouter using llama-3.1-8b-instruct:free...")
            
            serviceScope.launch {
                val actionTask = OpenRouterClient.analyzeText(title, text)
                
                if (actionTask != "IGNORE") {
                    Log.d(TAG, "🎯 Routed task via OpenRouter: $actionTask")
                    // This flows into your Room database / Jetpack UI next
                }
            }
        }
    }

    // Triggered when a user swipes away or clears a notification
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d(TAG, "Notification removed from: ${sbn.packageName}")
    }
}
