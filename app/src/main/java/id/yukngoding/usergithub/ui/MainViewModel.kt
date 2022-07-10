package id.yukngoding.usergithub.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.yukngoding.usergithub.base.Resource
import id.yukngoding.usergithub.models.ErrorGithub
import id.yukngoding.usergithub.models.UserDetail
import id.yukngoding.usergithub.models.Users
import id.yukngoding.usergithub.repo.UserGithubRepo
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: UserGithubRepo
) : ViewModel() {

    var since = 0
    private val _res = MutableLiveData<Resource<Pair<ArrayList<Users>, Boolean>>>()
    private val _resLoadMore = MutableLiveData<Resource<ArrayList<Users>?>>()
    private val _resUserDetail = MutableLiveData<Resource<UserDetail>>()

    val res: LiveData<Resource<Pair<ArrayList<Users>, Boolean>>>
        get() = _res
    val resLoadMore: LiveData<Resource<ArrayList<Users>?>>
        get() = _resLoadMore
    val resUserDetail: LiveData<Resource<UserDetail>>
        get() = _resUserDetail

    init {
        getUsers()
    }

    private fun getUsers() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        mainRepository.getUsers(since).let {
            if (it.isSuccessful) {
                since = it.body()!!.last().id
                _res.postValue(Resource.success(Pair(it.body()!!, false)))
            } else {
                //todo: improve
                val errorMsg = it.errorBody()?.string()
                it.errorBody()?.close()
                val errorGithub = Gson().fromJson(errorMsg, ErrorGithub::class.java)
                _res.postValue(Resource.error(errorGithub.message, null))
            }
        }
    }

    fun refreshUsers() = viewModelScope.launch {
        since = 0
        _res.postValue(Resource.loading(null))
        mainRepository.getUsers(since).let {
            if (it.isSuccessful) {
                since = it.body()!!.last().id
                _res.postValue(Resource.success(Pair(it.body()!!, true)))
            } else {
                //todo: improve
                val errorMsg = it.errorBody()?.string()
                it.errorBody()?.close()
                val errorGithub = Gson().fromJson(errorMsg, ErrorGithub::class.java)
                _res.postValue(Resource.error(errorGithub.message, null))
            }
        }
    }

    fun loadMoreUsers() = viewModelScope.launch {
        _resLoadMore.postValue(Resource.loading(null))
        mainRepository.getUsers(since).let {
            if (it.isSuccessful) {
                since = it.body()!!.last().id
                _resLoadMore.postValue(Resource.success(it.body()))
            } else {
                //todo: improve
                val errorMsg = it.errorBody()?.string()
                it.errorBody()?.close()
                val errorGithub = Gson().fromJson(errorMsg, ErrorGithub::class.java)
                _resLoadMore.postValue(Resource.error(errorGithub.message, null))
            }
        }
    }

    fun loadUserDetail(username: String) = viewModelScope.launch {
        _resUserDetail.postValue(Resource.loading(null))
        mainRepository.getUserDetail(username).let {
            if (it.isSuccessful) {
                _resUserDetail.postValue(Resource.success(it.body()))
            } else {
                //todo: improve
                val errorMsg = it.errorBody()?.string()
                it.errorBody()?.close()
                val errorGithub = Gson().fromJson(errorMsg, ErrorGithub::class.java)
                _resUserDetail.postValue(Resource.error(errorGithub.message, null))
            }
        }
    }

}