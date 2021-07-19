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
package icu.easyj.crypto;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 非对称加密算法
 *
 * @author wangliang181230
 */
public interface ICrypto extends Serializable {

	//region Encrypt

	//region 加密为byte[] start

	/**
	 * 加密
	 *
	 * @param data 被加密的bytes
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data);

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
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, String charset) {
		return encrypt(StrUtil.bytes(data, charset));
	}

	/**
	 * 加密，并关闭流
	 *
	 * @param data 被加密的数据流
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] encrypt(InputStream data) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data));
	}

	//endregion 加密为byte[] end


	//region 加密为Hex start

	/**
	 * 编码为Hex字符串
	 *
	 * @param data 被加密的bytes
	 * @return Hex字符串
	 */
	default String encryptHex(byte[] data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data 被加密的字符串
	 * @return Hex字符串
	 */
	default String encryptHex(String data) {
		return HexUtil.encodeHexStr(encrypt(data));
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param charset 编码
	 * @return Hex字符串
	 */
	default String encryptHex(String data, Charset charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return 加密后的Hex
	 */
	default String encryptHex(String data, String charset) {
		return HexUtil.encodeHexStr(encrypt(data, charset));
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

	//endregion 加密为Hex end


	//region 加密为Base64 start

	/**
	 * 编码为Base64字符串
	 *
	 * @param data 被加密的bytes
	 * @return Base64字符串
	 */
	default String encryptBase64(byte[] data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 编码为Base64字符串，使用UTF-8编码
	 *
	 * @param data 被加密的字符串
	 * @return Base64字符串
	 */
	default String encryptBase64(String data) {
		return Base64.encode(encrypt(data));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @return Base64字符串
	 */
	default String encryptBase64(String data, Charset charset) {
		return Base64.encode(encrypt(data, charset));
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
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBase64(InputStream data) {
		return Base64.encode(encrypt(data));
	}

	//endregion 加密为Base64 end


	//region 分组加密 start

	/**
	 * 分组加密
	 *
	 * @param data 数据
	 * @return 加密后的密文
	 */
	default String encryptBcd(byte[] data) {
		return BCD.bcdToStr(encrypt(data));
	}

	/**
	 * 分组加密
	 *
	 * @param data 数据
	 * @return 加密后的密文
	 */
	default String encryptBcd(String data) {
		return encryptBcd(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组加密
	 *
	 * @param data    数据
	 * @param charset 加密前编码
	 * @return 加密后的密文
	 */
	default String encryptBcd(String data, Charset charset) {
		return BCD.bcdToStr(encrypt(data, charset));
	}

	/**
	 * 分组加密
	 *
	 * @param data    数据
	 * @param charset 加密前编码
	 * @return 加密后的密文
	 */
	default String encryptBcd(String data, String charset) {
		return BCD.bcdToStr(encrypt(data, charset));
	}

	/**
	 * 加密
	 *
	 * @param data 被加密的字符串
	 * @return 加密后的Base64
	 */
	default String encryptBcd(InputStream data) {
		return BCD.bcdToStr(encrypt(data));
	}

	//endregion 分组加密 end

	//endregion


	//region Decrypt

	//region 解密为byte[] start

	/**
	 * 解密
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes);

	/**
	 * 从Hex或Base64字符串解密，编码为UTF-8格式
	 *
	 * @param data Hex（16进制）或Base64字符串
	 * @return 解密后的bytes
	 */
	default byte[] decrypt(String data) {
		return decrypt(SecureUtil.decode(data));
	}

	/**
	 * 解密
	 *
	 * @param data 被解密的bytes
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] decrypt(InputStream data) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data));
	}

	//endregion 解密为byte[] end


	//region 解密为String start

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

	//endregion 解密为String end


	//region 解密 Hex（16进制）或Base64 为String start

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 */
	default String decryptStr(String data, Charset charset) {
		return StrUtil.str(decrypt(data), charset);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data 数据，Hex（16进制）或Base64字符串
	 * @return 解密后的密文
	 */
	default String decryptStr(String data) {
		return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
	}

	//endregion


	//region 解密Base64为String start

	/**
	 * 解密Base64表示的字符串
	 *
	 * @param data    被解密的字符串
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptBase64(String data, Charset charset) {
		return decryptStr(Base64.decode(data), charset);
	}

	/**
	 * 解密Base64表示的字符串，使用UTF-8编码
	 *
	 * @param data 被解密的字符串
	 * @return 解密后的String
	 */
	default String decryptBase64(String data) {
		return decryptBase64(data, CharsetUtil.CHARSET_UTF_8);
	}

	//endregion 解密Base64为String end


	//region 解密Hex为String start

	/**
	 * 解密Hex（16进制）
	 *
	 * @param data    被解密的字符串
	 * @param charset 解密后的charset
	 * @return 解密后的String
	 */
	default String decryptHex(String data, Charset charset) {
		return decryptStr(HexUtil.decodeHex(data), charset);
	}

	/**
	 * 解密Hex（16进制），使用UTF-8编码
	 *
	 * @param data 被解密的字符串
	 * @return 解密后的String
	 */
	default String decryptHex(String data) {
		return decryptHex(data, CharsetUtil.CHARSET_UTF_8);
	}

	//endregion 解密Hex为String end


	//region 解密BCD start

	/**
	 * 解密BCD
	 *
	 * @param data 数据
	 * @return 解密后的密文
	 */
	default byte[] decryptFromBcd(String data) {
		return decryptFromBcd(data, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组解密
	 *
	 * @param data    数据
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 */
	default byte[] decryptFromBcd(String data, Charset charset) {
		Assert.notNull(data, "Bcd string must be not null!");
		final byte[] dataBytes = BCD.ascToBcd(StrUtil.bytes(data, charset));
		return decrypt(dataBytes);
	}

	/**
	 * 解密为字符串，密文需为BCD格式
	 *
	 * @param data    数据，BCD格式
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 */
	default String decryptStrFromBcd(String data, Charset charset) {
		return StrUtil.str(decryptFromBcd(data, charset), charset);
	}

	/**
	 * 解密为字符串，密文需为BCD格式，编码为UTF-8格式
	 *
	 * @param data 数据，BCD格式
	 * @return 解密后的密文
	 */
	default String decryptStrFromBcd(String data) {
		return decryptStrFromBcd(data, CharsetUtil.CHARSET_UTF_8);
	}

	//endregion 解密BCD end

	//endregion
}
