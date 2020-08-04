package club.geek66.downloader.rosi.common.path

import org.junit.Assert
import org.junit.Test

/**
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午11:32
 * @copyright: Copyright 2020 by orange
 */
class RangedIdGroupNameGeneratorTest {

	@Test
	fun test() =
			RangedIdGroupNameGenerator().also {
				Assert.assertEquals("0-99", it.generate(99))
			}.also {
				Assert.assertEquals("300-399", it.generate(333))
			}.also {
				Assert.assertEquals("1000-1099", it.generate(1024))
			}.let {
				Assert.assertEquals("0-99", it.generate(0))
			}

}