package club.geek66.downloader.rosi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * @author: orange
 * @date: 2020/7/19
 * @time: 下午11:02
 * @copyright: Copyright 2020 by orange
 */
@Configuration
@ConfigurationProperties(prefix = "rosi")
class RosiProperties(
		var rootPath: String = "",

		var username: String = "lock123",

		var password: String = "lock123"
)