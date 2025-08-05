package com.kedokato.lession6.data.local.database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kedokato.lession6.data.local.database.Entity.UserEntity

@Dao
interface UserDAO {
     @Insert
     suspend fun insertUser(user: UserEntity): Long

     @Query("SELECT * FROM users WHERE userId = :userId")
     suspend fun getUserById(userId: Long): UserEntity

     @Query(
          """
            UPDATE users
            SET fullName = :name, phone = :phone, university = :university, description = :description, avatarUri = :avatarUrl
            WHERE userId = :userId
     """
     )
     suspend fun updateUser(
          userId: Long,
          name: String,
          phone: String,
          university: String,
          description: String,
          avatarUrl: String?
     ): Int

     @Delete
     suspend fun deleteUser(user: UserEntity)

     @Query("SELECT * FROM users WHERE username = :username AND password = :password")
     suspend fun login(username: String, password: String): UserEntity?
}