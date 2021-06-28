package org.easyj.spring.boot.autoconfigure;

import org.easyj.core.constant.EasyjConstant;

/**
 * EasyJ-Starter相关的常量
 *
 * @author wangliang181230
 */
public interface EasyjStarterConstant {

	//region PREFIX

	String EASYJ_PREFIX = EasyjConstant.PREFIX;

	// Office
	String EASYJ_OFFICE_PREFIX = EASYJ_PREFIX + ".office";
	String EASYJ_OFFICE_EXCEL_PREFIX = EASYJ_OFFICE_PREFIX + ".excel";

	// Web
	String EASYJ_WEB_PREFIX = EASYJ_PREFIX + ".web";
	String EASYJ_WEB_CACHE304_PREFIX = EASYJ_WEB_PREFIX + ".cache304";

	//endregion

}
