package club.geek66.downloader.rosi.service

import club.geek66.downloader.rosi.common.Page

/**
 * @author: orange
 * @date: 2020/7/28
 * @time: 下午11:36
 * @copyright: Copyright 2020 by orange
 */
object Pager {

	fun <T> doByDesc(getPage: (Int) -> Page<T>, consumePage: (Page<T>) -> Boolean) {
		var page:Page<T> = getPage(1)
		var currentPage:Int = page.totalPage
		while (currentPage >= 1) {
			page = getPage(currentPage)
			if (!consumePage(page)) {
				break
			}
			currentPage--
		}
	}

	fun <T> doByAsc(getPage: (Int) -> Page<T>, consumePage: (Page<T>) -> Boolean) {
		var currentPage = 1
		var totalPage: Int

		do {
			val page:Page<T> = getPage(currentPage)
			if (!consumePage(page)) {
				break
			}
			currentPage++
			totalPage = page.totalPage
		} while (currentPage <= totalPage)
	}

}