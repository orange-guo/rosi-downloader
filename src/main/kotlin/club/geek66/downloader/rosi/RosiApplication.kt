package club.geek66.downloader.rosi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 22:48
 * @copyright: Copyright 2019 by 橙子
 */
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class])
class RosiApplication

fun main(args: Array<String>) {
	System.setProperty("java.awt.headless", "false")
	SpringApplication.run(RosiApplication::class.java, *args)
}