package br.com.bernardino.githubsearch.di

import br.com.bernardino.githubsearch.repository.ReposRepository
import br.com.bernardino.githubsearch.repository.ReposRepositoryImpl
import br.com.bernardino.githubsearch.viewmodel.HomeActivityViewModel
import br.com.bernardino.githubsearch.viewmodel.PullRequestActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val dataModuleRepository = module {

    viewModel { HomeActivityViewModel(get()) }
    single { ReposRepositoryImpl(get(), get()) }
}

@ExperimentalCoroutinesApi
val dataModulePullRequest = module {

    viewModel { params-> PullRequestActivityViewModel(get(),creator = params[0], repository = params[1]) }
}