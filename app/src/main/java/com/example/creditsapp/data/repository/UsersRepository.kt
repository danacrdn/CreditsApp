package com.example.creditsapp.data.repository

import com.example.creditsapp.data.database.User
import com.example.creditsapp.data.database.UserDao
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUserStream(id: Int): Flow<User>

    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun deleteUser(user: User)
}

class DefaultUsersRepository(private val userDao: UserDao) : UsersRepository {
    override fun getUserStream(id: Int): Flow<User> = userDao.getUser(id)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

}