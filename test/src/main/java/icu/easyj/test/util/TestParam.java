/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.test.util;

/**
 * 测试参数
 *
 * @author wangliang181230
 */
public class TestParam {

	/**
	 * 线程号，值域：1~threadCount
	 */
	private int threadNo;

	/**
	 * 运行次号，值域：1~times
	 */
	private int runNo;

	/**
	 * 是否正在预热
	 */
	private boolean warmUp = false;


	public TestParam() {
	}

	public TestParam(int threadNo, int runNo, boolean warmUp) {
		this.threadNo = threadNo;
		this.runNo = runNo;
		this.warmUp = warmUp;
	}


	public int getThreadNo() {
		return threadNo;
	}

	public TestParam setThreadNo(int threadNo) {
		this.threadNo = threadNo;
		return this;
	}

	public int getRunNo() {
		return runNo;
	}

	public TestParam setRunNo(int runNo) {
		this.runNo = runNo;
		return this;
	}

	public boolean isWarmUp() {
		return warmUp;
	}

	public TestParam setWarmUp(boolean warmUp) {
		this.warmUp = warmUp;
		return this;
	}
}
