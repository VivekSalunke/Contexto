package com.example.contexto.ai

import androidx.appfunctions.AppFunction
import androidx.appfunctions.AppFunctionContext

class AgentFunctions {

    /**
     * Extracts an actionable task from the current background system context.
     * @param context The application context.
     * @param taskTitle Plain text title summarizing what needs to be done.
     * @param urgency Priority tier: "HIGH", "MEDIUM", or "LOW".
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun createSmartTask(
        context: AppFunctionContext,
        taskTitle: String,
        urgency: String
    ): String {
        // This is where our app execution happens when the OS triggers it.
        return "Task '$taskTitle' ($urgency) successfully queued by the system agent."
    }
}
