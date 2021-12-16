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
package icu.easyj.core.util.jar.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import org.springframework.core.Ordered;

/**
 * Spring的JAR组名
 *
 * @author wangliang181230
 */
@LoadLevel(name = "spring", order = Ordered.LOWEST_PRECEDENCE - 1000)
public class SpringJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		String name = jarContext.getName();

		// spring
		if (name.startsWith("spring-")) {
			if (name.startsWith("spring-boot")) {
				return "org.springframework.boot";
			}
			if (name.startsWith("spring-cloud")) {
				return "org.springframework.cloud";
			}
			if (name.startsWith("spring-data")) {
				return "org.springframework.data";
			}
			if (name.startsWith("spring-security")) {
				return "org.springframework.security";
			}
			if (name.startsWith("spring-ws")) {
				return "org.springframework.ws";
			}
			if (name.startsWith("spring-kafka")) {
				return "org.springframework.kafka";
			}
			if (name.startsWith("spring-amqp")
					|| name.startsWith("spring-rabbit")
					|| name.startsWith("spring-erlang")) {
				return "org.springframework.amqp";
			}
			if (name.startsWith("spring-batch")) {
				return "org.springframework.batch";
			}
			if (name.startsWith("spring-session")) {
				return "org.springframework.session";
			}
			if (name.startsWith("spring-webflow")) {
				return "org.springframework.webflow";
			}
			if (name.startsWith("spring-aws")) {
				return "org.springframework.aws";
			}
			if (name.startsWith("spring-kotlin")) {
				return "org.springframework.kotlin";
			}
			if (name.startsWith("spring-guice")) {
				return "org.springframework.guice";
			}
			if (name.startsWith("spring-metrics")) {
				return "org.springframework.metrics";
			}
			if (name.startsWith("spring-commons")) {
				return "org.springframework.commons";
			}
			if (name.startsWith("spring-graphql")) {
				return "org.springframework.graphql";
			}
			if (name.startsWith("spring-integration")) {
				return "org.springframework.integration";
			}
			if (name.startsWith("spring-roo")) {
				return "org.springframework.roo";
			}
			if (name.startsWith("spring-shell")) {
				return "org.springframework.shell";
			}
			if (name.startsWith("spring-osgi")) {
				return "org.springframework.osgi";
			}
			if (name.startsWith("spring-ldap")) {
				return "org.springframework.ldap";
			}
			if (name.startsWith("spring-social")) {
				return "org.springframework.social";
			}
			if (name.startsWith("spring-hateoas")) {
				return "org.springframework.hateoas";
			}
			if (name.startsWith("spring-android")) {
				return "org.springframework.android";
			}
			if (name.startsWith("spring-restdocs")) {
				return "org.springframework.restdocs";
			}

			return "org.springframework";
		} else if (name.startsWith("org.springframework")) {
			if (name.startsWith("org.springframework.core")) {
				return "org.springframework.core";
			}
			if (name.startsWith("org.springframework.aop")) {
				return "org.springframework.aop";
			}
			if (name.startsWith("org.springframework.webflow")) {
				return "org.springframework.webflow";
			}
			if (name.startsWith("org.springframework.roo")) {
				return "org.springframework.roo";
			}
		}

		return null;
	}
}
