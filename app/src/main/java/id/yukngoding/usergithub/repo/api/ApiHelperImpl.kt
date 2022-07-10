package id.yukngoding.usergithub.repo.api

import id.yukngoding.usergithub.models.UserDetail
import id.yukngoding.usergithub.models.Users
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {

    override suspend fun getUsers(perPage: Int, since: Int): Response<ArrayList<Users>> =
        apiService.getUsers(perPage, since)

    override suspend fun getUserDetail(userName: String): Response<UserDetail> =
        apiService.getUserDetail(userName)

}