package com.timego.calculcator.utils;

public class JavaHexStr
{



    public static String getCRCNew(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        return getCRCNew(para);
    }
    /**
     * 计算CRC16校验码
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRCNew(byte[] bytes) {
        int wCRCin = 0x0000; // initial value 65535
        int wCPoly = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xffff;
        // 结果转换为16进制
        String result = Integer.toHexString(wCRCin).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        return result.substring(2, 4) + "" + result.substring(0, 2);
    }




    public static String getCRC2(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        return getCRC(para);
    }
    /**
     * 计算CRC16校验码
     *
     * @param bytes
     *            字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRC(byte[] bytes) {
        // CRC寄存器全为1
        int CRC = 0x0000ffff;
        // 多项式校验值
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        // 结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        //高位在前地位在后
        //return result.substring(2, 4) + " " + result.substring(0, 2);
        // 交换高低位，低位在前高位在后
        return result.substring(2, 4) +  result.substring(0, 2);
    }


    public static int CRC16_XMODEM(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0x1021;
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }

    /**
     * 十进制字符串转BCD码。举例：
     * 如果十进制字符串为"2"，转换后的BCD码为00000010
     * 如果十进制字符串为"12"，转换后的BCD码为00010010
     * 如果十进制字符串为"123"，转换后的BCD码为00000001 00100011
     * @param decStr 十进制字符串
     * @return BCD码
     * @throws Exception
     */
    public static byte[] decimalStringToBcd(String decStr) throws Exception {
        // 因为可能修改字符串的内容，所以构造StringBuffer
        StringBuffer sb = new StringBuffer(decStr);
        // 一个字节包含两个4位的BCD码，byte数组中要包含偶数个BCD码
        // 一个十进制字符对应4位BCD码，所以如果十进制字符串的长度是奇数，要在前面补一个0使长度成为偶数
        if ((sb.length() % 2) != 0) {
            sb.insert(0, '0');
        }

        // 两个十进制数字转换为BCD码后占用一个字节，所以存放BCD码的字节数等于十进制字符串长度的一半
        byte[] bcd = new byte[sb.length() / 2];
        for (int i = 0; i < sb.length();) {
            if (!Character.isDigit(sb.charAt(i)) || !Character.isDigit(sb.charAt(i + 1))) {
                throw new Exception("传入的十进制字符串包含非数字字符!");
            }
            // 每个字节的构成：用两位十进制数字运算的和填充，高位十进制数字左移4位+低位十进制数字
            bcd[i/2] = (byte)((Character.digit(sb.charAt(i), 10) << 4) + Character.digit(sb.charAt(i + 1), 10));
            // 字符串的每两个字符取出来一起处理，所以此处i的自增长要加2，而不是加1
            i += 2;
        }
        return bcd;
    }


    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
            System.out.format("%02X\n", bbt[p]);
        }
        return bbt;
    }
    private static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
            System.out.format("%02X\n", bcd[i]);
        }
        return bcd;
    }

    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }


}

