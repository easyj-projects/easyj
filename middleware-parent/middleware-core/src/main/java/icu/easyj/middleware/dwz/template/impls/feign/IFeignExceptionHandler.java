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
package icu.easyj.middleware.dwz.template.impls.feign;

/**
 * Feign异常处理器
 *
 * @author wangliang181230
 */
public interface IFeignExceptionHandler {

	/**
	 * 处理异常
	 *
	 * @param e 异常信息
	 * @throws Exception 可以将异常信息转换再抛出
	 */
	void handle(RuntimeException e) throws Exception;
}
