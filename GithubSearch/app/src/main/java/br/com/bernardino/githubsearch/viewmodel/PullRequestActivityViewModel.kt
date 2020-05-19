package br.com.bernardino.githubsearch.viewmodel

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import br.com.bernardino.githubsearch.datasource.PullRequestDataSource
import br.com.bernardino.githubsearch.model.PullRequest
import br.com.bernardino.githubsearch.network.GithubApi
import kotlinx.coroutines.Dispatchers

class PullRequestActivityViewModel(
    githubApi: GithubApi,
    creator: String,
    repository: String
) : ViewModel() {


    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    private var _postsLiveData : LiveData<PagedList<PullRequest>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        _postsLiveData  = initializedPagedListBuilder(config, githubApi, creator, repository).build()
    }

    fun getPullRequest() : LiveData<PagedList<PullRequest>> = _postsLiveData

    private fun initializedPagedListBuilder(config: PagedList.Config, githubApi: GithubApi, creator: String, repositoryName: String):
            LivePagedListBuilder<Int, PullRequest> {

        _isLoading.postValue(true)
        val pullRequestSourceLiveData = MutableLiveData<PullRequestDataSource>()

        val dataSourceFactory = object : DataSource.Factory<Int, PullRequest>() {
            override fun create(): DataSource<Int, PullRequest> {
                val  pullRequestDataSource = PullRequestDataSource(Dispatchers.IO, githubApi, creator, repositoryName)
                pullRequestSourceLiveData.postValue(pullRequestDataSource)
                return pullRequestDataSource
            }

        }
        _isLoading.postValue(false)
        return LivePagedListBuilder(dataSourceFactory, config)
    }

    companion object {
        private const val PAGE_SIZE = 45
    }
}