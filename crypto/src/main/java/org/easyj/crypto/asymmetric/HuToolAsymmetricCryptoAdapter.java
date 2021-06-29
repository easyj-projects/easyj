package org.easyj.crypto.asymmetric;

import cn.hutool.crypto.asymmetric.AbstractAsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import org.springframework.util.Assert;

/**
 * HuTool非对称加密算法适配器
 *
 * @author wangliang181230
 */
public class HuToolAsymmetricCryptoAdapter implements IAsymmetricCrypto {

	private final AbstractAsymmetricCrypto asymmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param asymmetricCrypto HuTool非对称加密
	 */
	public HuToolAsymmetricCryptoAdapter(AbstractAsymmetricCrypto asymmetricCrypto) {
		Assert.notNull(asymmetricCrypto, "asymmetricCrypto must be not null");
		this.asymmetricCrypto = asymmetricCrypto;
	}


	@Override
	public byte[] encrypt(byte[] data, KeyType keyType) {
		return asymmetricCrypto.encrypt(data, keyType);
	}


	@Override
	public byte[] decrypt(byte[] bytes, KeyType keyType) {
		return asymmetricCrypto.decrypt(bytes, keyType);
	}
}
