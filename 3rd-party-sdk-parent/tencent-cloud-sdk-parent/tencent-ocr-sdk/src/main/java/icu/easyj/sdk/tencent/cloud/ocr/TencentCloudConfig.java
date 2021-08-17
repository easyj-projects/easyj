package icu.easyj.sdk.tencent.cloud.ocr;

import com.tencentcloudapi.common.profile.Language;

/**
 * 腾讯云相关配置
 *
 * @author wangliang181230
 */
public class TencentCloudConfig {

	//region 安全相关配置

	/**
	 * 密钥对中的ID
	 *
	 * @see <a href="https://console.cloud.tencent.com/cam/capi">腾讯云密钥对管理页面</a>
	 */
	private String secretId;

	/**
	 * 密钥对中的Key
	 *
	 * @see <a href="https://console.cloud.tencent.com/cam/capi">腾讯云密钥对管理页面</a>
	 */
	private String secretKey;

	//endregion


	//region 地域配置

	/**
	 * 地域代码
	 * <p>
	 * 不同云服务的可选地域可能不同
	 */
	private String region;

	//endregion


	//region 接口相关配置

	/**
	 * Connect timeout in seconds
	 */
	private Integer connTimeout;

	/**
	 * Write timeout in seconds
	 */
	private Integer writeTimeout;

	/**
	 * Read timeout in seconds.
	 */
	private Integer readTimeout;

	/**
	 * 语言，默认：简体中文
	 */
	private Language language;

	/**
	 * 是否调试
	 */
	private Boolean debug;

	//endregion


	//region Getter、Setter

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getConnTimeout() {
		return connTimeout;
	}

	public void setConnTimeout(Integer connTimeout) {
		this.connTimeout = connTimeout;
	}

	public Integer getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(Integer writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}


	//endregion
}
