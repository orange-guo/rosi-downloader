package club.geek66.downloader.rosi.service.no.service

import club.geek66.downloader.rosi.service.no.repository.NoRepository
import org.springframework.stereotype.Service

/**
 * @author: orange
 * @date: 2020/7/29
 * @time: 下午11:30
 * @copyright: Copyright 2020 by orange
 */
@Service
class NoService(
		private val downloadService: NoDownloadService,
		private val pullService: NoPullService,
		private val repository: NoRepository
) {

	fun fastPull() {
		pullService.fastPull()
	}

	fun pullAll() {
		pullService.pullAll()
	}

	fun download(id: Int) {
		downloadService.downloadEntry(id)
	}

	fun downloadAll() {
		downloadService.downloadAll()
	}

	fun deletePulledRecord() {
		repository.deleteAll()
	}

}