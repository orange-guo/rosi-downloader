package club.geek66.downloader.rosi.common.path

import org.springframework.stereotype.Component

/**
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:57
 * @copyright: Copyright 2020 by orange
 */
@Component
class RangedIdGroupNameGenerator : IdGroupNameGenerator {

	private val range: Long = 100

	override fun generate(id: Long): String {
		val rangeStart: Long = id / range * range
		val rangeEnd: Long = rangeStart + range - 1
		return """$rangeStart-$rangeEnd"""
	}

}