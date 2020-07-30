package club.geek66.downloader.rosi.configuration

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