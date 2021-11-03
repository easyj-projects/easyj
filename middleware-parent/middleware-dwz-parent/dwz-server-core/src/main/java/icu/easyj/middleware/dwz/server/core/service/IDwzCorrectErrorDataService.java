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
package icu.easyj.middleware.dwz.server.core.service;

/**
 * 纠正错误数据的服务接口
 *
 * @author wangliang181230
 */
public interface IDwzCorrectErrorDataService {

	/**
	 * 纠正序列值
	 * <p>
	 * 说明：当前序列值不能小于短链接记录的最大ID值
	 *
	 * @return result 纠正结果 1=成功 | 0=无需纠正 | -1=失败
	 */
	int correctSequence();
}
