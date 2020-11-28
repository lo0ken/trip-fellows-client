package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.states.RequestProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TripRepo(
    private val apiRepo: ApiRepo,
    private val trips: MutableLiveData<List<Trip>> = MutableLiveData()) {
    private val trip: MutableLiveData<Trip> = MutableLiveData()

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
        return trips;
    }

    fun getTrip(): MutableLiveData<Trip> {
        return trip;
    }

    fun refreshTrips() {
        apiRepo.tripApi.getAllTrips().enqueue(object: Callback<List<Trip>> {

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
}