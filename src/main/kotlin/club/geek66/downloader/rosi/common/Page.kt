package club.geek66.downloader.rosi.common

/**
 *
 * @author: orange
 * @date: 2020/7/28
 * @time: 下午11:39
 * @copyright: Copyright 2020 by orange
 */
interface Page<T> {

	val currentPage: Int
	val totalPage: Int
	val content: Collection<T>
}