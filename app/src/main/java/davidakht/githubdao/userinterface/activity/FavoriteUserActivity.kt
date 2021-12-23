package davidakht.githubdao.userinterface.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import davidakht.githubdao.R
import davidakht.githubdao.adapter.ListFavoriteUserAdapter
import davidakht.githubdao.databinding.ActivityFavoriteUserBinding
import davidakht.githubdao.datastore.SettingPreferences
import davidakht.githubdao.userinterface.fragment.MenuFragment
import davidakht.githubdao.viewmodel.FavoriteUserViewModel
import davidakht.githubdao.viewmodel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: ListFavoriteUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "My Favorite GitHub Users"
        adapter = ListFavoriteUserAdapter()
        val favoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)
        favoriteUserViewModel.getAllFavoriteUsers().observe(this, { favoriteUserList ->
            if (favoriteUserList != null) {
                adapter.setListFavoriteUsers(favoriteUserList)
            }
        })
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
        val pref =  SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
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
                Intent(this@FavoriteUserActivity, ListUsersActivity::class.java).also {
                    it.putExtra(ListUsersActivity.EXTRA_QUERY, query)
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
}