package club.geek66.downloader.rosi.service.no.repository

import club.geek66.downloader.rosi.service.no.domain.NoDomain
import org.springframework.data.jpa.repository.JpaRepository
import javax.transaction.Transactional

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 21:26
 * @copyright: Copyright 2019 by 橙子
 */
@Transactional
interface NoRepository : JpaRepository<NoDomain, Int>