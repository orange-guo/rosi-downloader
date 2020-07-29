package club.geek66.downloader.rosi.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author: 橙子
 * @date: 2019/12/11
 * @time: 22:48
 * @copyright: Copyright 2019 by 橙子
 */
data class LoginRequest(
		val controller: String = "member",

		val action: String = "userLogin",

		val userId: String,

		val password: String
)

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午11:10
 * @copyright: Copyright 2020 by orange
 */
data class LoginResponse(
		@JsonProperty("mid")
		val id: Int,

		val token: String
)