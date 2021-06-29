package org.easyj.crypto.asymmetric;

import java.io.InputStream;
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
import cn.hutool.crypto.asymmetric.KeyType;

/**
 * 非对称加密算法
 *
 * @author wangliang181230
 */
public interface IAsymmetricCrypto {

	//region Encrypt

	/**
	 * 加密
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data, KeyType keyType);

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 */
	default String encryptHex(byte[] data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 */
	default String encryptBase64(byte[] data, KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, String charset, KeyType keyType) {
		return encrypt(StrUtil.bytes(data, charset), keyType);
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @param charset 编码
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, KeyType keyType, Charset charset) {
		return encrypt(StrUtil.bytes(data, charset), keyType);
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	default byte[] encrypt(String data, KeyType keyType) {
		return encrypt(StrUtil.bytes(data, CharsetUtil.CHARSET_UTF_8), keyType);
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 */
	default String encryptHex(String data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @param charset 编码
	 * @return Hex字符串
	 */
	default String encryptHex(String data, KeyType keyType, Charset charset) {
		return HexUtil.encodeHexStr(encrypt(data, keyType, charset));
	}

	/**
	 * 编码为Base64字符串，使用UTF-8编码
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 */
	default String encryptBase64(String data, KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @param charset 编码
	 * @return Base64字符串
	 */
	default String encryptBase64(String data, KeyType keyType, Charset charset) {
		return Base64.encode(encrypt(data, keyType, charset));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] encrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 */
	default String encryptHex(InputStream data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 */
	default String encryptBase64(InputStream data, KeyType keyType) {
		return Base64.encode(encrypt(data, keyType));
	}

	/**
	 * 分组加密
	 *
	 * @param data    数据
	 * @param keyType 密钥类型
	 * @return 加密后的密文
	 */
	default String encryptBcd(String data, KeyType keyType) {
		return encryptBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组加密
	 *
	 * @param data    数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 加密后的密文
	 */
	default String encryptBcd(String data, KeyType keyType, Charset charset) {
		return BCD.bcdToStr(encrypt(data, keyType, charset));
	}

	//endregion


	//region Decrypt

	/**
	 * 解密
	 *
	 * @param bytes   被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes, KeyType keyType);

	/**
	 * 解密
	 *
	 * @param data    被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	default byte[] decrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 从Hex或Base64字符串解密，编码为UTF-8格式
	 *
	 * @param data    Hex（16进制）或Base64字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	default byte[] decrypt(String data, KeyType keyType) {
		return decrypt(SecureUtil.decode(data), keyType);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 */
	default String decryptStr(String data, KeyType keyType, Charset charset) {
		return StrUtil.str(decrypt(data, keyType), charset);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 */
	default String decryptStr(String data, KeyType keyType) {
		return decryptStr(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 解密BCD
	 *
	 * @param data    数据
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 */
	default byte[] decryptFromBcd(String data, KeyType keyType) {
		return decryptFromBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 分组解密
	 *
	 * @param data    数据
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 */
	default byte[] decryptFromBcd(String data, KeyType keyType, Charset charset) {
		Assert.notNull(data, "Bcd string must be not null!");
		final byte[] dataBytes = BCD.ascToBcd(StrUtil.bytes(data, charset));
		return decrypt(dataBytes, keyType);
	}

	/**
	 * 解密为字符串，密文需为BCD格式
	 *
	 * @param data    数据，BCD格式
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 */
	default String decryptStrFromBcd(String data, KeyType keyType, Charset charset) {
		return StrUtil.str(decryptFromBcd(data, keyType, charset), charset);
	}

	/**
	 * 解密为字符串，密文需为BCD格式，编码为UTF-8格式
	 *
	 * @param data    数据，BCD格式
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 */
	default String decryptStrFromBcd(String data, KeyType keyType) {
		return decryptStrFromBcd(data, keyType, CharsetUtil.CHARSET_UTF_8);
	}

	//endregion
}
