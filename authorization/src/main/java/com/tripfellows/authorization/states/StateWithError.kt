package com.tripfellows.authorization.states

data class StateWithError (
    var actionState: ActionState,
    var errorMessage: String?
)