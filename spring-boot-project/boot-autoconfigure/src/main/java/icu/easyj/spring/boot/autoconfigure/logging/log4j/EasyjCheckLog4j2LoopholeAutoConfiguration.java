/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.spring.boot.autoconfigure.logging.log4j;

import icu.easyj.core.util.jar.JarInfo;
import icu.easyj.core.util.jar.JarUtils;
import icu.easyj.core.util.version.ExistLoopholeVersionError;
import icu.easyj.spring.boot.autoconfigure.LoopholeCheckProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;

/**
 * Log4j漏洞检测程序
 *
 * @author wangliang181230
 */
@Lazy(false) // 避免延迟初始化功能启用后导致该类在项目启动过程中未被实例化
@ConditionalOnClass(name = {
		"org.apache.logging.log4j.Logger", // log4j-api中的接口
		//"org.apache.logging.log4j.core.Logger", // log4j-core中的接口实现
		"org.apache.logging.log4j.core.net.JndiManager" // log4j-core中存在漏洞的类
})
@ConditionalOnProperty(value = "easyj.loophole-check.log4j2", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LoopholeCheckProperties.class)
public class EasyjCheckLog4j2LoopholeAutoConfiguration implements Ordered {

	public EasyjCheckLog4j2LoopholeAutoConfiguration(LoopholeCheckProperties properties) {
		// 创建一个log4j的日志实例
		Logger logger = LogManager.getLogger(this.getClass());

		// 判断实现：是否为可能存在漏洞的实现类
		if ("org.apache.logging.log4j.core.Logger".equals(logger.getClass().getName())) {
			// 获取log4j-core的jar信息
			JarInfo jarInfo = JarUtils.getJar("log4j-core");

			// 判断版本号：2.0.x ~ 2.14.x版本存在漏洞
			if (jarInfo != null && jarInfo.betweenVersion("2.0.0-SNAPSHOT", "2.14.999")) {
				logger.warn("");
				logger.warn("==>");
				logger.warn("log4j2 严重漏洞警告：");
				logger.warn("当前log4j2日志的实现类为 '{}'，且 'log4j-core.jar' 的版本号为：{}。" +
								"该版本的该实现存在远程代码执行漏洞，请尽快升级log4j2到2.15.0及以上版本",
						logger.getClass().getName(), jarInfo.getVersion());
				logger.warn("漏洞复现步骤：https://mp.weixin.qq.com/s/0tBE0Y4c-XLPlVdVsYZ4Ig");
				logger.warn("漏洞修复记录：https://github.com/apache/logging-log4j2/commit/7fe72d6");
				logger.warn("漏洞确认日志：{}", "${java:os}");
				logger.warn("请观察上一行“漏洞确认日志”中冒号后面的内容：如果显示出操作系统信息，说明漏洞的确存在；如果显示出 '${java:os}'，则说明漏洞不存在，以上警告信息则为误报。");
				logger.warn("您可以添加配置关闭该警告：easyj.loophole-check.log4j2=false");
				logger.warn("<==");
				logger.warn("");

				if (properties.isNeedThrowException()) {
					throw new ExistLoopholeVersionError("当前log4j2版本存在远程代码执行漏洞，请尽快更新至2.15.0及以上版本！");
				}
			}
		}
	}


	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
