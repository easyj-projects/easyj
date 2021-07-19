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
package icu.easyj.crypto.symmetric;

import java.io.InputStream;
import java.io.OutputStream;

import cn.hutool.core.io.IORuntimeException;
import icu.easyj.crypto.ICrypto;

/**
 * 对称加密算法
 *
 * @author wangliang181230
 */
public interface ISymmetricCrypto extends ICrypto {

	/**
	 * 加密，针对大数据量，可选结束后是否关闭流
	 *
	 * @param data    被加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流
	 * @throws IORuntimeException IO异常
	 */
	void encrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException;


	/**
	 * 解密，针对大数据量，结束后不关闭流
	 *
	 * @param data    加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流，包括输入和输出流
	 * @throws IORuntimeException IO异常
	 */
	void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException;
}
