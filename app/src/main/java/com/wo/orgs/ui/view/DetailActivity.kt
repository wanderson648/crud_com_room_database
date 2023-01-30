package com.wo.orgs.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wo.orgs.R
import com.wo.orgs.database.AppDatabase
import com.wo.orgs.databinding.ActivityDetailBinding
import com.wo.orgs.extensions.formatterToCoinBrazil
import com.wo.orgs.extensions.tryRefreshImage
import com.wo.orgs.model.Product

class DetailActivity : AppCompatActivity() {

    private var product: Product? = null
    private var productId: Long = 0L

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val productDao by lazy {
        AppDatabase.getInstance(this).productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tryLoadProduct()
    }

    override fun onResume() {
        super.onResume()
        getProduct()
    }

    private fun getProduct() {
        product = productDao.getById(productId)
        product?.let {
            fillFields(it)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details_product, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                product?.let { productDao.delete(it) }
                finish()
            }
            R.id.menu_edit -> {
                Intent(this, FormProductActivity::class.java).apply {
                    putExtra(PRODUCT_KEY_ID, productId)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryLoadProduct() {
        productId = intent.getLongExtra(PRODUCT_KEY_ID, 0L)
    }

    private fun fillFields(productLoaded: Product) {
        with(binding) {
            imgDetail.tryRefreshImage(productLoaded.image)
            detailTitle.text = productLoaded.name
            detailDesc.text = productLoaded.description
            detailAmount.text =
                productLoaded.amount.formatterToCoinBrazil()
        }
    }
}