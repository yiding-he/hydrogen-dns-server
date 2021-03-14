package com.hyd.dnsserver.netty;

import com.hyd.dnsserver.data.DnsLookupService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.DatagramDnsQueryDecoder;
import io.netty.handler.codec.dns.DatagramDnsResponseEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DNSServer {

    private int port = 53;

    private final DnsLookupService dnsLookupService;

    public DNSServer(DnsLookupService dnsLookupService) {
        this.dnsLookupService = dnsLookupService;
    }

    public void start() {
        new Bootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioDatagramChannel.class)
            .handler(new ChannelInitializer<NioDatagramChannel>() {
                @Override
                protected void initChannel(NioDatagramChannel ch) {
                    ch.pipeline()
                        .addLast(new DatagramDnsResponseEncoder())
                        .addLast(new DatagramDnsQueryDecoder())
                        .addLast(new DNSHandler(dnsLookupService));
                }
            })
            .bind(port);
        log.info("DNS Server started at port {}", this.port);
    }
}
