package id.yukngoding.usergithub.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.api.loadAny
import coil.transform.CircleCropTransformation
import id.yukngoding.usergithub.R
import id.yukngoding.usergithub.models.Users
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(private val itemClickListener: UserClickListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    lateinit var imageLoader: ImageLoader
    private var list: MutableList<Users> = mutableListOf()

    inner class UserViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val viewHolder = UserViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.user_item,
                parent,
                false
            )
        )
        viewHolder.containerView.clUser.setOnClickListener {
            itemClickListener.onUserClickListener(list[viewHolder.adapterPosition])
        }
        imageLoader = Coil.imageLoader(parent.context)
        return viewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = list[position]

        holder.itemView.apply {
            ivUser.loadAny(item) {
                placeholder(R.drawable.ic_account)
                error(R.drawable.ic_account)
                transformations(CircleCropTransformation())
            }
            tvName.text = "${item.login} (${item.id})"
            tvRepo.text = "${item.repos_url}"
        }
    }

    //

    fun submitList(newList: List<Users>, refresh: Boolean) {
        val lastItem = list.size

        if (refresh) {
            list.clear()
            notifyItemRangeRemoved(0, lastItem)
        }

        list.addAll(newList)

        if (refresh)
            notifyItemRangeInserted(0, list.size)
        else
            notifyItemRangeInserted(lastItem, list.size)
    }

    //
    interface UserClickListener {
        fun onUserClickListener(user: Users)
    }
}