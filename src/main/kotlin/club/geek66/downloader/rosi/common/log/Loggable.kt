package club.geek66.downloader.rosi.common.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @author: orange
 * @date: 2020/7/28
 * @time: 下午9:15
 * @copyright: Copyright 2020 by orange
 */
interface Loggable {

	val logger: Logger
		get() = LoggerFactory.getLogger(this::class.java)

}