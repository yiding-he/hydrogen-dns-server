package com.hyd.dnsserver.netty;

import com.hyd.dnsserver.data.DnsLookupService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.dns.*;
import io.netty.util.internal.SocketUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DNSHandler extends ChannelInboundHandlerAdapter {

    private final DnsLookupService dnsLookupService;

    public DNSHandler(DnsLookupService dnsLookupService) {
        this.dnsLookupService = dnsLookupService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramDnsQuery query = (DatagramDnsQuery) msg;
        DnsRecord queryRecord = query.recordAt(DnsSection.QUESTION);

        if (queryRecord.type() == DnsRecordType.PTR) {
            DefaultDnsPtrRecord responseRecord = new DefaultDnsPtrRecord(
                queryRecord.name(), queryRecord.dnsClass(), queryRecord.timeToLive(), "my-private-dns-server.com"
            );
            DnsResponse response = createResponse(query, responseRecord);
            ctx.channel().writeAndFlush(response);
        }

        if (queryRecord.type() == DnsRecordType.A) {
            String ipAddress = dnsLookupService.lookup(queryRecord.name());
            byte[] address = SocketUtils.addressByName(ipAddress).getAddress();
            ByteBuf buf = Unpooled.wrappedBuffer(address);
            DnsRecord responseRecord = new DefaultDnsRawRecord(
                queryRecord.name(), queryRecord.type(), 3600, buf
            );
            DnsResponse response = createResponse(query, responseRecord);
            ctx.channel().writeAndFlush(response);
        }

        if (queryRecord.type() == DnsRecordType.AAAA) {
            DnsResponse response = createResponse(query, null);
            ctx.channel().writeAndFlush(response);
        }
    }

    private DatagramDnsResponse createResponse(DatagramDnsQuery query, DnsRecord record) {
        DatagramDnsResponse response = new DatagramDnsResponse(query.recipient(), query.sender(), query.id());
        if (record != null) {
            response.addRecord(DnsSection.ANSWER, record);
        }
        return response;
    }
}
