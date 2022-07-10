package id.yukngoding.usergithub.repo.api

import id.yukngoding.usergithub.models.UserDetail
import id.yukngoding.usergithub.models.Users
import retrofit2.Response

interface ApiHelper {

    suspend fun getUsers(perPage: Int, since: Int): Response<ArrayList<Users>>

    suspend fun getUserDetail(userName: String): Response<UserDetail>

}