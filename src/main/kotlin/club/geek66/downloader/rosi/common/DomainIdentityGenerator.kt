package club.geek66.downloader.rosi.common

import org.hibernate.HibernateException
import org.hibernate.annotations.GenericGenerator
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentityGenerator
import java.io.Serializable

/**
 * @author: 橙子
 * @date: 2019/12/10
 * @time: 22:52
 * @copyright: Copyright 2019 by 橙子
 */
@GenericGenerator(name = "domainIdentityGenerator", strategy = "club.geek66.downloader.rosi.common.DomainIdentityGenerator")
class DomainIdentityGenerator : IdentityGenerator() {

	override fun generate(implementor: SharedSessionContractImplementor, domain: Any): Serializable {
		if (domain !is IdGetter) {
			throw HibernateException(NullPointerException())
		}

		if (domain.id < 0) {
			throw HibernateException(NullPointerException())
		}
		return domain.id
	}

}