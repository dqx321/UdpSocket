package com.example.udp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.Util.LogUtils;
import com.example.Util.ThreadPoolUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author CIDI
 */
public class UdpSocket {

    //存储socket
    private static HashMap<Integer, DatagramSocket> socketHashMap=new HashMap<>();
    private static ConnectState mconnectState;
    private static byte[] data=new byte[1024*1024];
    private static String mIpAddress;

    private static Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //失败
                case 11:
                    if(mconnectState!=null) {
                        String mport=msg.obj.toString();
                        mconnectState.connectFail(mport);
                    }
                    break;
                    //成功
                case 22:
                    if(mconnectState!=null){
                        String[] result=msg.obj.toString().split("&");
                        mconnectState.connectSuccess(result[1], Integer.valueOf(result[0]));
                    }
                    break;
                    default:
                        break;
            }
        }
    };


    public static  void receive(final String ipAddress, final int port){
        mIpAddress=ipAddress;
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                boolean  isContinous=true;
                DatagramSocket datagramSocket=socketHashMap.get(port);
                try {
                    if(datagramSocket == null){
                        datagramSocket=new DatagramSocket(port);
                        socketHashMap.put(port,datagramSocket);
                    }
                    DatagramPacket datagramPacket=new DatagramPacket(data,data.length);
                    while (isContinous){
                        if(socketHashMap.get(port) != null ){
                            socketHashMap.get(port).receive(datagramPacket);
                            LogUtils.e("UDPSocket","接收数据--->"+port+":"+new String(data,0,datagramPacket.getLength()));
                            handler.sendMessage(handler.obtainMessage(22,port+"&"+new String(data,0,datagramPacket.getLength())));
                        }else {
                            handler.sendMessage(handler.obtainMessage(11,port+"&"+new String(data,0,datagramPacket.getLength())));
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }


    public static synchronized void sendInfo(final String addressIP, final int port, final String msg){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                byte[] data=msg.getBytes();
                try {
                    if(socketHashMap.get(port) != null){
                        DatagramPacket datagramPacket=new DatagramPacket(data,data.length, InetAddress.getByName(addressIP),port);
                        socketHashMap.get(port).send(datagramPacket);
                    }else {
                        handler.sendMessage(handler.obtainMessage(11, String.valueOf(port+"&"+bytesToHexString(data))));
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ThreadPoolUtils.execute(runnable);
    }

    public static void onCloseAll(){
        Set<Integer> socketSet=socketHashMap.keySet();
        handler.removeCallbacks(null);
        if(socketSet.iterator().hasNext()){
            DatagramSocket datagramSocket=socketHashMap.get(socketSet.iterator().next());
            if(!datagramSocket.isClosed()){
                datagramSocket.close();
                datagramSocket=null;
            }
        }
    }


    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        //01 74686973 98523614 74241536 2536 1425 ff
        return result;
    }

    public interface ConnectState{
        //接收到的数据
        void connectSuccess(String result, int port);
        //连接失败
        void connectFail(String port);
    }
    //socket连接监听
    public static void ConnectStateListener(ConnectState connectState){
        mconnectState=connectState;
    }

}
