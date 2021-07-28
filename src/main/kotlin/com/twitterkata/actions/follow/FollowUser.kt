package com.twitterkata.actions.follow

import com.twitterkata.actions.user_account.enums.Messages
import com.twitterkata.actions.user_account.enums.Status
import com.twitterkata.infraestructure.repositories.FollowerRepository
import com.twitterkata.infraestructure.repositories.UserRepository
import com.twitterkata.model.ResponseResult
import com.twitterkata.model.User

class FollowUser (private val userRepository: UserRepository, private val followerRepository: FollowerRepository) {
    operator fun invoke(nickname: String): List<User> {
        return getFollowers(nickname)
    }

    fun getFollowers(nickname: String): List<User> {
        val user = userRepository.get(nickname) ?: return listOf()
        return followerRepository.getFollowersOfUser(user)
    }

    fun followUser(nickname: String, nicknameToFollow: String): ResponseResult {
        val followerUser = userRepository.get(nickname)
        val userToFollow = userRepository.get(nicknameToFollow)
        return when {
            followerUser != null && userToFollow != null -> {
                followerRepository.addFollower(userToFollow, followerUser)
                ResponseResult(Status.OK, Messages.OK)
            }
            else -> {
                ResponseResult(Status.FAIL, Messages.INEXISTENT_USER)
            }
        }
    }
}