package club.geek66.downloader.rosi.client

import club.geek66.downloader.rosi.client.pojo.*
import club.geek66.downloader.rosi.common.log.Loggable
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang.StringUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 22:23
 * @copyright: Copyright 2019 by 橙子
 */
@Component
@Suppress(names = ["NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"])
class RosiClient(private val template: RestTemplate, private val mapper: ObjectMapper) : Loggable {

	fun login(username: String, password: String): String {
		return doPost(
			body = LoginRequest(userId = username, password = password),
			typeReference = object : TypeReference<LoginResponse>() {}
		).token
	}

	fun getNoPage(pageNo: Int): EntryPageResponse<NoEntry> {
		val pageResponse: EntryPageResponse<NoEntry> =
			doPost(body = EntryPageRequest(type = TYPE_ALL, page = pageNo),
					typeReference = object : TypeReference<EntryPageResponse<NoEntry>>() {})

		val content: Set<NoEntry> =
				pageResponse.content.stream()
						.filter { it.title.startsWith("NO.") }
						.filter { StringUtils.isNumeric(it.quantity) }
						.collect(Collectors.toSet())
		return pageResponse.copy(content = content)
	}

	fun getVideoNoPage(pageNo: Int, token: String): EntryPageResponse<VideoNoDetailEntry> {
		val summaryPage: EntryPageResponse<VideoNoSummary> =
				doPost(EntryPageRequest(type = TYPE_VIDEO, page = pageNo),
						object : TypeReference<EntryPageResponse<VideoNoSummary>>() {})

		val details: Set<VideoNoDetailEntry> = summaryPage.content.stream()
				.filter { it.title.startsWith("VIDEO.NO.") }
				.filter { StringUtils.isNumeric(StringUtils.substringAfter(it.title, "VIDEO.NO.")) }
				.map { getVideoNoDetail(it.id, token) }
				.collect(Collectors.toSet())

		return EntryPageResponse(
				currentPage = summaryPage.currentPage,
				content = details,
				totalPage = summaryPage.totalPage
		)
	}

	private fun getVideoNoDetail(id: Int, token: String): VideoNoDetailEntry {
		return doPost(EntryRequest(aid = id, token = token), object : TypeReference<VideoNoDetailEntry>() {})
	}

	private fun <T, R> doPost(body: T, typeReference: TypeReference<R>): R {
		(0..9).forEach { _ ->
			try {
				val httpHeaders = HttpHeaders()
				httpHeaders.contentType = MediaType.APPLICATION_JSON
				httpHeaders.set("User-Agent", "okhttp/3.14.4")
				val content: String = template.postForObject(
						"http://rosi.jinyemimi.com/api_beta1_3/api.php",
						HttpEntity(body, httpHeaders),
						String::class.java
				)
				return mapper.readValue(content, typeReference)
			} catch (notFound: HttpClientErrorException.NotFound) {
				throw notFound
			} catch (ignored: Exception) {
				TimeUnit.SECONDS.sleep(2)
			}
		}
		throw IllegalStateException("Try failure")
	}

}