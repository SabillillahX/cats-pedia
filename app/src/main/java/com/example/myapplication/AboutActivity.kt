package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.google.android.material.appbar.MaterialToolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        val root = findViewById<View>(R.id.about_page)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = sb.top, bottom = sb.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.top_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.about_me)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val imgProfile = findViewById<ImageView>(R.id.img_profile)

        val screenW = resources.displayMetrics.widthPixels
        val targetW = (screenW - dpToPx(40f))
        val targetH = dpToPx(300f)

        Glide.with(this)
            .load(R.drawable.my_profile_picture)
            .override(targetW, targetH)
            .centerCrop()
            .format(DecodeFormat.PREFER_RGB_565)
            .into(imgProfile)
    }

    private fun dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}