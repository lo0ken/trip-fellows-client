package com.tripfellows.authorization.network.response

import com.tripfellows.authorization.states.RequestProgress

data class ResultResponse<T>(
    var requestProgress: RequestProgress,
    var data: T?,
    var errorMessage: String?
    )