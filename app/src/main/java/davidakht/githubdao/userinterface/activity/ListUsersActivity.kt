package davidakht.githubdao.userinterface.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import davidakht.githubdao.R
import davidakht.githubdao.adapter.ListUserAdapter
import davidakht.githubdao.data.User
import davidakht.githubdao.databinding.ActivityListUsersBinding
import davidakht.githubdao.datastore.SettingPreferences
import davidakht.githubdao.userinterface.fragment.MenuFragment
import davidakht.githubdao.viewmodel.DetailUserViewModel
import davidakht.githubdao.viewmodel.ListUserViewModel
import davidakht.githubdao.viewmodel.ViewModelFactory

class ListUsersActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_QUERY = "extra_query"
    }
    private lateinit var binding: ActivityListUsersBinding
    private lateinit var viewModel: ListUserViewModel
    private lateinit var adapter: ListUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "GitHub User's Search"
        binding.progressBar.visibility = View.VISIBLE
        val query = intent.getStringExtra(EXTRA_QUERY)
        adapter = ListUserAdapter()
        adapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@ListUsersActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID,data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATARURL,data.avatar_url)
                    startActivity(it)
                }
            }
        })
        viewModel = obtainViewModel(this@ListUsersActivity)
//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
//            ListUserViewModel::class.java
//        )
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@ListUsersActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }
        viewModel.getSearchUser().observe(this, {
            if (it != null) {
                binding.progressBar.visibility = View.GONE
                adapter.setList(it)
            }
        })
        if (query != null) {
            viewModel.setSearchUsers(query)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Intent(this@ListUsersActivity, ListUsersActivity::class.java).also {
                    it.putExtra(EXTRA_QUERY, query)
                    startActivity(it)
                    return true
                }
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MenuFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.Tentang -> {
                val i = Intent(this, MenuActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }
    private fun obtainViewModel(activity: AppCompatActivity): ListUserViewModel {
//    val factory = ViewModelFactory.getInstance(activity.application)
        val pref =  SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        return ViewModelProvider(activity, factory).get(ListUserViewModel::class.java)
    }
}