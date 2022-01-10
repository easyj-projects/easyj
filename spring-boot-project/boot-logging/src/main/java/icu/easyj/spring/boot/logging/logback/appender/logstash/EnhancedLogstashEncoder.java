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
package icu.easyj.spring.boot.logging.logback.appender.logstash;

import java.util.ArrayList;

import net.logstash.logback.composite.JsonProvider;
import net.logstash.logback.composite.JsonProviders;
import net.logstash.logback.encoder.LogstashEncoder;

/**
 * The type Enhanced logstash encoder
 *
 * @author wangliang181230
 */
public class EnhancedLogstashEncoder extends LogstashEncoder {

	/**
	 * set exclude provider
	 *
	 * @param excludedProviderClassName the excluded provider class name
	 */
	public void setExcludeProvider(String excludedProviderClassName) {
		JsonProviders<?> providers = getFormatter().getProviders();
		for (JsonProvider<?> provider : new ArrayList<>(providers.getProviders())) {
			if (provider.getClass().getName().equals(excludedProviderClassName)) {
				providers.removeProvider((JsonProvider)provider);
			}
		}
	}
}
