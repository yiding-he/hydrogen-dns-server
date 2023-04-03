package com.hyd.dnsserver.util;

public class IpAddressUtil {

    public static final String IPV4_REGEX = "^" +
            "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static final String IPV6_REGEX = "^" +
            "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|" +
            "([0-9a-fA-F]{1,4}:){1,7}:|" +
            "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
            "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
            "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
            "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
            "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
            "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
            ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
            "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +
            "::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|" +
            "([0-9a-fA-F]{1,4}:){1,4}:([0-9a-fA-F]{1,4}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,4}(25[0-5]|" +
            "(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";

    public static boolean isIpAddress(String s) {
        return s != null && (s.matches(IPV4_REGEX) || s.matches(IPV6_REGEX));
    }

    public static byte[] toBytes(String ip) {
        if (!isIpAddress(ip)) {
            throw new IllegalArgumentException("'" + ip + "'不是合法的IP地址");
        }
        String[] ipv4Segments = ip.split("\\."); // 使用 \\. 进行转义
        byte[] ipv4Bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            ipv4Bytes[i] = (byte) Integer.parseInt(ipv4Segments[i]);
        }
        return ipv4Bytes;
    }
}
