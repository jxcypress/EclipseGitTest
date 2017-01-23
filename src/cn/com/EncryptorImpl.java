package cn.com.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;

/**
 * @author cypress
 *         加密解密算法
 */
public class EncryptorImpl {

    private static final String KEY_HEX = "AC:ED:00:05:73:72:00:1E:63:6F:6D:2E:73:75:6E:2E:63:72:79:70:74:6F:2E" +
            ":70:72:6F:76:69:64:65:72:2E:44:45:53:4B:65:79:6B:34:9C:35:DA:15:68:98:02" +
            ":00:01:5B:00:03:6B:65:79:74:00:02:5B:42:78:70:75:72:00:02:5B:42:AC:F3:17:F8" +
            ":06:08:54:E0:02:00:00:78:70:00:00:00:08:25:DA:97:04:45:83:A8:C7";
    private static final String ALGORITHM = "DES";
    private static final String CHARSET_NAME = "ISO-8859-1";
    private SecretKey desKey = null;
    private Cipher c1 = null;

    public EncryptorImpl() throws Exception {
        byte[] keyByte = this.hex2byte(this.KEY_HEX);
        ByteArrayInputStream intemp = new ByteArrayInputStream(keyByte);
        ObjectInputStream in = new ObjectInputStream(intemp);
        this.desKey = (SecretKey) in.readObject();
        intemp.close();
        in.close();
        this.c1 = Cipher.getInstance(ALGORITHM);
    }

    /**
     * 加密字符串
     *
     * @param content 被加密字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encode(String content) throws Exception {
        if (content == null) {
            return null;
        } else {
            this.c1.init(1, this.desKey);
            byte[] cipherByte = this.c1.doFinal(content.getBytes(CHARSET_NAME));
            return toHexString(new String(cipherByte, CHARSET_NAME));
        }
    }

    /**
     * 解密字符串
     *
     * @param encoded 被解密字符串
     * @return 原字符内容
     * @throws Exception
     */
    public String decode(String encoded) throws Exception {
        if (encoded == null) {
            return null;
        } else {
            encoded = toString(encoded);
            this.c1.init(2, this.desKey);
            byte[] clearByte = this.c1.doFinal(encoded.getBytes(CHARSET_NAME));
            return new String(clearByte, CHARSET_NAME);
        }
    }

    private byte[] hex2byte(String src) {
        StringTokenizer st = new StringTokenizer(src, ":");
        int count = st.countTokens();
        String[] hexArray = new String[count];
        for (int retByte = 0; retByte < count; ++retByte) {
            hexArray[retByte] = st.nextToken();
        }
        byte[] var9 = new byte[hexArray.length];
        for (int i = 0, len = hexArray.length; i < len; ++i) {
            String tempStr = hexArray[i];
            int tt = Integer.parseInt(tempStr.trim(), 16);
            var9[i] = (byte) tt;
        }

        return var9;
    }

    public static String toHexString(String content) {
        StringBuilder sb = new StringBuilder();
        char[] contentArray = content.toCharArray();
        for (int i = 0, len = contentArray.length; i < len; ++i) {
            sb.append(Integer.toHexString(contentArray[i]));
            if (i != len - 1) {
                sb.append(":");
            }
        }

        return sb.toString();
    }

    public static String toString(String hexContent) {
        StringBuilder sb = new StringBuilder();
        String[] hexContentArray = hexContent.split(":");
        for (int index = 0, len = hexContentArray.length; index < len; ++index) {
            String hexChar = hexContentArray[index];
            int i = Integer.parseInt(hexChar, 16);
            char c = (char) i;
            sb.append(String.valueOf(c));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        EncryptorImpl encryptor = new EncryptorImpl();
        String encode = encryptor.encode("&*@.163.com^");
        System.out.println(encode);
        System.out.println(encryptor.decode(encode));
    }
}
