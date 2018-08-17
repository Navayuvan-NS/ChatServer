import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;


class MyClient implements ActionListener
{
  Socket s;
  DataInputStream dis;
  DataOutputStream dos;
  JButton sendButton;
  JButton logoutButton;
  JButton loginButton;
  JButton exitButton;
  JFrame chatWindow;
  JTextArea txtBroadcast;
  JTextArea txtMessage;
  JList usersList;
  
  public void displayGUI()
  {
    chatWindow = new JFrame();
    txtBroadcast = new JTextArea(5, 30);
    txtBroadcast.setEditable(false);
    txtMessage = new JTextArea(2, 20);
    usersList = new JList();
	
	
    
    sendButton = new JButton("Send");
    logoutButton = new JButton("Log out");
    loginButton = new JButton("Log in");
    exitButton = new JButton("Exit");
    
	
	
    JPanel Panel1 = new JPanel();
    Panel1.setLayout(new BorderLayout());
    Panel1.add(new JLabel("Messages", 0), "North");
    Panel1.add(new JScrollPane(txtBroadcast), "Center");
    
	
	
    JPanel Panel2 = new JPanel();
	Panel2.setLayout(new FlowLayout());
    Panel2.add(new JScrollPane(txtMessage));
    Panel2.add(sendButton);
    
	
	
    JPanel Panel3 = new JPanel();
    Panel3.setLayout(new FlowLayout());
    Panel3.add(loginButton);
    Panel3.add(logoutButton);
    Panel3.add(exitButton);
    
	
	
    JPanel Panel4 = new JPanel();
    Panel4.setLayout(new GridLayout(2, 1));
    Panel4.add(localJPanel2);
    Panel4.add(localJPanel3);
    
	
	
    JPanel Panel5 = new JPanel();
    Panel5.setLayout(new BorderLayout());
    Panel5.add(new JLabel("Users", 0), "East");
    Panel5.add(new JScrollPane(usersList), "South");
	Panel5.add(new JScrollPane(txtBroadcast), "Center");
    
	
    chatWindow.add(localJPanel5, "East");
    chatWindow.add(localJPanel1, "Center");
    chatWindow.add(localJPanel4, "South");
    
	
    chatWindow.pack();
    chatWindow.setTitle("Chat Server");
    chatWindow.setDefaultCloseOperation(0);
    chatWindow.setVisible(true);
    sendButton.addActionListener(this);
    logoutButton.addActionListener(this);
    loginButton.addActionListener(this);
    exitButton.addActionListener(this);
    logoutButton.setEnabled(false);
    loginButton.setEnabled(true);
    txtMessage.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent paramAnonymousFocusEvent) { txtMessage.selectAll(); }
    });
    chatWindow.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        if (s != null)
        {
          JOptionPane.showMessageDialog(chatWindow, "Logged out ", "Exit", 1);
          logoutSession();
        }
        System.exit(0);
      }
    });
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    JButton localJButton = (JButton)paramActionEvent.getSource();
    if (localJButton == sendButton)
    {
      if (s == null) {
        JOptionPane.showMessageDialog(chatWindow, "Please Login");return;
      }
      try { dos.writeUTF(txtMessage.getText());
        txtMessage.setText("");
      } catch (Exception localException) {
        txtBroadcast.append("\nsend button click :" + localException);
      } }
    if (localJButton == loginButton)
    {
      String str = JOptionPane.showInputDialog(chatWindow, "Emter your name ");
      if (str != null)
        clientChat(str);
    }
    if (localJButton == logoutButton)
    {
      if (s != null)
        logoutSession();
    }
    if (localJButton == exitButton)
    {
      if (s != null)
      {
        JOptionPane.showMessageDialog(chatWindow, "Logged out  ", "Exit", 1);
        logoutSession();
      }
      System.exit(0);
    }
  }
  
  public void logoutSession()
  {
    if (s == null) return;
    try {
      dos.writeUTF("@@logoutme@@:");
      Thread.sleep(500L);
      s = null;
    } catch (Exception localException) {
      txtBroadcast.append("\n inside logoutSession Method" + localException);
    }
    logoutButton.setEnabled(false);
    loginButton.setEnabled(true);
    chatWindow.setTitle("Login for Chat");
  }
  
  public void clientChat(String paramString)
  {
    try {
      s = new Socket(InetAddress.getLocalHost(), 10);
      dis = new DataInputStream(s.getInputStream());
      dos = new DataOutputStream(s.getOutputStream());
      ClientThread localClientThread = new ClientThread(dis, this);
      Thread localThread = new Thread(localClientThread);
      localThread.start();
      dos.writeUTF(paramString);
      chatWindow.setTitle(paramString + " Chat Window");
    } catch (Exception localException) {
      txtBroadcast.append("\nClient Constructor " + localException); }
    logoutButton.setEnabled(true);
    loginButton.setEnabled(false);
  }
  
  public MyClient()
  {
    displayGUI();
  }
  

  public static void main(String[] paramArrayOfString)
  {
    new MyClient();
  }
}
