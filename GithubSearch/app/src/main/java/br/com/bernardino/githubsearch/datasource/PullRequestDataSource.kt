package br.com.bernardino.githubsearch.datasource

import android.util.Log
import androidx.paging.PageKeyedDataSource
import br.com.bernardino.githubsearch.model.PullRequest
import br.com.bernardino.githubsearch.network.GithubApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PullRequestDataSource(
     coroutineContext: CoroutineContext
    , private val api: GithubApi
    , private val creator: String
    , private val repositoryName: String
) : PageKeyedDataSource<Int, PullRequest>() {

    private val job = Job()
    private val scope = CoroutineScope(coroutineContext + job)

    private var lastLoadPage = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PullRequest>
    ) {
        val numbersOfItems = params.requestedLoadSize
        scope.launch {
            try {
                val response =
                    api.getPullRequests(creator, repositoryName, numbersOfItems, 0)
                response?.let {
                    callback.onResult(it, 0, numbersOfItems)
                    lastLoadPage++
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data: ${exception.localizedMessage}")
            }

        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PullRequest>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize

        scope.launch {
            try {
                val response =
                    api.getPullRequests(creator, repositoryName, numberOfItems, page)

                response.let {
                    callback.onResult(it, lastLoadPage)
                    lastLoadPage++
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data: ${exception.message}")
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PullRequest>) {

    }

    override fun invalidate() {
        super.invalidate()
        job.cancel()
    }

    companion object{
        const val   FIRST_PAGE = 1
        const val   PAGE_SIZE = 10
    }

}