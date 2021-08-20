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
package icu.easyj.spring.boot.autoconfigure.sdk.tencent.cloud;

import icu.easyj.sdk.tencent.cloud.config.TencentCloudCommonConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云全局通用配置
 *
 * @author wangliang181230
 * @see <a href="https://console.cloud.tencent.com/cam/capi">腾讯云密钥对管理页面</a>
 */
@ConfigurationProperties("easyj.sdk.tencent-cloud.common")
public class TencentCloudCommonProperties extends TencentCloudCommonConfig {
}
