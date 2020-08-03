package club.geek66.downloader.rosi.service.setting.service

import club.geek66.downloader.rosi.service.setting.domain.Setting
import club.geek66.downloader.rosi.service.setting.exception.SettingException
import club.geek66.downloader.rosi.service.setting.repository.SettingRepository
import io.vavr.control.Option
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.nio.file.Files
import java.nio.file.Path

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:47
 * @copyright: Copyright 2020 by orange
 */
@Service
class SettingService(private val repository: SettingRepository) {

	fun setHomeDirectory(homeDirectory: String) {
		Option.of(Path.of(homeDirectory))
				.filter {
					Files.exists(it)
							&& Files.isDirectory(it)
							&& Files.isReadable(it)
							&& Files.isWritable(it)
				}
				.map(Path::toAbsolutePath)
				.map(Path::toString)
				.flatMap { path ->
					repository.findTop1().orElse {
						Option.of(Setting())
					}.map {
						it.copy(homeDirectory = path)
					}
				}
				.map { repository.save(it) }
				.getOrElseThrow { SettingException("Illegal path or no permission(read|write)") }
	}

	fun getHomeDirectory(): String =
			repository.findTop1()
					.map(Setting::homeDirectory::get)
					.filter(StringUtils::hasText)
					.getOrElseThrow { SettingException("Home direcotry not set") }

}