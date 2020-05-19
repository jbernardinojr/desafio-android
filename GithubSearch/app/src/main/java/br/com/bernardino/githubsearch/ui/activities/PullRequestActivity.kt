package br.com.bernardino.githubsearch.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.bernardino.githubsearch.R
import br.com.bernardino.githubsearch.adapter.PullRequestListAdapter
import br.com.bernardino.githubsearch.database.RepositoryDatabase
import br.com.bernardino.githubsearch.databinding.ActivityPullrequestBinding
import br.com.bernardino.githubsearch.di.dataModulePullRequest
import br.com.bernardino.githubsearch.di.dataModuleRepository
import br.com.bernardino.githubsearch.di.databaseModule
import br.com.bernardino.githubsearch.di.networkModule
import br.com.bernardino.githubsearch.model.EXTRA_REPOSITORY
import br.com.bernardino.githubsearch.network.GithubApi
import br.com.bernardino.githubsearch.viewmodel.PullRequestActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class PullRequestActivity : BaseActivity(), KoinComponent {

    lateinit var mBinding: ActivityPullrequestBinding
    lateinit var mAdapter: PullRequestListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pullrequest)

        val repository = intent.getParcelableExtra<RepositoryDatabase>(EXTRA_REPOSITORY)

        val mPullRequestActivityViewModel = getViewModel<PullRequestActivityViewModel> {
            parametersOf(repository.ownerUserFirstName, repository.name)
        }

        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_pullrequest
        )
        mBinding.viewmodel = mPullRequestActivityViewModel
        mBinding.lifecycleOwner = this

        configureToolbar(mBinding.pullrequestToolbar, repository.name)
        setSupportActionBar(mBinding.pullrequestToolbar)

        configureList()
        attachObserver()
    }

    private fun configureToolbar(t: Toolbar, title: String) {
        t.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        t.title = title
        t.setNavigationOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }
    }

    private fun configureList() {
        mBinding.rvPullrequest.addItemDecoration(
            DividerItemDecoration(
                mBinding.rvPullrequest.context,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter = PullRequestListAdapter(this) { url: String -> clickListener(url) }
        mBinding.rvPullrequest.adapter = mAdapter
    }

    private fun attachObserver() {
        mBinding.viewmodel?.isLoading?.observe(this, Observer<Boolean> {
            it?.let { showLoadingDialog(it) }
        })

        mBinding.viewmodel?.getPullRequest()?.observe(this, Observer {
            mAdapter.submitList(it)
        })

        //mBinding.viewmodel?.eventNetworkError?.observe(this, Observer<Boolean> { isNetworkError ->
        //  if (isNetworkError) onNetworkError()
        //})
    }

    private fun showLoadingDialog(show: Boolean) {
        if (show) mBinding.pbPullrequest.visibility =
            View.VISIBLE else mBinding.pbPullrequest.visibility = View.GONE
    }

    private fun onNetworkError() {
        //if (!mBinding.viewmodel?.isNetworkErrorShown?.value!!) {
        //  Snackbar.make(
        //    mBinding.rvPullrequest,
        //  getString(R.string.network_error_msg),
        //Snackbar.LENGTH_LONG
        //).show()
        //mBinding.viewmodel?.onNetworkErrorShown()
        //}
    }

    private fun clickListener(url: String) {
        val webIntent: Intent = Uri.parse(url).let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }

        startActivity(webIntent)
    }
}
