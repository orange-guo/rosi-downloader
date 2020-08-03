package club.geek66.downloader.rosi.client.pojo

import club.geek66.downloader.rosi.service.no.domain.NoDomain
import club.geek66.downloader.rosi.service.no.domain.NoType
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
			it.type = NoType.NO
			it.coverUrl = this.coverUrl
			it.urlPrefix = this.baseUrl + this.subPath
			it.quantity = this.quantity.toInt()
		}
	}

}