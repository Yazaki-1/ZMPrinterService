package server;

import common.CommonClass;
import common.LogType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class PrinterTcpSocketServer {
    private final int port;

    public PrinterTcpSocketServer(int port) {
        this.port = port;
    }

    public void start_server() throws InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        EventLoopGroup connectExecutors = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(eventExecutors, connectExecutors)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)//3.保存连接数
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            channelPipeline.addLast(new TcpMessageDecoder());
                            channelPipeline.addLast(new StringEncoder(Charset.forName("GBK")));
                            channelPipeline.addLast(new PrinterTcpSocketHandler());
                        }
                    });

            // 绑定端口，开始接收进来的连接
            Channel channel = bootstrap.bind(port).sync().channel();
            String message = CommonClass.i18nMessage.getString("tcp") + port + "\n";
            CommonClass.saveAndShow(message, LogType.ServiceData);
            //关闭channel和块，直到它被关闭
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new InterruptedException(e.getMessage());
        } finally {
            eventExecutors.shutdownGracefully();
            connectExecutors.shutdownGracefully();
        }
    }
}
