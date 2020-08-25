package club.geek66.downloader.rosi.service.video.shell

import club.geek66.downloader.rosi.service.video.service.VideoNoService
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:22
 * @copyright: Copyright 2020 by orange
 */
@ShellComponent
class VideoNoShellComponent(
		private val videoNoService: VideoNoService
) {

	@ShellMethod(key = [], value = "132132")
	fun downloadAll(): Nothing = TODO("Not support")

	@ShellMethod(key = [], value = "132312312")
	fun downloadById(): Nothing = TODO("Not support")

	@ShellMethod(key = [], value = "132312312")
	fun pullAll(): Unit = videoNoService.pullAll()

	@ShellMethod(key = [], value = "Print all video no url")
	fun printAllUrl():Unit = videoNoService.printAllUrl()

	@ShellMethod(key = [], value = "2352354")
	fun fastPull(): Nothing = TODO("Not support")

}