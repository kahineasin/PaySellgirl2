package com.sellgirl.sellgirlPayWeb.projHelper;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
//import weaver.integration.logging.Logger;
//import weaver.integration.logging.LoggerFactory;
//import weaver.interfaces.encode.IEncode;

public class DES_IV// implements IEncode
{
	//private static Logger newlog = LoggerFactory.getLogger(DES_IV.class);
	private String pwd = null;//密钥
	private String iv = null;//向量

	public DES_IV() {
	}

	public DES_IV(String paramString1, String paramString2) {
		pwd = paramString1;
		iv = paramString2;
	}

	public String encode(String paramString) {
		byte[] arrayOfByte1 = null;
		try {
			if (pwd == null) {
				pwd = "ecology9";
			}
			DESKeySpec localDESKeySpec = new DESKeySpec(pwd.getBytes());
			SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey localSecretKey = localSecretKeyFactory.generateSecret(localDESKeySpec);
			if (iv == null) {
				iv = "ecology9";
			}
			IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv.getBytes());

			Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			localCipher.init(1, localSecretKey, localIvParameterSpec);
			byte[] arrayOfByte2 = paramString.getBytes();
			arrayOfByte1 = localCipher.doFinal(arrayOfByte2);
		} catch (Exception localException) {
			//newlog.error("加密异常！", localException);
			return null;
		}
		return parseByte2HexStr(arrayOfByte1);
	}

	public String decode(String paramString) {
		byte[] arrayOfByte1 = null;
		byte[] arrayOfByte2 = parseHexStr2Byte(paramString);
		try {
			if (pwd == null) {
				pwd = "ecology9";
			}
			DESKeySpec localDESKeySpec = new DESKeySpec(pwd.getBytes());
			SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey localSecretKey = localSecretKeyFactory.generateSecret(localDESKeySpec);
			if (iv == null) {
				iv = "ecology9";
			}
			IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv.getBytes());

			Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			localCipher.init(2, localSecretKey, localIvParameterSpec);
			arrayOfByte1 = localCipher.doFinal(arrayOfByte2);
		} catch (Exception localException) {
			//newlog.error("解密异常！", localException);
			return null;
		}
		return new String(arrayOfByte1);
	}

	public boolean setPwd(String paramString) {
		pwd = paramString;
		return true;
	}

	public boolean setIv(String paramString) {
		iv = paramString;
		return true;
	}

	private String parseByte2HexStr(byte[] paramArrayOfByte) {
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < paramArrayOfByte.length; i++) {
			String str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
			if (str.length() == 1) {
				str = '0' + str;
			}
			localStringBuffer.append(str.toUpperCase());
		}
		return localStringBuffer.toString();
	}

	private byte[] parseHexStr2Byte(String paramString) {
		if (paramString.length() < 1) {
			return null;
		}
		byte[] arrayOfByte = new byte[paramString.length() / 2];
		for (int i = 0; i < paramString.length() / 2; i++) {
			int j = Integer.parseInt(paramString.substring(i * 2, i * 2 + 1), 16);
			int k = Integer.parseInt(paramString.substring(i * 2 + 1, i * 2 + 2), 16);
			arrayOfByte[i] = ((byte) (j * 16 + k));
		}
		return arrayOfByte;
	}

	public static void main(String[] paramArrayOfString) {
		DES_IV localDES_IV = new DES_IV();
		String str1 = "SHTC201901003";

		String str2 = "ecology9";

		String str3 = "ecology9";
		localDES_IV.setPwd(str2);
		localDES_IV.setIv(str3);

		String str4 = localDES_IV.encode(str1);

		String str5 = localDES_IV.decode(str4);
		System.out.println("被加密参数："+str1);
		System.out.println("加密密钥："+str2);
		System.out.println("加密向量："+str3);
		System.out.println("加密后："+str4);
		System.out.println("解密后："+str5);
	}
}
