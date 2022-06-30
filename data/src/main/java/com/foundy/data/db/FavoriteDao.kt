package com.foundy.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.foundy.data.model.FAVORITE_NOTICE_TABLE_NAME
import com.foundy.data.model.FavoriteNoticeEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM $FAVORITE_NOTICE_TABLE_NAME")
    suspend fun getAll(): List<FavoriteNoticeEntity>

    @Insert
    suspend fun insert(entity: FavoriteNoticeEntity)

    @Delete
    suspend fun delete(entity: FavoriteNoticeEntity)
}