package com.tripfellows.authorization.network.request

import com.tripfellows.authorization.states.Progress

data class ResultResponse<T>(
    var progress: Progress,
    var data: T?,
    var errorMessage: String?
    )