package id.yukngoding.usergithub.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import id.yukngoding.usergithub.R
import id.yukngoding.usergithub.base.Resource
import id.yukngoding.usergithub.base.Status
import id.yukngoding.usergithub.models.UserDetail
import id.yukngoding.usergithub.models.Users
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserAdapter.UserClickListener {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter(this)
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = adapter

        viewAction()
        observeVm()
    }

    private fun viewAction() {
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            mainViewModel.refreshUsers()
        }

        rvUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!rvUser.canScrollVertically(1)) {
                    mainViewModel.loadMoreUsers()
                }
            }
        })
    }

    private fun observeVm() {
        mainViewModel.res.observe(this, observerList)
        mainViewModel.resLoadMore.observe(this, observerLoadMore)
        mainViewModel.resUserDetail.observe(this, observerUserDetail)
    }

    private var observerList = Observer<Resource<Pair<ArrayList<Users>, Boolean>>> {
        when (it.status) {
            Status.LOADING -> {
                progress.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                rvUser.visibility = View.GONE
            }
            Status.SUCCESS -> {
                progress.visibility = View.GONE
                tvError.visibility = View.GONE
                rvUser.visibility = View.VISIBLE
                it.data.let { res ->
                    val (result, refresh) = res!!
                    adapter.submitList(result.toList(), refresh)
                }
            }
            Status.ERROR -> {
                progress.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                rvUser.visibility = View.GONE
                tvError.text = "Error: ${it.message}"
            }
        }
    }

    private var observerLoadMore = Observer<Resource<ArrayList<Users>?>> {
        when (it.status) {
            Status.LOADING -> {
                progressLoadMore.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                progressLoadMore.visibility = View.GONE
                it.data.let { res ->
                    adapter.submitList(res!!.toList(), false)
                }
            }
            Status.ERROR -> {
                progressLoadMore.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onUserClickListener(user: Users) {
        mainViewModel.loadUserDetail(user.login)
    }

    private var observerUserDetail = Observer<Resource<UserDetail>> {
        when (it.status) {
            Status.LOADING -> {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            Status.SUCCESS -> {
                it.data.let { res ->
                    var text =
                        "Name: ${res?.name}\nEmail: ${res?.email}\nCreated_at: ${res?.created_at}"
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                }
            }
            Status.ERROR -> {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}