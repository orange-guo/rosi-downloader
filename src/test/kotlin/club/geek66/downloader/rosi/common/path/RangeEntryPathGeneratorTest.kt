package club.geek66.downloader.rosi.common.path

import org.junit.Assert
import org.junit.Test

/**
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午11:32
 * @copyright: Copyright 2020 by orange
 */
class RangeEntryPathGeneratorTest {

	@Test
	fun generate() {
		val generator = RangeEntryPathGenerator()
		var generatedPath: String = generator.generate("/home/test", 99)
		Assert.assertEquals("/home/test/0-99/99", generatedPath)

		generatedPath = generator.generate("/home/test", 333)
		Assert.assertEquals("/home/test/300-399/333", generatedPath)

		generatedPath = generator.generate("/home/test", 1024)
		Assert.assertEquals("/home/test/1000-1099/1024", generatedPath)
	}

}