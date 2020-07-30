package club.geek66.downloader.rosi.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

/**
 *
 * @author: orange
 * @date: 2020/7/28
 * @time: 下午9:14
 * @copyright: Copyright 2020 by orange
 */
@Configuration
class BeanConfiguration {

	@Bean
	fun restTemplate(): RestTemplate = SimpleClientHttpRequestFactory().let {
		it.setReadTimeout(1000)
		it.setConnectTimeout(1000)
		RestTemplate()
	}

}

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