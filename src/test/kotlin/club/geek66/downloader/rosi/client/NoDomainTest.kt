package club.geek66.downloader.rosi.client

import club.geek66.downloader.rosi.service.no.NoDownloadService
import club.geek66.downloader.rosi.service.no.NoPullService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 22:50
 * @copyright: Copyright 2019 by 橙子
 */
@SpringBootTest
@Rollback(false)
@RunWith(SpringRunner::class)
class NoDomainTest {
	@Autowired
	lateinit var pullService: NoPullService

	@Autowired
	lateinit var downloadService: NoDownloadService

	@Test
	fun pullAll() {
		pullService.pullAll()
	}

	@Test
	fun fastPull() {
		pullService.fastPull()
	}

	@Test
	fun testDownloadRosi() {
		downloadService.downloadAll()
	}
}