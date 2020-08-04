package club.geek66.downloader.rosi.service.no.service

import club.geek66.downloader.rosi.client.pojo.EntryPageResponse
import club.geek66.downloader.rosi.common.Pager
import club.geek66.downloader.rosi.common.log.Loggable
import club.geek66.downloader.rosi.common.path.IdGroupNameGenerator
import club.geek66.downloader.rosi.service.no.domain.NoDomain
import club.geek66.downloader.rosi.service.no.exception.NoException
import club.geek66.downloader.rosi.service.no.repository.NoRepository
import club.geek66.downloader.rosi.service.setting.service.SettingService
import io.vavr.control.Option
import io.vavr.control.Try
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import java.text.DecimalFormat
import java.util.stream.Collectors
import java.util.stream.IntStream

/**
 * @author: 橙子
 * @date: 2019/12/19
 * @time: 21:22
 * @copyright: Copyright 2019 by 橙子
 */
@Service
@Suppress(names = ["NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"])
class NoDownloadService(
		private val repository: NoRepository,
		private val template: RestTemplate,
		private val settingService: SettingService,
		private val groupNameGenerator: IdGroupNameGenerator
) : Loggable {

	private val formatter: DecimalFormat = DecimalFormat("000")

	fun downloadAll() {
		Pager.doByDesc(getPage = { currentPage: Int ->
			// jpa start from 0
			val jpaCurrentPage: Int = currentPage - 1
			PageRequest.of(jpaCurrentPage, 10, Sort.by("id")).let(repository::findAll).let {
				EntryPageResponse(
						currentPage = currentPage, totalPage = it.totalPages, content = it.content.toSet()
				)
			}
		}, consumePage = {
			logger.info(
					"Entry page {}/{} download start with entries {}",
					it.currentPage,
					it.totalPage,
					it.content.stream().map(NoDomain::id::get).collect(Collectors.toList()).toString()
			)
			it.content.parallelStream().forEach(::downloadEntry)
			logger.info("Entry page {}/{} download complete", it.currentPage, it.totalPage)
			true
		})
	}

	fun downloadEntry(id: Int): Unit = repository.findById(id).map(::downloadEntry).orElseThrow { NoException("You must pull it before download") }

	fun generateEntryRootPath(domain: NoDomain): Path {
		val groupName: String = groupNameGenerator.generate(domain.id.toLong())
		return Path.of(settingService.getHomeDirectory(), "No", groupName, domain.id.toString())
	}

	private fun downloadEntry(domain: NoDomain): Unit =
			Option.of(domain)
					.filter { !it.downloaded }
					.onEmpty { logger.info("Entry {} already exists", domain.id) }
					.map { generateEntryRootPath(it) }
					.map { entryRootPath ->
						createRootPath(entryRootPath)
						logger.info("Entry {} download start", domain.id)
						IntStream.range(0, domain.quantity + 1).parallel().forEach { downloadEntryImage(entryRootPath, domain, it) }
						logger.info("Entry {} download complete", domain.id)
						openFromExplorer(entryRootPath)
					}.forEach { _ ->
						domain.downloaded = true
						repository.save(domain)
					}

	private fun createRootPath(entryRootPath: Path) {
		if (!Files.exists(entryRootPath)) {
			Files.createDirectories(entryRootPath)
		}
	}

	private fun openFromExplorer(entryRootPath: Path) {
		Try.of { entryRootPath }
				.map(Path::toFile)
				.mapTry(Desktop.getDesktop()::open)
				.onFailure { logger.info("Open {} from explorer failed, {}", entryRootPath, it.message) }
	}

	private fun generateEntryImagePath(entryRootPath: Path, index: Int): Path =
			Path.of(entryRootPath.toString(), "${formatter.format(index)}.jpg")

	private fun downloadEntryImage(rootPath: Path, domain: NoDomain, index: Int) {
		if (domain.id == 3079 && index > 92) return
		val imagePath: Path = generateEntryImagePath(rootPath, index)
		if (Files.exists(imagePath)) return

		(0..9).forEach { _ ->
			try {
				val bytes: ByteArray = getEntryImage(domain, index)
				Files.write(imagePath, bytes)
				return
			} catch (ex: HttpClientErrorException.NotFound) {
				return
			} catch (ignored: Exception) {
			}
		}
		logger.error("Entry {} with index {} download failed", domain.id, index)
	}

	fun getEntryImage(entry: NoDomain, index: Int): ByteArray {
		val newIndex: String = formatter.format(index)
		val url: String = if (index != 0) entry.urlPrefix + "/" + newIndex + ".rosi" else entry.coverUrl
		return template.getForObject(url, ByteArray::class.java)
	}

}