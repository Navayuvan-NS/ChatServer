import java.io.DataInputStream;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.JTextArea;

class ClientThread implements Runnable
{
  DataInputStream dis;
  MyClient client;
  
  ClientThread(DataInputStream paramDataInputStream, MyClient paramMyClient)
  {
    dis = paramDataInputStream;
    client = paramMyClient;
  }
  
  public void run()
  {
    String str = "";
    try
    {
      while(1) {
        str = dis.readUTF();
        if (str.startsWith("updateuserslist:")) {
          updateUsersList(str);
        } else {
			if (str.equals("@@logoutme@@:")) {
            break;
          }
          client.txtBroadcast.append("\n" + str); }
        int i = client.txtBroadcast.getLineStartOffset(client.txtBroadcast.getLineCount() - 1);
        client.txtBroadcast.setCaretPosition(i);
      }
    } catch (Exception localException) { client.txtBroadcast.append("\nClientThread run : " + localException);
    }
  }
  

  public void updateUsersList(String paramString)
  {
    Vector localVector = new Vector(); 
    
    paramString = paramString.replace("[", "");
    paramString = paramString.replace("]", "");
    paramString = paramString.replace("updateuserslist:", "");
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
    
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();
      localVector.add(str);
    }
    client.usersList.setListData(localVector);
  }
}
