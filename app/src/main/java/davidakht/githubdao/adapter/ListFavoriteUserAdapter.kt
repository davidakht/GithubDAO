package davidakht.githubdao.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import davidakht.githubdao.dao.FavoriteUserDiffCallback
import davidakht.githubdao.data.FavoriteUser
import davidakht.githubdao.databinding.ItemFavoriteUserBinding
import davidakht.githubdao.userinterface.activity.DetailFavoriteUserActivity

class ListFavoriteUserAdapter :
    RecyclerView.Adapter<ListFavoriteUserAdapter.FavoriteUserViewHolder>() {
    private val listFavoriteUsers = ArrayList<FavoriteUser>()
    private var onItemClickCallback: OnItemClickCallbackFavoriteUser? = null
    fun setListFavoriteUsers(listFavoriteUsers: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.listFavoriteUsers, listFavoriteUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUsers.clear()
        this.listFavoriteUsers.addAll(listFavoriteUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val binding =
            ItemFavoriteUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
    }

    override fun getItemCount(): Int {
        return listFavoriteUsers.size
    }

    interface OnItemClickCallbackFavoriteUser {
        fun onItemClicked(data: FavoriteUser)
    }

    inner class FavoriteUserViewHolder(private val binding: ItemFavoriteUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                Glide.with(itemView)
                    .load(favoriteUser.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(imgAvatar)
                tvUsername.text = favoriteUser.login
                rvUser.setOnClickListener {
                    val intent = Intent(it.context, DetailFavoriteUserActivity::class.java)
                    intent.putExtra(DetailFavoriteUserActivity.EXTRA_USERNAME, favoriteUser.login)
                    intent.putExtra(DetailFavoriteUserActivity.EXTRA_ID, favoriteUser.id)
                    it.context.startActivity(intent)
                    binding.root.setOnClickListener {
                        onItemClickCallback?.onItemClicked(favoriteUser)
                    }
                }
            }
        }
    }
}