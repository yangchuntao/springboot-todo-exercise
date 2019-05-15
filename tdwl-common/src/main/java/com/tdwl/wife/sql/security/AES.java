package com.tdwl.wife.sql.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AES {

    private static final String Algorithm = "AES/ECB/PKCS5Padding"; // 定义加密算法,模式和填充方式，建议这种方式，因为ios已经和他无缝对接
    private final static byte[] defaultKeyBytes = "dddfyc035864cxtt".getBytes();

    // src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            if (keybyte == null || keybyte.length == 0) {
                keybyte = defaultKeyBytes;
            }
            // 生成密钥
            SecretKeySpec key = new SecretKeySpec(keybyte, "AES");

            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, key);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    // src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            if (keybyte == null || keybyte.length == 0) {
                keybyte = defaultKeyBytes;
            }
            // 生成密钥

            SecretKeySpec key = new SecretKeySpec(keybyte, "AES");
            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, key);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 加密一个字符串
     *
     * @param src
     * @return 字节数组
     */
    public static byte[] encryptMode(String src) {
        if (src == null) {
            return null;
        }
        return encryptMode(defaultKeyBytes, src.getBytes());
    }

    /**
     * 加密一个字符串
     *
     * @param src
     * @return 字符串
     * @throws Exception
     */
    public static String encryptStrByHex(String src) throws Exception {
        if (src == null) {
            return null;
        }
        return HexByteEncoder.byteArr2HexStr(encryptMode(defaultKeyBytes, src.getBytes()));
    }

    /**
     * 加密一个字符串
     *
     * @param src
     * @return 字符串
     * @throws Exception
     */
    public static String encryptString(Object src) throws Exception {
        if (src == null) {
            return null;
        }
        return HexByteEncoder.byteArr2HexStr(encryptMode(defaultKeyBytes, String.valueOf(src).getBytes()));
    }

    /**
     * 解密一个字符串
     *
     * @param src
     * @return 字符串
     * @throws Exception
     */
    public static String decryptStrByHex(String src) throws Exception {
        if (src == null) {
            return null;
        }
        return new String(decryptMode(defaultKeyBytes, HexByteEncoder.hexStr2ByteArr(src)));
    }

    /**
     * 解密一个List<Map>
     *
     * @param
     * @return 字符串
     * @throws Exception
     */
    public static List encryptList(List list, String... encryptField) throws Exception {
        if (null != list) {
            for (Object object : list) {
                Map m = (Map) object;
                m = encryptMap(m, encryptField);
            }
        }
        return list;
    }

    /**
     * 加密一个Map
     *
     * @param src
     * @return 字符串
     * @throws Exception
     */
    public static Map encryptMap(Map map, String... encryptField) throws Exception {
        if (null != map) {
            Set<String> keySet = map.keySet();
            Iterator<String> itr = keySet.iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                String value = String.valueOf(map.get(key));
                String tmp = Arrays.toString(encryptField);
                Pattern p = Pattern.compile(key);
                Matcher m = p.matcher(tmp);
                if (null != value && m.find()) {
                    value = AES.encryptStrByHex(value);
                }
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 加密一个List<Map>
     *
     * @param src
     * @return 字符串
     * @throws Exception
     */
    public static List decryptList(List list, String... decryptField) throws Exception {
        if (null != list) {
            for (Object object : list) {
                Map m = (Map) object;
                m = decryptMap(m, decryptField);
            }
        }
        return list;
    }

    /**
     * 解密一个Map
     *
     * @param src
     * @return 字符串
     * @throws Exception
     */
    public static Map decryptMap(Map map, String... decryptField) throws Exception {
        if (null != map) {
            Set<String> keySet = map.keySet();
            Iterator<String> itr = keySet.iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                String value = String.valueOf(map.get(key));
                String tmp = Arrays.toString(decryptField);
                Pattern p = Pattern.compile(key);
                Matcher m = p.matcher(tmp);
                if (null != value && m.find()) {
                    value = AES.decryptStrByHex(value);
                }
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 解密一个字符串
     *
     * @param src
     * @return
     */
    public static byte[] decryptMode(String src) {
        if (src == null) {
            return null;
        }
        return decryptMode(defaultKeyBytes, src.getBytes());
    }

    /**
     * 解密一个字符串
     *
     * @param src
     * @return
     */
    public static byte[] decryptMode(byte src[]) {
        return decryptMode(defaultKeyBytes, src);
    }

    public static String encryptStrByHex(String key, String str) throws Exception {
        byte[] encryptMode = encryptMode(key.getBytes(), str.getBytes());
        return HexByteEncoder.byteArr2HexStr(encryptMode);
    }

    public static String encryptStrByBase64(String key, String str) throws Exception {
        byte[] encryptMode = encryptMode(key.getBytes(), str.getBytes());
        return Base64.getUrlEncoder().encodeToString(encryptMode);
    }

    public static String decryptStrByHex(String key, String str) throws Exception {
        byte[] encryptMode = decryptMode(key.getBytes(), HexByteEncoder.hexStr2ByteArr(str));
        return new String(encryptMode);
    }

    public static String decryptStrByBase64(String key, String str) throws Exception {
        byte[] encryptMode = decryptMode(key.getBytes(), Base64.getUrlDecoder().decode(str));
        return new String(encryptMode);
    }

}
