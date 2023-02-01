package com.wo.orgs.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wo.orgs.R
import com.wo.orgs.database.AppDatabase
import com.wo.orgs.databinding.ActivityListProductBinding
import com.wo.orgs.ui.adapter.ListProductAdapter
import kotlinx.coroutines.*

class ListProductActivity : AppCompatActivity() {

    private lateinit var rvProduct: RecyclerView
    private val adapter = ListProductAdapter(this)
    private val binding by lazy {
        ActivityListProductBinding.inflate(layoutInflater)
    }
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupFab()
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.getInstance(this)
        val productDao = db.productDao()

        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Toast.makeText(
                this@ListProductActivity,
                "onResume $throwable",
                Toast.LENGTH_SHORT
            ).show()
        }
        // using coroutines
        scope.launch(handler) {
            MainScope().launch(handler) {
                throw Exception("lançando exception na coroutine em outro scope")
            }
            throw IllegalStateException("lançando exception na coroutine")
            val products = withContext(Dispatchers.IO) {
                productDao.getAllProducts()
            }
            adapter.updateList(products)
        }
    }

    private fun setupFab() {
        val fab = binding.fab
        fab.setOnClickListener {
            goToFormActivity()
        }
    }

    private fun goToFormActivity() {
        startActivity(Intent(this, FormProductActivity::class.java))
    }

    private fun setupRecyclerView() {
        rvProduct = binding.rvProduct
        rvProduct.adapter = adapter
        rvProduct.layoutManager = LinearLayoutManager(this)

        adapter.whenItemClickListener = { product ->
            val intent = Intent(
                this,
                DetailActivity::class.java
            ).apply {
                putExtra(PRODUCT_KEY_ID, product.id)
            }
            startActivity(intent)
        }
    }
}