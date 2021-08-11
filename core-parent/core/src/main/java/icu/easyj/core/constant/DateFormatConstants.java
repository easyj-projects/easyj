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
package icu.easyj.core.constant;

/**
 * 时间格式化相关常量
 *
 * @author wangliang181230
 */
public interface DateFormatConstants {

	String MM = "yyyy-MM"; // 精确到月，长度：7
	String DD = "yyyy-MM-dd"; // 精确到日，长度：10
	String MI = "yyyy-MM-dd HH:mm"; // 精确到分，长度：16
	String SS = "yyyy-MM-dd HH:mm:ss"; // 精确到秒，长度：19
	String SSS = "yyyy-MM-dd HH:mm:ss.SSS"; // 完整格式，精确到毫秒，长度：23

	String MM2 = "yyyy/MM"; // 精确到月，长度：7
	String DD2 = "yyyy/MM/dd"; // 精确到日，长度：10
	String MI2 = "yyyy/MM/dd HH:mm"; // 精确到分，长度：16
	String SS2 = "yyyy/MM/dd HH:mm:ss"; // 精确到秒，长度：19
	String SSS2 = "yyyy/MM/dd HH:mm:ss.SSS"; // 完整格式，精确到毫秒，长度：23

	String MM_UNSIGNED = "yyyyMM"; // 精确到月，长度：6
	String DD_UNSIGNED = "yyyyMMdd"; // 精确到日，长度：8
	String MI_UNSIGNED = "yyyyMMddHHmm"; // 精确到分，长度：12
	String SS_UNSIGNED = "yyyyMMddHHmmss"; // 精确到秒，长度：14
	String SSS_UNSIGNED = "yyyyMMddHHmmssSSS"; // 完整格式，精确到毫秒，长度：17

	String TIME_MM = "HH:mm"; // 无日期，精确到分钟，长度：5
	String TIME_SS = "HH:mm:ss"; // 无日期，精确到秒，长度：8
}
