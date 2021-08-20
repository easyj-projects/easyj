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
package icu.easyj.sdk.ocr;

/**
 * 卡证的正反面枚举
 *
 * @author wangliang
 */
public enum CardSide {

	/**
	 * 正反两面都有
	 */
	BOTH("两面"),

	/**
	 * 正面
	 * 如：身份证有照片的一面（人像面）
	 */
	FRONT("正面"),

	/**
	 * 反面
	 * 如：身份证有国徽的一面（国徽面）
	 */
	BACK("反面");

	private final String sideName;

	CardSide(String sideName) {
		this.sideName = sideName;
	}

	public String sideName() {
		return sideName;
	}
}
