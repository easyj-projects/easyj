/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.spring.boot.autoconfigure.loopholecheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.EasyjFastjsonBugfixUtils;
import icu.easyj.core.util.jar.JarInfo;
import icu.easyj.core.util.jar.JarUtils;
import icu.easyj.core.util.version.ExistLoopholeVersionError;
import icu.easyj.spring.boot.autoconfigure.jar.EasyjDependenciesAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;

/**
 * FastJson 漏洞检测程序
 *
 * @author wangliang181230
 * @see <a href="https://repo1.maven.org/maven2/com/alibaba/fastjson">fastjson公网发布版本查看页面，可查看到所有漏洞修复版本</a>
 */
@Lazy(false) // 避免延迟初始化功能启用后导致该类在项目启动过程中未被实例化
@ConditionalOnClass(JSON.class)
@ConditionalOnProperty(value = "easyj.loophole-check.fastjson", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(EasyjDependenciesAutoConfiguration.class)
@EnableConfigurationProperties(LoopholeCheckProperties.class)
public class EasyjFastjsonLoopholeCheckAutoConfiguration implements Ordered {

	public EasyjFastjsonLoopholeCheckAutoConfiguration(LoopholeCheckProperties properties) {
		// 获取 fastjson 的jar信息
		JarInfo jarInfo = JarUtils.getJar("com.alibaba", "fastjson");

		// 判断版本号：1.2.68及以下版本，且不是漏洞修复版本的，就存在漏洞
		// 漏洞修复版本请查阅：https://repo1.maven.org/maven2/com/alibaba/fastjson
		if (jarInfo != null && EasyjFastjsonBugfixUtils.isLoopholeVersion(jarInfo.getVersion())) {
			Logger logger = LoggerFactory.getLogger(this.getClass());

			// 打印漏洞警告日志
			logger.warn("");
			logger.warn("==>");
			logger.warn("fastjson 严重漏洞警告：");
			logger.warn("当前 fastjson 版本号为：{}。该版本存在远程代码执行漏洞，请尽快升级至1.2.69及以上版本。", jarInfo.getVersion());
			logger.warn("为了避免升级导致的兼容性问题，也可升级至低版本的最新漏洞修复版本（格式如 '1.2.68.sec10' 的版本号）。");
			logger.warn("fastjson版本号请查看链接（含低版本漏洞修复版本）：https://repo1.maven.org/maven2/com/alibaba/fastjson");
			logger.warn("如果您的fastjson版本已经是不存在漏洞的版本，您可以添加配置关闭该警告：easyj.loophole-check.fastjson=false");
			logger.warn("<==");
			logger.warn("");

			if (properties.isNeedThrowIfExist()) {
				throw new ExistLoopholeVersionError("当前fastjson版本存在远程代码执行漏洞，请尽快更新至1.2.69及以上版本，或最新漏洞修复版本！");
			}
		}
	}


	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
