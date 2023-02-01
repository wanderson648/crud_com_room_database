package com.wo.orgs.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.wo.orgs.R
import com.wo.orgs.database.AppDatabase
import com.wo.orgs.databinding.ActivityFormProductBinding
import com.wo.orgs.dialog.FormImageDialog
import com.wo.orgs.extensions.tryRefreshImage
import com.wo.orgs.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class FormProductActivity : AppCompatActivity() {

    private var productId: Long = 0L
    private val binding by lazy {
        ActivityFormProductBinding.inflate(layoutInflater)
    }
    private val productDao by lazy {
        AppDatabase.getInstance(this).productDao()
    }
    private var url: String? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupButtonSave()
        title = getString(R.string.save_product)

        binding.imgProductForm.setOnClickListener {
            FormImageDialog(this)
                .showDialog(url) { img ->
                    url = img
                    binding.imgProductForm.tryRefreshImage(url)
                }
        }
        tryToLoadProduct()
    }

    override fun onResume() {
        super.onResume()
        tryGetProduct()
    }


    private fun tryGetProduct() {
        // using coroutines
        scope.launch {
            productDao.getById(productId)?.let {
               withContext(Dispatchers.Main) {
                   title = getString(R.string.update_product)
                   fillFields(it)
               }
            }
        }
    }

    private fun tryToLoadProduct() {
        productId = intent.getLongExtra(PRODUCT_KEY_ID, 0L)
    }

    private fun fillFields(product: Product) {
        url = product.image
        binding.apply {
            imgProductForm.tryRefreshImage(product.image)
            txtEditTitle.setText(product.name)
            txtEditDesc.setText(product.description)
            txtEditAmount.setText(product.amount.toPlainString())
        }
    }


    private fun setupButtonSave() {
        binding.apply {
            val name = txtEditTitle
            val desc = txtEditDesc
            val amount = txtEditAmount
            val btnSave = btnSave

            btnSave.setOnClickListener {
                scope.launch {
                    productDao.saveProduct(saveOrUpdateProduct(name, desc, amount))
                    finish()
                }
            }
        }
    }

    private fun saveOrUpdateProduct(
        editName: TextInputEditText,
        editDesc: TextInputEditText,
        editAmount: TextInputEditText
    ): Product {
        val name = editName.text.toString()
        val desc = editDesc.text.toString()
        val txtAmount = editAmount.text.toString()
        val amount = if (txtAmount.isBlank()) BigDecimal.ZERO else BigDecimal(txtAmount)

        return Product(
            id = productId,
            name = name,
            description = desc,
            amount = amount,
            image = url
        )
    }
}