package club.geek66.downloader.rosi.service.no

import club.geek66.downloader.rosi.client.dto.EntryPageResponse
import club.geek66.downloader.rosi.common.AbstractLoggable
import club.geek66.downloader.rosi.configuration.properties.RosiProperties
import club.geek66.downloader.rosi.domain.NoDomain
import club.geek66.downloader.rosi.repository.NoRepository
import club.geek66.downloader.rosi.service.Pager
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
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
	private val properties: RosiProperties
) : AbstractLoggable() {

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
			it.content.parallelStream().forEach(::download)
			logger.info("Entry page {}/{} download complete", it.currentPage, it.totalPage)
			true
		})
	}

	fun download(id: Int): Unit = repository.findById(id).map(::download).orElseThrow()
	private fun canDownload(domain: NoDomain): Boolean {
		val entryRootPath: Path = getEntryRootPath(domain)
		if (!Files.exists(entryRootPath)) {
			Files.createDirectories(entryRootPath)
			return true
		}

		return !domain.downloaded
	}

	private fun download(domain: NoDomain) {
		if (!canDownload(domain)) {
			logger.info("Entry {} already exists", domain.id)
			return
		}
		val rootPath: Path = getEntryRootPath(domain)
		logger.info("Entry {} download start", domain.id)
		IntStream.range(0, domain.quantity + 1).parallel().forEach { saveImg(rootPath, domain, it) }
		logger.info("Entry {} download complete", domain.id)
		domain.downloaded = true
		repository.save(domain)
		/*try {
			Desktop.getDesktop().open(rootPath.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private fun pathOf(rootPath: Path, index: String): Path {
		return Path.of(rootPath.toString(), "$index.jpg")
	}

	private fun saveImg(rootPath: Path, domain: NoDomain, index: Int) {
		val formattedIndex: String = formatter.format(index)
		val imagePath: Path = pathOf(rootPath, formattedIndex)
		if (Files.exists(imagePath)) {
			return
		}

		if (domain.id == 3079 && index > 92) {
			return
		}

		(0 .. 9).forEach { _ ->
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

	private fun getEntryRootPath(domain: NoDomain): Path {
		val pageStart: Long = domain.id / 100 * 100.toLong()
		val rangeString: String = pageStart.toString() + "-" + (pageStart + 100 - 1)
		return Path.of(properties.rootPath, rangeString, domain.id.toString())
	}

	fun getEntryImage(entry: NoDomain, index: Int): ByteArray {
		val newIndex: String = formatter.format(index)
		val url: String = if (index != 0) entry.urlPrefix + "/" + newIndex + ".rosi" else entry.coverUrl
		return template.getForObject(url, ByteArray::class.java)
	}
}