package server;

import com.ZMPrinter.conn.ConnectException;
import common.CommonClass;
import common.LogType;
import function.FuncLabelCreator;
import function.FunctionalException;
import utils.DataUtils;
import data_processing.JsonData;
import data_processing.LabelBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

@ChannelHandler.Sharable
public class PrinterWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String message = ctx.channel().remoteAddress().toString() + "    已连接.";
        CommonClass.saveAndShow(message, LogType.ServiceData);
        ChannelMap.addChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String message = ctx.channel().remoteAddress() + "    已断开.";
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
            try {
                // 如果是Json
                JsonData jsonData = DataUtils.fromJson(webSocketMessage);
                LabelBuilder.build(jsonData, remoteAddress);
            } catch (FunctionalException e) { // lsf模板打印的时候选择了preview抛出的异常、未定义的Operator抛出的异常、lsf文件找不到路径异常、json反序列化填充数据异常
                CommonClass.saveAndShow(remoteAddress + "    " + e.getMessage(), LogType.ErrorData);
                ChannelMap.writeMessageToClient(remoteAddress, e.getMessage());
            } catch (Exception e) { // json序列化异常
                // 函数式调用
                try {
                    FuncLabelCreator.analysis(remoteAddress, webSocketMessage);
                } catch (ConnectException | FunctionalException exception) {
                    String message = exception.getMessage();
                    CommonClass.saveAndShow(remoteAddress + "    " + message, LogType.ErrorData);
                    ChannelMap.writeMessageToClient(remoteAddress, message);
                } catch (Exception otherException) {
                    String message = "4005|其他异常:" + otherException.getMessage();
                    CommonClass.saveAndShow(remoteAddress + "    " + message, LogType.ErrorData);
                    ChannelMap.writeMessageToClient(remoteAddress, message);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String message = ctx.channel().remoteAddress() + "    触发异常:" + cause.getMessage();
        CommonClass.saveAndShow(message, LogType.ErrorData);
        ChannelMap.removeChannel(ctx.channel().remoteAddress().toString());
    }
}
