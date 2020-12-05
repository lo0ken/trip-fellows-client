package com.tripfellows.authorization.states

data class ActionState (
    var actionStatus: ActionStatus,
    var errorMessage: String?
)