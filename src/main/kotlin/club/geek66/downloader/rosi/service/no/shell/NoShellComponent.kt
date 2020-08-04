package club.geek66.downloader.rosi.service.no.shell

import club.geek66.downloader.rosi.service.no.service.NoService
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

/**
 *
 * @author: orange
 * @date: 2020/7/29
 * @time: 下午11:50
 * @copyright: Copyright 2020 by orange
 */
@ShellComponent(value = "Operation for Rosi-No.")
class NoShellComponent(private val service: NoService) {

	@ShellMethod(value = "Form remote server pull all record of Rosi-No.")
	fun pullAllNo() = service.pullAll()

	@ShellMethod(value = "From remote server fast pull latest record of Rosi-No.")
	fun fastPullNo() = service.fastPull()

	@ShellMethod(value = "Download all content of Rosi-No.(Must local has all records can do it)")
	fun downloadAllNo() = service.downloadAll()

	@ShellMethod(value = "Download content of Rosi-No by specific id(Must local has specific records can do it)")
	fun downloadNo(id: Int) = service.download(id)

	@ShellMethod(value = "Clear local pulled record of Rosi-No.(Not download content)")
	fun deletePulledNoRecord() = service.deletePulledRecord()

}