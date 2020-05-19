package br.com.bernardino.githubsearch.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.bernardino.githubsearch.R
import br.com.bernardino.githubsearch.adapter.ReposListAdapter
import br.com.bernardino.githubsearch.database.RepositoryDatabase
import br.com.bernardino.githubsearch.databinding.ActivityHomeBinding
import br.com.bernardino.githubsearch.di.dataModulePullRequest
import br.com.bernardino.githubsearch.di.dataModuleRepository
import br.com.bernardino.githubsearch.di.databaseModule
import br.com.bernardino.githubsearch.di.networkModule
import br.com.bernardino.githubsearch.model.EXTRA_REPOSITORY
import br.com.bernardino.githubsearch.repository.ReposRepositoryImpl
import br.com.bernardino.githubsearch.viewmodel.HomeActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class HomeActivity : BaseActivity() {

    lateinit var mAdapter: ReposListAdapter
    lateinit var mBinding: ActivityHomeBinding
    lateinit var mHomeActivityViewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mHomeActivityViewModel = getViewModel()

        mBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )
        mBinding.viewmodel = mHomeActivityViewModel
        mBinding.lifecycleOwner = this

        configureToolbar(mBinding.homeToolbar, getString(R.string.app_name))

        configureList()
        attachObserver()
    }

    private fun configureToolbar(t: Toolbar, title: String) {
        t.setNavigationIcon(R.drawable.ic_githubicon)
        t.title = title
        setSupportActionBar(t)
    }

    private fun attachObserver() {
        mHomeActivityViewModel.repos.observe(this, Observer {
            showEmptyList(it?.size == 0)
            mAdapter.submitList(it)
        })

        mHomeActivityViewModel.networkErrors.observe(this, Observer {
            onNetworkError(it)
        })
    }

    private fun configureList() {
        mBinding.rvRepos.addItemDecoration(
            DividerItemDecoration(
                mBinding.rvRepos.context,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter = ReposListAdapter(this) { repositoryDatabase: RepositoryDatabase ->
            clickListener(repositoryDatabase)
        }
        mBinding.rvRepos.adapter = mAdapter
    }

    private fun onNetworkError(message: String) {
            Snackbar.make(
                rv_repos,
                message,
                Snackbar.LENGTH_LONG
            ).show()
    }

    private fun clickListener(repository: RepositoryDatabase) {
        val intent = Intent(this, PullRequestActivity::class.java).apply {
            putExtra(EXTRA_REPOSITORY, repository)
        }
        startActivity(intent)
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            rv_repos.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            rv_repos.visibility = View.VISIBLE
        }
    }
}

