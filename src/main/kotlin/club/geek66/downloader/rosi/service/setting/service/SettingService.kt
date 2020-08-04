package club.geek66.downloader.rosi.service.setting.service

import club.geek66.downloader.rosi.service.setting.domain.SettingDomain
import club.geek66.downloader.rosi.service.setting.exception.SettingException
import club.geek66.downloader.rosi.service.setting.repository.SettingRepository
import io.vavr.control.Option
import org.springframework.data.domain.PageRequest
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
					getSetting().orElse {
						Option.of(SettingDomain())
					}.map {
						it.copy(homeDirectory = path)
					}
				}
				.map { repository.save(it) }
				.getOrElseThrow { SettingException("Illegal path or no permission(read|write)") }
	}

	fun getHomeDirectory(): String =
			getSetting()
					.map(SettingDomain::homeDirectory::get)
					.filter(StringUtils::hasText)
					.getOrElseThrow { SettingException("Home directory not set") }

	fun getSetting() = PageRequest.of(0, 1).let { repository.findAll(it) }.stream().findFirst().let { Option.ofOptional(it) }

}