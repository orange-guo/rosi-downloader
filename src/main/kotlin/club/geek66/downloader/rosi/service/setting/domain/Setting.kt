package club.geek66.downloader.rosi.service.setting.domain

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:29
 * @copyright: Copyright 2020 by orange
 */
class Setting {

	var id: Int? = null

	lateinit var homeDirectory: String

	fun copy(id: Int? = this.id, homeDirectory: String = this.homeDirectory) = Setting().also {
		it.id = id
		it.homeDirectory = homeDirectory
	}

}