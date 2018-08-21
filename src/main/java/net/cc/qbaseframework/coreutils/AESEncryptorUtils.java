package net.cc.qbaseframework.coreutils;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description AES加密工具
 */

public class AESEncryptorUtils
{
	private final static int ANDROID_PLATFORM_4_2 = 17;

	/**
	 * AES加密
	 */
	public static String encrypt(String seed, String cleartext) throws Exception
	{
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}

	/**
	 * AES解密
	 */
	public static String decrypt(String seed, String encrypted) throws Exception
	{
		if (seed == null || "".equals(seed) || encrypted == null || "".equals(encrypted))
		{
			return null;
		}
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception
	{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = null;
		if (android.os.Build.VERSION.SDK_INT >= ANDROID_PLATFORM_4_2)
		{
			sr = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
		}
		else
		{
			sr = SecureRandom.getInstance("SHA1PRNG");
		}
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception
	{
		if (raw == null || clear == null)
		{
			return null;
		}
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception
	{
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt)
	{
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex)
	{
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString)
	{
		if (hexString == null || "".equals(hexString))
		{
			return null;
		}
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
		{
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}

	public static String toHex(byte[] buf)
	{
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++)
		{
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b)
	{
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	static final class CryptoProvider extends Provider {
		/**
		 * Creates a Provider and puts parameters
		 */
		public CryptoProvider() {
			super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
			put("SecureRandom.SHA1PRNG",
					"org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
			put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
		}
	}
}
