package com.wo.orgs.database.dao

import android.service.autofill.OnClickAction
import androidx.room.*
import com.wo.orgs.model.Product

@Dao
interface ProductDAO {

    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<Product>

//    @Insert
//    fun saveProduct(vararg product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(vararg product: Product)

    @Delete
    fun delete(product: Product)

//    @Update
//    fun updateProduct(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun getById(id: Long): Product?

}