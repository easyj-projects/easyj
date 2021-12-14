package icu.easyj.spring.boot.autoconfigure.logging.log4j;

import icu.easyj.core.util.jar.JarInfo;
import icu.easyj.core.util.jar.JarUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class EasyjCheckLog4j2LoopholeAutoConfiguration implements Ordered {

	public EasyjCheckLog4j2LoopholeAutoConfiguration() {
		// 创建一个日志实例
		Logger logger = LogManager.getLogger(this.getClass());

		// 判断实现：是否为存在漏洞的实现
		if ("org.apache.logging.log4j.core.Logger".equals(logger.getClass().getName())) {
			// 获取log4j-core的jar信息
			JarInfo jarInfo = JarUtils.getJar("log4j-core");

			// 判断版本号：2.0.x ~ 2.14.x版本存在漏洞
			if (jarInfo != null && jarInfo.betweenVersion("2.0.0-SNAPSHOT", "2.14.999")) {
				logger.warn("严重漏洞警告：当前log4j日志的实现类为 '{}'，且 'log4j-core.jar' 的版本号为：{}。" +
								"该版本的该实现存在远程代码执行漏洞，请尽快升级log4j到2.15.0及以上版本",
						logger.getClass().getName(), jarInfo.getVersion());
				logger.warn("LOG4J2-3021漏洞复现步骤：https://mp.weixin.qq.com/s/0tBE0Y4c-XLPlVdVsYZ4Ig");
				logger.warn("LOG4J2-3021漏洞修复记录：https://github.com/apache/logging-log4j2/commit/7fe72d6");
				logger.warn("漏洞检测：{}", "${java:os}");
				logger.warn("请观察上一行日志中冒号后面的内容：如果显示出操作系统信息，说明漏洞的确存在；如果显示出 '${java:os}'，则说明漏洞不存在，以上警告信息为误报。");
				logger.warn("您可以添加配置关闭该警告：easyj.loophole-check.log4j2=false");
			}
		}
	}


	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
