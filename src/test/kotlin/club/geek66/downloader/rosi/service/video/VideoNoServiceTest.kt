package club.geek66.downloader.rosi.service.video

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午11:15
 * @copyright: Copyright 2020 by orange
 */
@SpringBootTest
@RunWith(SpringRunner::class)
class VideoNoServiceTest {

	@Autowired
	private val service: VideoNoService? = null

	@Test
	fun pullAll() {
		service!!.pullAll()
	}
}