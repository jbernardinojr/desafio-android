package br.com.bernardino.githubsearch.network

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import br.com.bernardino.githubsearch.model.PullRequest
import br.com.bernardino.githubsearch.model.RepositoryBody
import org.koin.core.KoinComponent
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi : KoinComponent {

    @GET("search/repositories?q=language:Java&sort=stars")
    suspend fun getRepositories(@Query("page") page: Int) : RepositoryBody
    @GET ("repos/{creator}/{repository}/pulls")
    suspend fun getPullRequests (@Path("creator") creator: String?,
                                 @Path("repository") repository : String?, @Query ("pagesize") pageSize : Int, @Query("page") page: Int) : List<PullRequest>
}