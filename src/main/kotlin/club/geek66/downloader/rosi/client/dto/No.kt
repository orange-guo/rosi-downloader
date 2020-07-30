package club.geek66.downloader.rosi.client.dto

import club.geek66.downloader.rosi.domain.NoDomain
import club.geek66.downloader.rosi.domain.RosiEntryType
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang.StringUtils

/**
 * @author: orange
 * @date: 2020/7/18
 * @time: 下午1:56
 * @copyright: Copyright 2020 by orange
 */
data class NoEntry(
	// NO.3148
	val title: String,
	// http://2020.jinyemimi.com/2020/3147.rosi
	@JsonProperty("litpic")
	val coverUrl: String,
	// http://rs.jinyemimi.com/jpg/
	@JsonProperty("cdn")
	val baseUrl: String,
	// 3147-JFgKoBeg
	@JsonProperty("shorttitle")
	val subPath: String,
	// 41
	@JsonProperty("source")
	val quantity: String
) {

	fun convertToDomain(): NoDomain {
		return NoDomain().also {
			it.id = StringUtils.substringAfter(this.title, "NO.").toInt()
			it.type = RosiEntryType.NO
			it.coverUrl = this.coverUrl
			it.urlPrefix = this.baseUrl + this.subPath
			it.quantity = this.quantity.toInt()
		}
	}

}