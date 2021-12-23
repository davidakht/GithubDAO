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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import davidakht.githubdao.R
import davidakht.githubdao.adapter.SectionPagerAdapter
import davidakht.githubdao.databinding.ActivityDetailFavoriteUserBinding
import davidakht.githubdao.datastore.SettingPreferences
import davidakht.githubdao.userinterface.fragment.MenuFragment
import davidakht.githubdao.viewmodel.DetailFavoriteUserViewModel
import davidakht.githubdao.viewmodel.ViewModelFactory

class DetailFavoriteUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityDetailFavoriteUserBinding
    private lateinit var viewModel: DetailFavoriteUserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail Favorite User"
        binding.progressBar.visibility = View.VISIBLE
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        viewModel = obtainViewModel(this@DetailFavoriteUserActivity)
        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                binding.progressBar.visibility = View.GONE
                binding.apply {
                    Glide.with(this@DetailFavoriteUserActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(imgAvatar)
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvLocation.text = it.location
                    tvCompany.text = it.company
                    tvRepository.text = getString(R.string.repository, it.public_repos)
                    tvFollowing.text = getString(R.string.following, it.following.toString())
                    tvFollowers.text = getString(R.string.followers, it.followers.toString())
                }
            }
        })
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
        binding.fabRemove.setOnClickListener {
            viewModel.removeFromFavorite(id)
            val myFavoriteIntent =
                Intent(this@DetailFavoriteUserActivity, FavoriteUserActivity::class.java)
            startActivity(myFavoriteIntent)
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
                Intent(this@DetailFavoriteUserActivity, ListUsersActivity::class.java).also {
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

    private fun obtainViewModel(activity: AppCompatActivity): DetailFavoriteUserViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
        val pref =  SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        return ViewModelProvider(activity, factory).get(DetailFavoriteUserViewModel::class.java)
    }
}