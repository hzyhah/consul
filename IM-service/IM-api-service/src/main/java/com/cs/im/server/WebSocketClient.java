package com.cs.im.server;


import com.cs.im.handle.WebSocketClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * @Description WebSocketClient
 * @Author honry.guan
 * @Date 2020/5/25 10:47
 */
public class WebSocketClient {
    /*public static final String URL = System.getProperty("url", "wss://stream.binance.com:9443/ws/BNBBTC@kline_1m");*/
    public static final String URL = System.getProperty("url", "ws://127.0.0.1:9091/ws");

    public static void main(String[] args) throws Exception {
        URI uri = new URI(URL);
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        final int port = uri.getPort();

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.out.println("只支持ws");
            return;
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        } else {
            sslCtx = null;
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final WebSocketClientHandler handler = new WebSocketClientHandler
                        (WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(socketChannel.alloc(), host, port));
                            }
                            //http协议握手
                            p.addLast(new HttpClientCodec());
                            p.addLast(new HttpObjectAggregator(8192));
                            //支持websocket数据压缩
                            p.addLast(WebSocketClientCompressionHandler.INSTANCE);
                            p.addLast(handler);
                        }
                    });
            //连接服务器
            Channel ch = b.connect(uri.getHost(), port).sync().channel();
            //等待握手完成
            handler.handshakeFuture().sync();

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg == null) {
                    break;
                } else if ("bye".equals(msg.toLowerCase())) {
                    ch.writeAndFlush(new CloseWebSocketFrame());
                    ch.closeFuture().sync();
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {
                    WebSocketFrame frame = new PingWebSocketFrame(
                            Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
                    ch.writeAndFlush(frame);
                } else {
                    WebSocketFrame frame = new TextWebSocketFrame(msg);
                    ch.writeAndFlush(frame);
                }
            }
        } finally {
            group.shutdownGracefully();
        }
    }

}
