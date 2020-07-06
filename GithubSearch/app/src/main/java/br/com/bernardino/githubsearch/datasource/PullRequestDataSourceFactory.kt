package br.com.bernardino.githubsearch.datasource

import androidx.paging.DataSource
import br.com.bernardino.githubsearch.model.PullRequest
import br.com.bernardino.githubsearch.network.GithubApi
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PullRequestDataSourceFactory(
     private  val coroutineContext: CoroutineContext
    , private val api: GithubApi
    , private val creator: String
    , private val repositoryName: String) : DataSource.Factory<Int, PullRequest>() {

    override fun create(): DataSource<Int, PullRequest> {
        return PullRequestDataSource(coroutineContext, api, creator, repositoryName)
    }


}