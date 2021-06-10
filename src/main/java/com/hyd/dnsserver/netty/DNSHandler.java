package com.hyd.dnsserver.netty;

import com.hyd.dnsserver.data.DnsLookupService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.dns.*;
import io.netty.util.internal.SocketUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        if (query.opCode() == DnsOpCode.QUERY) {
            if (queryRecord.type() == DnsRecordType.PTR) {
                DefaultDnsPtrRecord responseRecord = new DefaultDnsPtrRecord(
                    queryRecord.name(), DnsRecord.CLASS_IN, 250, "my-private-dns-server.com"
                );
                DnsResponse response = createResponse(query, Collections.singletonList(responseRecord));
                ctx.channel().writeAndFlush(response);
                return;
            }

            if (queryRecord.type() == DnsRecordType.A) {
                List<String> ipAddresses = dnsLookupService.lookup(DnsRecordType.A, queryRecord.name());
                DnsResponse response = createResponse(query, createDnsRecords(queryRecord, ipAddresses));
                ctx.channel().writeAndFlush(response);
                return;
            }

            if (queryRecord.type() == DnsRecordType.AAAA) {
                List<String> ipAddresses = dnsLookupService.lookup(DnsRecordType.AAAA, queryRecord.name());
                DnsResponse response = createResponse(query, createDnsRecords(queryRecord, ipAddresses));
                ctx.channel().writeAndFlush(response);
                return;
            }
        }

        DatagramDnsResponse response = new DatagramDnsResponse(query.recipient(), query.sender(), query.id());
        response.setCode(DnsResponseCode.NOTIMP);
        ctx.channel().writeAndFlush(response);
    }

    /**
     * 根据查询请求记录和解析结果构建 DnsRecord 对象
     *
     * @param queryRecord 查询请求记录
     * @param ipAddresses 解析结果
     */
    private List<DnsRecord> createDnsRecords(
        DnsRecord queryRecord, List<String> ipAddresses
    ) throws UnknownHostException {

        List<DnsRecord> records = new ArrayList<>();
        for (String ipAddress : ipAddresses) {
            byte[] address = SocketUtils.addressByName(ipAddress).getAddress();
            ByteBuf buf = Unpooled.wrappedBuffer(address);

            DefaultDnsRawRecord record =
                new DefaultDnsRawRecord(queryRecord.name(), queryRecord.type(), 3600, buf);

            records.add(record);
        }
        return records;
    }

    /**
     * 构建 DnsResponse 对象
     *
     * @param query   查询请求
     * @param records 解析结果
     */
    private DatagramDnsResponse createResponse(DatagramDnsQuery query, List<DnsRecord> records) {
        DatagramDnsResponse response = new DatagramDnsResponse(query.recipient(), query.sender(), query.id());
        response.setRecursionAvailable(true);
        response.setRecursionDesired(query.isRecursionDesired());
        response.addRecord(DnsSection.QUESTION, query.recordAt(DnsSection.QUESTION));
        records.forEach(record -> response.addRecord(DnsSection.ANSWER, record));
        return response;
    }
}
