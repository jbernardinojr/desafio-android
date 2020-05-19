package br.com.bernardino.githubsearch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.bernardino.githubsearch.R
import br.com.bernardino.githubsearch.database.RepositoryDatabase
import br.com.bernardino.githubsearch.model.PullRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_pullrequest.view.*

class PullRequestListAdapter (val mContext: Context, private val mClickListener: (String) -> Unit)
    : PagedListAdapter<PullRequest,PullRequestListAdapter.ViewHolder>(PULL_REQUEST_COMPARATOR) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(pullRequest: PullRequest, context: Context, clickListener: (String) -> Unit) {
            val pullRequestTitle = itemView.tv_title_pr
            val pullRequestUser = itemView.tv_user_name_pr
            val pullRequestFullName = itemView.tv_fullname_pr
            val pullRequestDesc = itemView.tv_desc_pr
            val pullRequestAvatar = itemView.ci_user_avatar_pr

            pullRequestTitle.text = pullRequest.title
            pullRequestDesc.text = pullRequest.body
            pullRequestUser.text = pullRequest.user.login
            pullRequestFullName.text = pullRequest.user.login
            Glide.with(context).load(pullRequest.user.avatar_url).into(pullRequestAvatar)
            itemView.setOnClickListener{
                clickListener(pullRequest.html_url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.list_item_pullrequest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pullRequestItem = getItem(position)
        holder.let {
            pullRequestItem?.let { it1 -> it.bindView(it1, mContext, mClickListener) }
        }
    }

    companion object {
        private val PULL_REQUEST_COMPARATOR = object : DiffUtil.ItemCallback<PullRequest>() {
            override fun areItemsTheSame(oldItem: PullRequest, newItem: PullRequest): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PullRequest, newItem: PullRequest): Boolean =
                oldItem == newItem
        }
    }

}