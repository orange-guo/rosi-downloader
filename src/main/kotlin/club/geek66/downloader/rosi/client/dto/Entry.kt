package club.geek66.downloader.rosi.client.dto

import club.geek66.downloader.rosi.common.Page
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午9:42
 * @copyright: Copyright 2020 by orange
 */
var TYPE_ALL = "t"
var TYPE_VIDEO = "r"

data class EntryPageRequest(
		val controller: String = "archives",

		val action: String = "Sort",

		val page: Int = 1,

		val type: String = "r",

		val bid: Int = 1
)

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午9:55
 * @copyright: Copyright 2020 by orange
 */
data class EntryPageResponse<T> (
		@JsonProperty("current_page")
		override val currentPage: Int,

		@JsonProperty("total_page")
		override val totalPage: Int,

		@JsonProperty("entries_s")
		override val content: Set<T>
): Page<T>

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午10:10
 * @copyright: Copyright 2020 by orange
 */
data class EntryRequest(
		val controller: String = "archives",

		val action: String = "getArchiveD",

		val token: String? = null,

		val aid: Int? = null
)