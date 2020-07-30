package club.geek66.downloader.rosi.client.dto

import club.geek66.downloader.rosi.domain.VideoNoDomain
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang.StringUtils

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午9:56
 * @copyright: Copyright 2020 by orange
 */
class VideoNoEntry(
	val id: Int,
	val title: String,
	@JsonProperty("litpic") val coverUrl: String,
	@JsonProperty("softlinks") val videoUrl: String
) {

	fun convert(): VideoNoDomain {
		val index: String = StringUtils.substringAfter(this.title, "VIDEO.NO.")
		return VideoNoDomain().also {
			it.id = index.toInt()
			it.contentUrl = this.videoUrl
			it.coverUrl = this.coverUrl
		}
	}
}