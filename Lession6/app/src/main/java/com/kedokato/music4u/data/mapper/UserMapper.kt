package com.kedokato.music4u.data.mapper

import com.kedokato.music4u.data.local.database.Entity.UserEntity
import com.kedokato.music4u.domain.model.User
import androidx.core.net.toUri


object UserMapper {

    fun toEntity(user: User): UserEntity {
        return UserEntity(
            userId = user.userId,
            username = user.username,
            password = user.password,
            avatar = user.avatarUri?.toString(),
            name = user.name,
            phone = user.phone,
            email = user.email,
            university = user.university,
            description = user.description
        )
    }

    fun toDomain(userEntity: UserEntity): User {
        return User(
            userId = userEntity.userId,
            username = userEntity.username,
            password = userEntity.password,
            avatarUri = userEntity.avatar?.toUri(),
            name = userEntity.name ?: "",
            phone = userEntity.phone,
            email = userEntity.email,
            university = userEntity.university,
            description = userEntity.description
        )
    }


}


