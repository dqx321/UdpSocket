# UdpSocket
功能：1.UDP通讯工具类。
       接收消息：
      
      UDPSocket.ConnectStateListener(new UDPSocket.ConnectState() {
            @Override
            public void connectSuccess(String result, int port) {
                
            }

            @Override
            public void connectFail(String port) {

            }
        });
        UDPSocket.receive(ip, 9998);
        

       发送消息: 
       
       UDPSocket.sendInfo(IP, PORT, msg);
       
       
     2.线程池。
    
    ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        
        
     3.常用单位转换的辅助类。
      
      类名DensityUtils,包含dp转px;sp转px;px转dp;px2sp
      
      
     4.权限申请Activity。
     
     使用方法：
     
     extends BaseActivity，
      
      
      /**
       * 需要进行检测的权限数组
       */
      
      protected String[] needPermissions = {
            Manifest.permission.CAMERA
       };
      
      在方法里调用 
      
      checkPerMissions(needPermissions);
      
     
     后续会往里面持续添加功能，敬请期待。



How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:


allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
Step 2. Add the dependency


dependencies 
{
	        implementation 'com.github.dqx321:UdpSocket:1.0.5'
}
        
