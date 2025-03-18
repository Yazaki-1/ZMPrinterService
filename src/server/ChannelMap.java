package server;

import data_processing.LabelData;
import data_processing.PrintQueue;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ChannelMap {
    private static final ConcurrentMap<String, Channel> ChannelMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, PrintQueue> queueConcurrentMap = new ConcurrentHashMap<>();

    public static void addChannel(Channel channel) {
        String remoteAddress = channel.remoteAddress().toString();
        ChannelMap.put(remoteAddress, channel);
        queueConcurrentMap.put(remoteAddress, new PrintQueue(new LinkedBlockingQueue<>(200)));
    }

    public static void removeChannel(String remoteAddress) {
        ChannelMap.remove(remoteAddress);
        queueConcurrentMap.get(remoteAddress).releaseQueue();
        queueConcurrentMap.remove(remoteAddress);
    }

    public static Channel findChannel(String remoteAddress) {
        return ChannelMap.get(remoteAddress);
    }

    public static void addQueue(String remoteAddress, LabelData labelData) {
        try {
            queueConcurrentMap.get(remoteAddress).addQueue(labelData);
        }catch (Exception e) {
            writeMessageToClient(remoteAddress, "PrintQueue|打印任务已满,请等候");// 返回队列已满的消息
        }
    }

    public static boolean hasChannel(String remoteAddress) {
        return ChannelMap.containsKey(remoteAddress);
    }

    public static void startQueue(String remoteAddress) {
        queueConcurrentMap.get(remoteAddress).startPrint();
    }

    public static void cleanQueue(String remoteAddress) {
        queueConcurrentMap.get(remoteAddress).cleanQueue();
    }

    public static void writeMessageToClient(String remoteAddress, String serverMessage) {
        Channel channel = findChannel(remoteAddress);
        channel.writeAndFlush(new TextWebSocketFrame(serverMessage));
    }
}
