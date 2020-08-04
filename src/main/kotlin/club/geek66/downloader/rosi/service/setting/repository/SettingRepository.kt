package club.geek66.downloader.rosi.service.setting.repository

import club.geek66.downloader.rosi.service.setting.domain.SettingDomain
import org.springframework.data.jpa.repository.JpaRepository

/**
 *
 * @author: orange
 * @date: 2020/8/3
 * @time: 下午10:31
 * @copyright: Copyright 2020 by orange
 */
interface SettingRepository : JpaRepository<SettingDomain, Int?>