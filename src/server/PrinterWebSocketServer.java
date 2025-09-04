package server;

import common.CommonClass;
import common.LogType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

public class PrinterWebSocketServer {

    private final int port;

    public PrinterWebSocketServer(int port) {
        this.port = port;
    }

    public void start_server() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    pipeline.addLast(new HttpServerCodec()); // HTTP 协议解析，用于握手阶段
                                    pipeline.addLast(new HttpObjectAggregator(65535)); // HTTP 协议解析，用于握手阶段
                                    pipeline.addLast(new WebSocketServerCompressionHandler()); // WebSocket 数据压缩扩展
                                    pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true, 65536 * 2)); // WebSocket 握手、控制帧处理
                                    pipeline.addLast(new WebSocketFrameAggregator(16 * 1024 * 1024)); // websocket帧聚合
                                    pipeline.addLast(new PrinterWebSocketHandler());
                                }
                            }
                    )
                    .option(ChannelOption.SO_BACKLOG, 1024);

            // 绑定端口，开始接收进来的连接
            Channel channel = bootstrap.bind(port).sync().channel();
            String message = CommonClass.i18nMessage.getString("ws") + " " + port + "\n";
            CommonClass.saveAndShow(message, LogType.ServiceData);
            //关闭channel和块，直到它被关闭
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
