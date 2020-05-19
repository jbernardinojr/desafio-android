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

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PullRequest>
    ) {
        val numbersOfItems = params.requestedLoadSize
        scope.launch {
            try {
                val response =
                    api.getPullRequests(creator, repositoryName, numbersOfItems, FIRST_PAGE)
                response?.let {
                    callback.onResult(it, FIRST_PAGE, PAGE_SIZE)
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
                    api.getPullRequests(creator, repositoryName, PAGE_SIZE,FIRST_PAGE + 1)

                response.let {
                    callback.onResult(it, numberOfItems)
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data: ${exception.message}")
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PullRequest>) {
        val page = params.key

        scope.launch {
            try {
                val adjacentKey = if (params.key > 1) params.key - 1 else  null;
                val response =
                    api.getPullRequests(creator, repositoryName, PAGE_SIZE, page)

                response.let {
                    callback.onResult(it, adjacentKey)
                }

            } catch (exception: Exception) {
                Log.e("PostsDataSource", "Failed to fetch data: ${exception.message}")
            }
        }

    }

    override fun invalidate() {
        super.invalidate()
        job.cancel()
    }

    companion object{
        const val   FIRST_PAGE = 1
        const val   PAGE_SIZE = 45
    }

}