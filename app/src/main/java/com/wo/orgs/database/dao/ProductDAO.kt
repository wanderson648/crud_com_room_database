package com.wo.orgs.database.dao

import androidx.room.*
import com.wo.orgs.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {

    @Query("SELECT * FROM Product")
    fun getAllProducts(): Flow<List<Product>>

//    @Insert
//    fun saveProduct(vararg product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(vararg product: Product)

    @Delete
    suspend fun delete(product: Product)

//    @Update
//    fun updateProduct(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    suspend fun getById(id: Long): Product?

}