package club.geek66.downloader.rosi.service.video.service

import club.geek66.downloader.rosi.client.RosiClient
import club.geek66.downloader.rosi.client.pojo.VideoNoDetailEntry
import club.geek66.downloader.rosi.common.Pager
import club.geek66.downloader.rosi.common.RosiProperties
import club.geek66.downloader.rosi.common.log.Loggable
import club.geek66.downloader.rosi.service.video.repository.VideoNoRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午10:49
 * @copyright: Copyright 2020 by orange
 */
@Service
class VideoNoService(
		private val client: RosiClient,
		private val repository: VideoNoRepository,
		private val properties: RosiProperties
) : Loggable {

	fun pullAll() {
		val token: String = client.login(username = properties.username, password = properties.password)
		Pager.doByDesc(getPage = { client.getVideoNoPage(pageNo = it, token = token) }, consumePage = {
			it.content.parallelStream().map(VideoNoDetailEntry::convert).filter { !repository.existsById(it.id) }
					.forEach {
						logger.info("Save " + it.id)
						repository.save(it)
					}
			true
		})
	}

	fun printAllUrl(): Unit {
		repository.findAll(Sort.by("id").descending()).map { it.contentUrl }.forEach(::println)
	}

}