package club.geek66.downloader.rosi.service.video.domain

import club.geek66.downloader.rosi.common.IdGetter
import org.hibernate.annotations.GenericGenerator
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