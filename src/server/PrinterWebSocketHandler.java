package server;

import common.CommonClass;
import common.LogType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

@ChannelHandler.Sharable
public class PrinterWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String message = ctx.channel().remoteAddress().toString() + "    " + CommonClass.i18nMessage.getString("connected");
        CommonClass.saveAndShow(message, LogType.ServiceData);
        ChannelMap.addChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String message = ctx.channel().remoteAddress() + "    " + CommonClass.i18nMessage.getString("closed");
        CommonClass.saveAndShow(message, LogType.ServiceData);
        String remoteAddress = ctx.channel().remoteAddress().toString();
        if (ChannelMap.hasChannel(remoteAddress)) {
            ChannelMap.removeChannel(remoteAddress);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof TextWebSocketFrame) { // 此处仅处理 Text Frame
            String remoteAddress = ctx.channel().remoteAddress().toString();
            if (ChannelMap.findChannel(remoteAddress) == null) {
                ChannelMap.addChannel(ctx.channel());
            }
            //进入消息解析,丢到数据处理中心
            String webSocketMessage = ((TextWebSocketFrame) webSocketFrame).text();
            webSocketMessage = webSocketMessage.replace("imagedata", "imagedata1").replace("QR Code(2D)", "QR Code").replace("objectName", "ObjectName");
            CommonClass.saveLog(remoteAddress + "    " + webSocketMessage, LogType.ServiceData);

            DataProcessing.process(remoteAddress, webSocketMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String message = ctx.channel().remoteAddress() + "    " + CommonClass.i18nMessage.getString("exception_caught") + cause.getMessage();
        CommonClass.saveAndShow(message, LogType.ErrorData);
        ChannelMap.removeChannel(ctx.channel().remoteAddress().toString());
    }
}
