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
import java.nio.charset.Charset;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 对称加密算法
 *
 * @author wangliang181230
 */
public interface ISymmetricCrypto {

	//region Encrypt

	/**
	 * 加密
	 *
	 * @param data 被加密的bytes
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data);

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
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Hex
	 */
	default String encryptHex(byte[] data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 数据
	 * @return 加密后的Base64
	 * @since 4.0.1
	 */
	default String encryptBase64(byte[] data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, String charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, Charset charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 * @since 4.5.12
	 */
	default String encryptHex(String data, String charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 * @since 4.5.12
	 */
	default String encryptHex(String data, Charset charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 */
	default String encryptBase64(String data, String charset) {
		return Base64.encode(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Base64
	 * @since 4.5.12
	 */
	default String encryptBase64(String data, Charset charset) {
		return Base64.encode(encrypt(data, charset));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data) {
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	default String encryptHex(String data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(String data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 加密，加密后关闭流
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] encrypt(InputStream data) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Hex
	 */
	default String encryptHex(InputStream data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(InputStream data) {
		return Base64.encode(encrypt(data));
	}

	//endregion


	//region Decrypt

	/**
	 * 解密
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes);

	/**
	 * 解密，针对大数据量，结束后不关闭流
	 *
	 * @param data    加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流，包括输入和输出流
	 * @throws IORuntimeException IO异常
	 */
	void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException;


	/**
	 * 解密为字符串
	 *
	 * @param bytes   被解密的bytes
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptStr(byte[] bytes, Charset charset) {
		return StrUtil.str(decrypt(bytes), charset);
	}

	/**
	 * 解密为字符串，默认UTF-8编码
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的String
	 */
	default String decryptStr(byte[] bytes) {
		return decryptStr(bytes, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param data 被解密的String，必须为16进制字符串或Base64表示形式
	 * @return 解密后的bytes
	 */
	default byte[] decrypt(String data) {
		return decrypt(SecureUtil.decode(data));
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串
	 *
	 * @param data    被解密的String
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptStr(String data, Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密Hex（16进制）或Base64表示的字符串，默认UTF-8编码
	 *
	 * @param data 被解密的String
	 * @return 解密后的String
	 */
	default String decryptStr(String data) {
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密，会关闭流
	 *
	 * @param data 被解密的bytes
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] decrypt(InputStream data) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data));
	}

	/**
	 * 解密，不会关闭流
	 *
	 * @param data    被解密的InputStream
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptStr(InputStream data, Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密
	 *
	 * @param data 被解密的InputStream
	 * @return 解密后的String
	 */
	default String decryptStr(InputStream data) {
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
	}

	//endregion
}
