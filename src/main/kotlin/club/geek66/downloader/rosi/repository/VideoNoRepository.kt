package club.geek66.downloader.rosi.repository

import club.geek66.downloader.rosi.domain.VideoNoDomain
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author: orange
 * @date: 2020/7/27
 * @time: 下午10:22
 * @copyright: Copyright 2020 by orange
 */
interface VideoNoRepository : JpaRepository<VideoNoDomain, Int>