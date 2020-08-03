package club.geek66.downloader.rosi.service.setting

import club.geek66.downloader.rosi.service.setting.service.SettingService
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:26
 * @copyright: Copyright 2020 by orange
 */
@ShellComponent
class SettingShellComponent(private val service: SettingService) {

	@ShellMethod(key = ["set-home-directory"], value = "Set downloader home directory.")
	fun setHomeDirectory(homeDirectory: String) = service.setHomeDirectory(homeDirectory)

}