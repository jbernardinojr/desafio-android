package br.com.bernardino.githubsearch.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.bernardino.githubsearch.di.dataModulePullRequest
import br.com.bernardino.githubsearch.di.dataModuleRepository
import br.com.bernardino.githubsearch.di.networkModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

open class BaseActivity : AppCompatActivity() {

    private val koinFeatures by lazy {
        loadKoinModules(listOf(dataModuleRepository, networkModule, dataModulePullRequest))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(listOf(dataModuleRepository, networkModule, dataModulePullRequest))
    }
    private fun injectFeatures() = koinFeatures
}