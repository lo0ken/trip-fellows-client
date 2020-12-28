package com.tripfellows.authorization.network

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.tripfellows.authorization.util.UtilConstants.getServerURL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiRepo(
    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor {
            val user = FirebaseAuth.getInstance().currentUser
            val tokenTask = user?.getIdToken(true)!!
            val result = Tasks.await<GetTokenResult>(tokenTask)
            var token = ""

            if (tokenTask.isSuccessful) {
                token = result.token!!
            }

            val request = it.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            it.proceed(request)
        }.build(),

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(getServerURL())
        .client(okHttpClient)
        .build(),

    val accountApi: AccountApi = retrofit.create(AccountApi::class.java),
    val tripApi: TripApi = retrofit.create(TripApi::class.java),
    val fcmTokenApi: FcmTokenApi = retrofit.create(FcmTokenApi::class.java)
)