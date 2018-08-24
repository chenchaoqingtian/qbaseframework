package net.cc.qbaseframework.coreutils;

import java.security.MessageDigest;

/**
 * MD5加密工具
 */
public class EncryptUtils {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * MD5加密字符
	 * @param orginStr 明码
	 * @return
	 */
	public static String encryptByMD5(String orginStr) {
		if (!StringUtils.isEmpty(orginStr)) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(orginStr.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return orginStr;
	}

	public static String encryptBySHA1(String orginStr) {
		if (!StringUtils.isEmpty(orginStr)) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] results = md.digest(orginStr.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return orginStr;
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String[] args) {
		System.out.println(encryptByMD5("123456"));
	}

}
