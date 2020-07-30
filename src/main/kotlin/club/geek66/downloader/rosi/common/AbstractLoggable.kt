package club.geek66.downloader.rosi.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @author: orange
 * @date: 2020/7/28
 * @time: 下午9:15
 * @copyright: Copyright 2020 by orange
 */
abstract class AbstractLoggable : Loggable {

	val logger: Logger = LoggerFactory.getLogger(this::class.java)
}