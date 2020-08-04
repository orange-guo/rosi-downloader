package club.geek66.downloader.rosi.service.setting.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:29
 * @copyright: Copyright 2020 by orange
 */
@Entity
class SettingDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Int? = null

	lateinit var homeDirectory: String

	fun copy(id: Int? = this.id, homeDirectory: String = this.homeDirectory) = SettingDomain().also {
		it.id = id
		it.homeDirectory = homeDirectory
	}

}