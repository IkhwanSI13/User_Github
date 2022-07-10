package id.yukngoding.usergithub.repo.api

import id.yukngoding.usergithub.models.UserDetail
import id.yukngoding.usergithub.models.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int
    ): Response<ArrayList<Users>>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") userName: String
    ): Response<UserDetail>

}