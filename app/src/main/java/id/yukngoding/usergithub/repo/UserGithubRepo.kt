package id.yukngoding.usergithub.repo

import id.yukngoding.usergithub.repo.api.ApiHelper
import javax.inject.Inject

class UserGithubRepo @Inject constructor(
    private val apiHelper: ApiHelper
) {

    suspend fun getUsers(since: Int) = apiHelper.getUsers(10, since)

    suspend fun getUserDetail(username: String) = apiHelper.getUserDetail(username)

}