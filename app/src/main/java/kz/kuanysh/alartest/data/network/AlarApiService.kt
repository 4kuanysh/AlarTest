package kz.kuanysh.alartest.data.network

import kz.kuanysh.alartest.data.model.AuthResponse
import kz.kuanysh.alartest.data.model.DataResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AlarApiService {

    @POST("auth.cgi")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String,
    ): AuthResponse

    @GET("data.cgi")
    suspend fun getData(
        @Query("code") code: String,
        @Query("p") page: Int,
    ): DataResponse

}