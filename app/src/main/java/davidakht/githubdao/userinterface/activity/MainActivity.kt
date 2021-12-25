package davidakht.githubdao.userinterface.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.switchmaterial.SwitchMaterial
import davidakht.githubdao.R
import davidakht.githubdao.databinding.ActivityMainBinding
import davidakht.githubdao.datastore.SettingPreferences
import davidakht.githubdao.userinterface.fragment.MenuFragment
import davidakht.githubdao.viewmodel.MainViewModel
import davidakht.githubdao.viewmodel.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyUser()
        val btnMyGitHub: Button = findViewById(R.id.btnMyGitHub)
        val btnShowFavorite: Button = findViewById(R.id.btnShowfavorite)
        btnMyGitHub.setOnClickListener(this)
        btnShowFavorite.setOnClickListener(this)
        val mainViewModel = obtainViewModel(this@MainActivity)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)
        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun getMyUser() {
        binding.apply {
            Glide.with(this@MainActivity)
                .load("https://avatars.githubusercontent.com/u/94115632?v=4")
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(myAvatar)
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
                Intent(this@MainActivity, ListUsersActivity::class.java).also {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnMyGitHub -> {
                val myGitHubIntent = Intent(this@MainActivity, ListUsersActivity::class.java)
                myGitHubIntent.putExtra(ListUsersActivity.EXTRA_QUERY, "davidakht")
                startActivity(myGitHubIntent)
            }
            R.id.btnShowfavorite -> {
                val myFavoriteIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(myFavoriteIntent)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }
}