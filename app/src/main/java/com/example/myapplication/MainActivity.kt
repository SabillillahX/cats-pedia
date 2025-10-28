package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Cats
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CATS = "extra_cats"
        const val REQUEST_CODE_ABOUT = 100
    }

    private lateinit var rvCats: RecyclerView
    private val list = ArrayList<Cats>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        val root = findViewById<View>(R.id.main_page)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = sb.top, bottom = sb.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.top_bar)
        setSupportActionBar(toolbar)

        rvCats = findViewById(R.id.rv_cats)
        rvCats.setHasFixedSize(true)

        list.addAll(getListCats())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("Recycle")
    private fun getListCats(): ArrayList<Cats> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)

        val size = minOf(dataName.size, dataDescription.size, dataPhoto.length())
        val listCats = ArrayList<Cats>(size)
        for (i in 0 until size) {
            listCats.add(Cats(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1)))
        }
        return listCats
    }

    private fun showRecyclerList() {
        rvCats.layoutManager = LinearLayoutManager(this)
        val adapter = ListCatsAdapter(list)
        rvCats.adapter = adapter
        adapter.setOnItemClickCallback(object : ListCatsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Cats) {
                val intent = Intent(this@MainActivity, activityDetail::class.java)
                intent.putExtra(EXTRA_CATS, data)
                startActivity(intent)
            }
        })
    }
}