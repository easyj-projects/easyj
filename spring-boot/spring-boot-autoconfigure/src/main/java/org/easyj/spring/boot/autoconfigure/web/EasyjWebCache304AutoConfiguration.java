package org.easyj.spring.boot.autoconfigure.web;

import org.easyj.web.cache304.Cache304Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * EasyJ-Web-Cache304自动装配类
 *
 * @author wangliang@181230
 */
@ConditionalOnWebApplication
@EnableConfigurationProperties(EasyjWebCache304Properties.class)
public class EasyjWebCache304AutoConfiguration {

	@Bean
	public Cache304Aspect cache304Aspect() {
		return new Cache304Aspect();
	}
}
