package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.model.TripMember
import com.tripfellows.authorization.model.TripStatusCodeEnum
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.network.request.JoinMemberRequest
import com.tripfellows.authorization.network.response.APIResponse
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.network.response.APIError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TripRepo(
    private val apiRepo: ApiRepo,
    private val trips: MutableLiveData<List<Trip>> = MutableLiveData()) {
    private val trip: MutableLiveData<Trip> = MutableLiveData()
    private val currentTrip: MutableLiveData<Trip> = MutableLiveData()

    init {
        trips.value = Collections.emptyList()
    }

    companion object {
        fun getInstance(context: Context): TripRepo {
            return ApplicationModified.from(context).tripRepo
        }
    }

    fun createTrip(createTripRequest: CreateTripRequest): LiveData<RequestProgress> {
        val createTripProgress: MutableLiveData<RequestProgress> = MutableLiveData()
        createTripProgress.value = RequestProgress.IN_PROGRESS

        apiRepo.tripApi.createTrip(createTripRequest).enqueue(object: Callback<CreateTripRequest> {
            override fun onResponse(call: Call<CreateTripRequest>, response: Response<CreateTripRequest>) {
                if (response.isSuccessful && response.body() != null) {
                    createTripProgress.postValue(RequestProgress.SUCCESS)
                }
            }

            override fun onFailure(call: Call<CreateTripRequest>, t: Throwable) {
                createTripProgress.postValue(RequestProgress.FAILED)
            }
        })

        return createTripProgress
    }

    fun getTrips(): MutableLiveData<List<Trip>> {
        return trips
    }

    fun getTrip(): MutableLiveData<Trip> {
        return trip
    }

    fun getCurrentTrip(): MutableLiveData<Trip> {
        return currentTrip
    }

    fun refreshTrips() {
        apiRepo.tripApi.getAllActiveTrips().enqueue(object: Callback<List<Trip>> {

            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                if (response.isSuccessful && response.body() != null) {
                    trips.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
            }
        })
    }

    fun refreshTrip(tripId: Int) {
        apiRepo.tripApi.getTrip(tripId).enqueue(object: Callback<Trip> {
            override fun onResponse(call: Call<Trip>, response: Response<Trip>) {
                if (response.isSuccessful && response.body() != null) {
                    trip.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Trip>, t: Throwable) {
            }
        })
    }

    fun joinMember(joinMemberRequest: JoinMemberRequest): MutableLiveData<APIResponse<TripMember>> {
        val apiResponse = APIResponse<TripMember>(RequestProgress.IN_PROGRESS, null, "")
        val joinProgress = MutableLiveData(apiResponse)

        apiRepo.tripApi.joinMember(joinMemberRequest).enqueue(object: Callback<TripMember> {
            override fun onResponse(call: Call<TripMember>, response: Response<TripMember>) {
                if (response.isSuccessful) {
                    apiResponse.requestProgress = RequestProgress.SUCCESS
                    apiResponse.data = response.body()
                    joinProgress.postValue(apiResponse)
                } else {
                    val message = Gson().fromJson(response.errorBody()!!.charStream(),
                        APIError::class.java)
                    apiResponse.requestProgress = RequestProgress.FAILED
                    apiResponse.errorMessage = message.message.toString()
                    joinProgress.postValue(apiResponse)
                }
            }

            override fun onFailure(call: Call<TripMember>, t: Throwable) {
                apiResponse.requestProgress = RequestProgress.FAILED
                apiResponse.errorMessage = t.message.toString()
                joinProgress.postValue(apiResponse)
            }
        })

        return joinProgress
    }

    fun removeMember(tripMemberId: Int): MutableLiveData<RequestProgress> {
        val removingProgress: MutableLiveData<RequestProgress> = MutableLiveData()
        removingProgress.value = RequestProgress.IN_PROGRESS

        apiRepo.tripApi.removeMember(tripMemberId).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                removingProgress.postValue(RequestProgress.SUCCESS)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                removingProgress.postValue(RequestProgress.FAILED)
            }
        })

        return removingProgress;
    }

    fun changeStatus(tripId: Int, status: TripStatusCodeEnum) {
        apiRepo.tripApi.changeStatus(tripId, status).enqueue(object: Callback<Trip> {
            override fun onResponse(call: Call<Trip>, response: Response<Trip>) {
                if (response.isSuccessful && response.body() != null) {
                    trip.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Trip>, t: Throwable) {
            }
        })
    }

    fun currentTripRequest() {
        apiRepo.tripApi.getCurrentTrip().enqueue(object: Callback<Trip> {
            override fun onResponse(call: Call<Trip>, response: Response<Trip>) {
                if (response.isSuccessful && response.body() != null) {
                    currentTrip.postValue(response.body())
                } else {
                    currentTrip.postValue(null)
                }
            }

            override fun onFailure(call: Call<Trip>, t: Throwable) {
                currentTrip.postValue(null)
            }
        })

    }
}