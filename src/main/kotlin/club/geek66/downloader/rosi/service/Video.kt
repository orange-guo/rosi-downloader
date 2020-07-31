package club.geek66.downloader.rosi.service

import club.geek66.downloader.rosi.client.RosiClient
import club.geek66.downloader.rosi.client.pojo.VideoNoEntry
import club.geek66.downloader.rosi.common.AbstractLoggable
import club.geek66.downloader.rosi.common.IdGetter
import club.geek66.downloader.rosi.common.Pager
import club.geek66.downloader.rosi.common.RosiProperties
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.stereotype.Service
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * @author: orange
 * @date: 2020/7/24
 * @time: 下午9:39
 * @copyright: Copyright 2020 by orange
 */
@Entity
class VideoNoDomain : IdGetter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "domainIdentityGenerator")
	@GenericGenerator(name = "domainIdentityGenerator", strategy = "club.geek66.downloader.rosi.common.DomainIdentityGenerator")
	override var id: Int = -1

	lateinit var coverUrl: String

	lateinit var contentUrl: String

}

/**
 * @author: orange
 * @date: 2020/7/27
 * @time: 下午10:22
 * @copyright: Copyright 2020 by orange
 */
interface VideoNoRepository : JpaRepository<VideoNoDomain, Int>

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
) : AbstractLoggable() {

	fun pullAll() {
		val token: String = client.login(username = properties.username, password = properties.password)
		Pager.doByDesc(getPage = { client.getVideoNoPage(pageNo = it, token = token) }, consumePage = {
			it.content.parallelStream().map(VideoNoEntry::convert).filter { !repository.existsById(it.id) }
				.forEach { logger.info("Save " + it.id) }
			true
		})
	}

}

@ShellComponent
class NoVideoShellComponent {

	@ShellMethod(key = [], value = "132132")
	fun downloadAll() = notSupport()

	@ShellMethod(key = [], value = "132312312")
	fun downloadById() = notSupport()

	@ShellMethod(key = [], value = "132312312")
	fun pullAll() = notSupport()

	@ShellMethod(key = [], value = "2352354")
	fun fastPull() = notSupport()

	private fun notSupport() = print("Not support")

}