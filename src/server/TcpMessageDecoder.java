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
        System.out.println(message);
        String[] messageArr = message.split("\\|");
        System.out.println(messageArr[0]);
        System.out.println(messageArr[1].length());
        if (messageArr[1].length() == Integer.parseInt(messageArr[0])) {
            list.add(messageArr[1]);
        }else {
            byteBuf.resetReaderIndex();
        }
    }
}
