package club.geek66.downloader.rosi.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 21:44
 * @copyright: Copyright 2019 by 橙子
 */
@Entity
class NoDomain : IdGetter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "customGenerator")
	@GenericGenerator(name = "customGenerator", strategy = "club.geek66.downloader.rosi.domain.DomainIdentityGenerator")
	override var id: Int = -1

	@Enumerated(EnumType.STRING)
	lateinit var type: RosiEntryType

	lateinit var coverUrl: String

	// http://rs.jinyemimi.com/jpg/3154-tBxfldMb
	lateinit var urlPrefix: String

	var quantity: Int = -1

	var downloaded: Boolean = false

}