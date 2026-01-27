package com.example.geekhub.Repository

import com.example.geekhub.Model.IpLocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface IpApiService {
    @GET("json")
    suspend fun getLocation(): IpLocation
}

class LocationRepositorio {

    private val api = Retrofit.Builder()
        .baseUrl("http://ip-api.com/") // Nota: El plan gratuito usa http
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IpApiService::class.java)

    suspend fun fetchLocation(): Result<IpLocation> {
        return try {
            Result.success(api.getLocation())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}