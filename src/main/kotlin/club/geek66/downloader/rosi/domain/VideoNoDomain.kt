package club.geek66.downloader.rosi.domain

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
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "customGenerator")
	@GenericGenerator(name = "customGenerator", strategy = "club.geek66.downloader.rosi.domain.DomainIdentityGenerator")
	override var id: Int = -1

	lateinit var coverUrl: String

	lateinit var contentUrl: String

}