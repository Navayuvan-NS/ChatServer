import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



class MyServer
{
  ArrayList al = new ArrayList();
  ArrayList users = new ArrayList();
  ServerSocket ss;
  Socket s;
  public static final int PORT = 10;
  public static final String UPDATE_USERS = "updateuserslist:";
  public static final String LOGOUT_MESSAGE = "@@logoutme@@:";
  
  public MyServer()
  {
    try {
      ss = new ServerSocket(10);
      System.out.println("Server Started " + ss);
      while(1)
      {
        s = ss.accept();
        MyThread localMyThread = new MyThread(s, al, users);
        Thread localThread = new Thread(localMyThread);
        localThread.start();
      }
    }
    catch (Exception localException) {
      System.err.println("Server constructor" + localException);
    }
  }
  
  public static void main(String[] paramArrayOfString) {
    new MyServer();
  }
}
