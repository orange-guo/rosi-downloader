package club.geek66.downloader.rosi.common.path

import club.geek66.downloader.rosi.common.log.Loggable

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午11:01
 * @copyright: Copyright 2020 by orange
 */
interface IdGroupNameGenerator : Loggable {

	fun generate(id: Long): String

}