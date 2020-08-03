package club.geek66.downloader.rosi.service.no.service

import club.geek66.downloader.rosi.client.RosiClient
import club.geek66.downloader.rosi.client.pojo.NoEntry
import club.geek66.downloader.rosi.common.log.Loggable
import club.geek66.downloader.rosi.common.Page
import club.geek66.downloader.rosi.common.Pager
import club.geek66.downloader.rosi.service.no.repository.NoRepository
import org.springframework.stereotype.Service

/**
 * @author: 橙子
 * @date: 2019/12/19
 * @time: 21:19
 * @copyright: Copyright 2019 by 橙子
 */
@Service
class NoPullService(private val client: RosiClient, private val repository: NoRepository) : Loggable {

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