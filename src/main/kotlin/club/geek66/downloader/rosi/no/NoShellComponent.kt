package club.geek66.downloader.rosi.no

import club.geek66.downloader.rosi.service.no.NoService
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

/**
 *
 * @author: orange
 * @date: 2020/7/29
 * @time: 下午11:50
 * @copyright: Copyright 2020 by orange
 */
@ShellComponent
class NoShellComponent(private val service: NoService) {
	@ShellMethod(key = ["no-pull-all"], value = "Form remote server pull all Rosi-No.")
	fun pullAll() = service.pullAll()

	@ShellMethod(key = ["no-download-all"], value = "From remote server download all Rosi-No.")
	fun downloadAll() = service.downloadAll()
}