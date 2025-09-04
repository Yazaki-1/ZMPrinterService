package server;

import common.CommonClass;
import common.LogType;
import io.netty.channel.*;


@ChannelHandler.Sharable
public class PrinterTcpSocketHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String message = ctx.channel().remoteAddress().toString() + "    " + CommonClass.i18nMessage.getString("connected");
        CommonClass.saveAndShow(message, LogType.ServiceData);
        ChannelMap.addChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg);
        String remoteAddress = ctx.channel().remoteAddress().toString();
        if (ChannelMap.findChannel(remoteAddress) == null) {
            ChannelMap.addChannel(ctx.channel());
        }

        String message = msg.toString();
        message = message.replace("imagedata", "imagedata1").replace("QR Code(2D)", "QR Code").replace("objectName", "ObjectName");
        CommonClass.saveLog(remoteAddress + "    " + message, LogType.ServiceData);

        DataProcessing.process(remoteAddress, message);
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String message = ctx.channel().remoteAddress() + "    " + CommonClass.i18nMessage.getString("exception_caught") + cause.getMessage();
        CommonClass.saveAndShow(message, LogType.ErrorData);
        ChannelMap.removeChannel(ctx.channel().remoteAddress().toString());
    }
}