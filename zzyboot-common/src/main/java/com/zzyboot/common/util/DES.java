package com.zzyboot.common.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DES {
private static final String DES_ALGORITHM = "DES";
public static final String key = "5f1354c7268451d0dcc5f21da45a96ca063e2dc5bd691768edd15603372f990e";
//一个人在路上
	public static String encryption(String plainData) throws Exception {
		return encryption(plainData, key);
	}

	public static String encryption(String plainData, String secretKey) throws Exception {
		if(plainData == null || plainData.length() < 1){
			return plainData;
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			
		} try {
			// 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must
			// be multiple of 8 when decrypting with padded cipher异常,
			// 不能把加密后的字节数组直接转换成字符串 
			byte[] buf = cipher.doFinal(plainData.getBytes());
			return Base64Utils.encode(buf);
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
				throw new Exception("IllegalBlockSizeException", e);
			} catch (BadPaddingException e) {
				e.printStackTrace();
				throw new Exception("BadPaddingException", e);
			}
		}
    public static String decryption(String secretData) throws Exception {
    	return decryption(secretData,key);
    	
    }
		
	    public static String decryption(String secretData, String secretKey) throws Exception {
	    	System.out.println("decryption:["+secretData+"]["+secretKey+"]");
	    	Cipher cipher = null;
	    	try {
	    		cipher = Cipher.getInstance(DES_ALGORITHM);
	    		cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
	    	}
	    	catch (NoSuchAlgorithmException e) {
	    		e.printStackTrace();
	    		throw new Exception("NoSuchAlgorithmException", e);
	    	} catch (NoSuchPaddingException e) {
	    		e.printStackTrace();
	    		throw new Exception("NoSuchPaddingException", e);
	    	} catch (InvalidKeyException e) {
	    		e.printStackTrace();
	    		throw new Exception("InvalidKeyException", e);
	    	} try {
	    		byte[] buf = cipher.doFinal(Base64Utils.decode(secretData.toCharArray()));
	    		return new String(buf);
	    	} catch (IllegalBlockSizeException e) {
	    		e.printStackTrace();
	    		throw new Exception("IllegalBlockSizeException", e);
	    	} catch (BadPaddingException e) {
	    		e.printStackTrace();
	    		throw new Exception("BadPaddingException", e);
	    	}
	    }
	   
	    private static SecretKey generateKey(String secretKey)
	    		throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
	    	DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
	    	keyFactory.generateSecret(keySpec);
	    	return keyFactory.generateSecret(keySpec);
	    }
	   
		static private class Base64Utils{
			static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=" .toCharArray();
			static private byte[] codes = new byte[256];
			static {
				for (int i = 0; i < 256; i++)
					codes[i] = -1;
				for (int i = 'A'; i <= 'Z'; i++)
					codes[i] = (byte) (i - 'A');
				for (int i = 'a'; i <= 'z'; i++)
					codes[i] = (byte) (26 + i - 'a');
				for (int i = '0'; i <= '9'; i++)
					codes[i] = (byte) (52 + i - '0');
				codes['+'] = 62; codes['/'] = 63;
			}
			
			
			static private String encode(byte[] data){
				char[] out = new char[((data.length + 2) / 3) * 4];
				for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
					boolean quad = false;
					boolean trip = false;
					int val = (0xFF & (int) data[i]);
					val <<= 8;
					if ((i + 1) < data.length) {
						val |= (0xFF & (int) data[i + 1]);
						trip = true;
					}
					val <<= 8;
					if ((i + 2) < data.length) {
						val |= (0xFF & (int) data[i + 2]);
						quad = true;
					}
					out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
					val >>= 6;
					out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
					val >>= 6;
					out[index + 1] = alphabet[val & 0x3F];
					val >>= 6;
					out[index + 0] = alphabet[val & 0x3F];
				}
				return new String(out);
				
			}
			
			static private byte[] decode(char[] data){
				return decode(data, false);
			}
			static private byte[] decode(char[] data, boolean isEqual){
				int datalength = data.length;
				if(isEqual){
					datalength--;
				}
				 int len = ((datalength + 3) / 4) * 3;
				 if (datalength > 0 && data[datalength - 1] == '=') --len;
				 if (datalength > 1 && data[datalength - 2] == '=') --len;
				 byte[] out = new byte[len];
				 int shift = 0;
				 int accum = 0;
				 int index = 0;
				 for (int ix = 0; ix < datalength; ix++) {
					 int value = codes[data[ix] & 0xFF];
					 if (value >= 0) {
						 accum <<= 6;
						 shift += 6;
						 accum |= value;
						 if (shift >= 8) {
							 shift -= 8;
							 out[index++] = (byte) ((accum >> shift) & 0xff);
						}
					}
				 }
				 if (index != out.length){
					 if (!isEqual && datalength > 0 && data[datalength - 1] == '='){
						 return decode(data, true);
					 }
					 System.out.println("index["+index+"] != out.length["+out.length+"]");
					 throw new Error("miscalculated data length!");
				 }
				 return out;
			}
		}

	  
	    /*public static void main(String[] args) throws Exception {
	        //待加密内容
	    	String str = "http://localhost:8085/super.html";
	    	
	    	String strSe = DES.encryption(str, key);
	    	System.out.println("strSe is " + strSe);
	    	strSe = "Ns5R3IbQMdJopyQdM5kM5g==";
	        System.out.println(strSe);
	        System.out.println(DES.decryption(strSe, key));
	    }*/
}
