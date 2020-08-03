package club.geek66.downloader.rosi.common.path

import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:57
 * @copyright: Copyright 2020 by orange
 */
@Component
class RangeEntryPathGenerator : EntryPathGenerator {

	private val range: Int = 100

	override fun generate(basePath: String, entryId: Int): String {
		val rangeStart: Int = entryId / 100 * 100
		return Path.of(basePath, """$rangeStart-${rangeStart + 99}""", entryId.toString()).toString()
	}

}