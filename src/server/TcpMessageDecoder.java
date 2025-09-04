package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TcpMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String message = new String(bytes, "GBK");
        String[] messageArr = message.split("\\|");
        int len = Integer.parseInt(messageArr[0]); // len|data的len
        int head = messageArr[0].length() + 1; // len和|的占位长度
        int bodyLength = message.length() - head; // data的长度
        if (bodyLength == len) { // 如果data的长度等于len
            list.add(message.substring(head)); // 去掉消息头"len|"取data数据
        } else {
            if (bodyLength < len) { // 如果data长度小于len,说明包被拆分,需要重定位byte读取
                byteBuf.resetReaderIndex();
            } else { // 如果data长度大于len,说明被粘包,需要裁掉head,右移head取len长度代表分包取源数据
                String packet = message.substring(head, len + head);
                list.add(packet);
            }
        }
    }
}
