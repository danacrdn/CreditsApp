package com.example.creditsapp.data.repository

import com.example.creditsapp.data.database.User
import com.example.creditsapp.data.database.UserDao
import com.example.creditsapp.domain.model.UserTotalCredits
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUserByEmailStream(email: String): Flow<User>

    fun getUserStream(id: Int): Flow<User>

    fun getUserAndCredits(id: Int): Flow<UserTotalCredits>

    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun deleteUser(user: User)
}

class DefaultUsersRepository(private val userDao: UserDao) : UsersRepository {
    override fun getUserByEmailStream(email: String): Flow<User> = userDao.getUserByEmail(email)

    override fun getUserStream(id: Int): Flow<User> = userDao.getUser(id)

    override fun getUserAndCredits(id: Int): Flow<UserTotalCredits> = userDao.getUserAndCredits(id)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

}