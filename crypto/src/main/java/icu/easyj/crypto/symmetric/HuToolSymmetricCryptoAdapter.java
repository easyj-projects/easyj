package icu.easyj.crypto.symmetric;

import java.io.InputStream;
import java.io.OutputStream;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.util.Assert;

/**
 * HuTool对称加密算法适配器
 *
 * @author wangliang181230
 */
public class HuToolSymmetricCryptoAdapter implements ISymmetricCrypto {

	private final SymmetricCrypto symmetricCrypto;

	/**
	 * 构造函数
	 *
	 * @param symmetricCrypto HuTool对称加密
	 */
	public HuToolSymmetricCryptoAdapter(SymmetricCrypto symmetricCrypto) {
		Assert.notNull(symmetricCrypto, "symmetricCrypto must be not null");
		this.symmetricCrypto = symmetricCrypto;
	}


	@Override
	public byte[] encrypt(byte[] data) {
		return symmetricCrypto.encrypt(data);
	}

	@Override
	public void encrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		symmetricCrypto.encrypt(data, out, isClose);
	}


	@Override
	public byte[] decrypt(byte[] bytes) {
		return symmetricCrypto.decrypt(bytes);
	}

	@Override
	public void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
		symmetricCrypto.decrypt(data, out, isClose);
	}

	//endregion
}
