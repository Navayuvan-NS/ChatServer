import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

































class MyThread
  implements Runnable
{
  Socket s;
  ArrayList al;
  ArrayList users;
  String username;
  
  MyThread(Socket paramSocket, ArrayList paramArrayList1, ArrayList paramArrayList2)
  {
    s = paramSocket;
    al = paramArrayList1;
    users = paramArrayList2;
    try {
      DataInputStream localDataInputStream = new DataInputStream(paramSocket.getInputStream());
      username = localDataInputStream.readUTF();
      paramArrayList1.add(paramSocket);
      paramArrayList2.add(username);
      tellEveryOne("****** " + username + " Logged in at " + new Date() + " ******");
      sendNewUserList();
    } catch (Exception localException) {
      System.err.println("MyThread constructor  " + localException);
    }
  }
  
  public void run()
  {
    try {
      DataInputStream localDataInputStream = new DataInputStream(s.getInputStream());
      for (;;)
      {
        String str = localDataInputStream.readUTF();
        if (str.toLowerCase().equals("@@logoutme@@:"))
          break;
        tellEveryOne(username + " said: " + " : " + str);
      }
      
      DataOutputStream localDataOutputStream = new DataOutputStream(s.getOutputStream());
      localDataOutputStream.writeUTF("@@logoutme@@:");
      localDataOutputStream.flush();
      users.remove(username);
      tellEveryOne("****** " + username + " Logged out at " + new Date() + " ******");
      sendNewUserList();
      al.remove(s);
      s.close();
    }
    catch (Exception localException) {
      System.out.println("MyThread Run" + localException);
    }
  }
  
  public void sendNewUserList() {
    tellEveryOne("updateuserslist:" + users.toString());
  }
  

  public void tellEveryOne(String paramString)
  {
    Iterator localIterator = al.iterator();
    while (localIterator.hasNext()) {
      try
      {
        Socket localSocket = (Socket)localIterator.next();
        DataOutputStream localDataOutputStream = new DataOutputStream(localSocket.getOutputStream());
        localDataOutputStream.writeUTF(paramString);
        localDataOutputStream.flush();
      }
      catch (Exception localException) {
        System.err.println("TellEveryOne " + localException);
      }
    }
  }
}
