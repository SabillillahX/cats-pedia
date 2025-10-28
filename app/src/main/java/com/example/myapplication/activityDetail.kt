package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.example.myapplication.data.Cats
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.abs

class activityDetail : AppCompatActivity() {

    private var currentCat: Cats? = null

    // Views tambahan
    private lateinit var tvCategory: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvLikes: TextView
    private lateinit var tvFeatured: TextView
    private lateinit var tvViews: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        val root = findViewById<View>(R.id.detail_page)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = sb.top, bottom = sb.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.top_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cat: Cats? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MainActivity.EXTRA_CATS, Cats::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(MainActivity.EXTRA_CATS)
        }
        currentCat = cat

        val iv = findViewById<ImageView>(R.id.iv_detail_photo)
        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvDesc = findViewById<TextView>(R.id.tv_detail_desc)

        tvCategory = findViewById(R.id.tv_detail_category)
        tvRating = findViewById(R.id.tv_detail_rating)
        tvLikes = findViewById(R.id.tv_detail_likes)
        tvFeatured = findViewById(R.id.tv_detail_featured)
        tvViews = findViewById(R.id.tv_detail_views)

        cat?.let {
            iv.setImageResource(it.photo)
            tvName.text = it.name
            tvDesc.text = it.description
            supportActionBar?.title = it.name

            val seed = abs(it.name.hashCode())
            val categories = listOf("Domestic", "Persian", "Siamese", "Maine Coon", "Bengal")
            val category = categories[seed % categories.size]
            val rating = 3.0f + (seed % 20) / 10f
            val likes = 50 + (seed % 950)
            val featured = (seed % 2 == 0)
            val views = 500 + (seed % 5000)

            tvCategory.text = "Kategori: $category"
            tvRating.text = "Rating: ${"%.1f".format(rating)}"
            tvLikes.text = "Likes: $likes"
            tvFeatured.text = "Featured: ${if (featured) "Ya" else "Tidak"}"
            tvViews.text = "Views: $views"
            // ====== /Data tambahan ======
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareCurrentCat()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareCurrentCat() {
        val c = currentCat ?: return
        val shareText = buildString {
            appendLine(c.name)
            appendLine()
            appendLine(c.description)
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, c.name)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(intent, "Bagikan via"))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
