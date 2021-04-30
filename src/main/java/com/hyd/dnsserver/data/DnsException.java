package com.hyd.dnsserver.data;

public class DnsException extends RuntimeException {

    public DnsException() {
    }

    public DnsException(String message) {
        super(message);
    }

    public DnsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DnsException(Throwable cause) {
        super(cause);
    }
}
