package org.easyj.spring.boot.autoconfigure.web;

import org.easyj.spring.boot.autoconfigure.EasyjStarterConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * EasyJ-Web-Cache304配置类
 *
 * @author wangliang181230
 */
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = EasyjStarterConstant.EASYJ_WEB_CACHE304_PREFIX)
public class EasyjWebCache304Properties {
}
