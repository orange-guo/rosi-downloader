package club.geek66.downloader.rosi.service

import club.geek66.downloader.rosi.client.RosiClient
import club.geek66.downloader.rosi.client.pojo.EntryPageResponse
import club.geek66.downloader.rosi.client.pojo.NoEntry
import club.geek66.downloader.rosi.common.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.nio.file.Files
import java.nio.file.Path
import java.text.DecimalFormat
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.persistence.*
import javax.transaction.Transactional

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 21:44
 * @copyright: Copyright 2019 by 橙子
 */
@Entity
class NoDomain : IdGetter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "domainIdentityGenerator")
	@GenericGenerator(name = "domainIdentityGenerator", strategy = "club.geek66.downloader.rosi.common.DomainIdentityGenerator")
	override var id: Int = -1

	@Enumerated(EnumType.STRING)
	lateinit var type: RosiEntryType

	lateinit var coverUrl: String

	// http://rs.jinyemimi.com/jpg/3154-tBxfldMb
	lateinit var urlPrefix: String

	var quantity: Int = -1

	var downloaded: Boolean = false

}

/**
 * @author: orange
 * @date: 2020/7/19
 * @time: 下午6:41
 * @copyright: Copyright 2020 by orange
 */
enum class RosiEntryType {

	NO, KZ, YXM

}

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 21:26
 * @copyright: Copyright 2019 by 橙子
 */
@Transactional
interface NoRepository : JpaRepository<NoDomain, Int>

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

/**
 * @author: 橙子
 * @date: 2019/12/19
 * @time: 21:19
 * @copyright: Copyright 2019 by 橙子
 */
@Service
class NoPullService(private val client: RosiClient, private val repository: NoRepository) : AbstractLoggable() {

	private final val getPage: (Int) -> Page<NoEntry> = client::getNoPage
	fun pullAll() {
		Pager.doByAsc(getPage = getPage, consumePage = {
			it.content.parallelStream().map(NoEntry::convertToDomain).filter { !repository.existsById(it.id) }.forEach {
				logger.info("Save {}", it.id)
				repository.save(it)
			}
			true
		})
	}

	/*fun pull(id: Int) {
		client.getNoPage()
	}*/
	fun fastPull() {
		Pager.doByAsc(getPage = getPage, consumePage = {
			it.content.parallelStream().map(NoEntry::convertToDomain).filter { !repository.existsById(it.id) }.map {
				logger.info("Save {}", it.id)
				repository.save(it)
			}.count() != 0L
		})
	}
}

/**
 * @author: orange
 * @date: 2020/7/29
 * @time: 下午11:30
 * @copyright: Copyright 2020 by orange
 */
@Service
class NoService(private val downloadService: NoDownloadService, private val pullService: NoPullService) {

	fun fastPull() {
		pullService.fastPull()
	}

	fun pullAll() {
		pullService.pullAll()
	}

	fun download(id: Int) {
		downloadService.download(id)
	}

	fun downloadAll() {
		downloadService.downloadAll()
	}

}

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

	@ShellMethod(key = ["no-fast-pull"], value = "From remote server fast pull latest Rosi-No.")
	fun fastPull() = service.fastPull()

	@ShellMethod(key = ["no-download-all"], value = "From remote server download all Rosi-No.")
	fun downloadAll() = service.downloadAll()

}

