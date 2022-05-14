package RFBDemo;

import RobotService.RobotMouse;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;

import static java.lang.Integer.parseInt;
import static javax.swing.JOptionPane.*;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

class Mouse implements MouseListener{
    DataInputStream in;
    DataOutputStream out;
    static  Socket s;
    Mouse(){    }
    public void mouseClicked(MouseEvent e){
        try {
            out.writeInt(4);
            System.out.println("X-Co = "+e.getX());
            out.writeInt(e.getX());
            System.out.println("Y-Co = "+e.getY());
            out.writeInt(e.getY());
            out.writeInt(e.getButton());
        } catch (IOException xe){
            xe.printStackTrace();
        }
    }
    public void mouseExited(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            out.writeInt(4);
            out.writeInt(e.getX());
            out.writeInt(e.getY());
            out.writeInt(e.getButton());
        } catch (IOException xe){
            xe.printStackTrace();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public void setSocket() throws IOException{
        this.s=s;
        in=new DataInputStream(s.getInputStream());
        out=new DataOutputStream(s.getOutputStream());
    }
}
//Class for Displaying and updating screecshots
class ImageImplement extends JPanel {
    private Image imag;
    public ImageImplement(ImageIcon img) {
        this.imag = img.getImage();
        Dimension size = new Dimension(imag.getWidth(null), imag.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        setVisible(false);
    }
    public void paintComponent(Graphics g) {
        g.drawImage(imag, 0, 0, null);
    }
}
//Separate Thread for Screen Sharing
class Th1 extends Thread{
    static ImageIcon o;
    static ImageImplement panel;
    static JFrame jp;
    static Mouse m;
    void getFrameBuffer(Socket s) throws IOException,OutOfMemoryError,InterruptedException{
        int ix=0;

        DataInputStream in=new DataInputStream(s.getInputStream());//Stream for reading data from server
        DataOutputStream out=new DataOutputStream(s.getOutputStream());//Stream for writing data to server
        jp= new JFrame();//Separate Frame for Screen SHaring
        jp.setLocation(10,10);
        jp.setVisible(true);
        jp.setSize(800, 800);
        jp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    out.writeInt(4);
                    System.out.println("X-Co = "+e.getX());
                    out.writeInt(e.getX());
                    System.out.println("Y-Co = "+e.getY());
                    out.writeInt(e.getY());
                    out.writeInt(e.getButton());
                } catch (IOException xe){
                    xe.printStackTrace();
                }
            }
        });
        while (ix<100){//Reading Byte data from server
            int nbrToRead = in.readInt();
            System.out.println("received Size " + ix+" Size = "+nbrToRead);
            byte[] byteArray = new byte[nbrToRead];
            int nbrRd = 0;
            int nbrLeftToRead = nbrToRead;
            while(nbrLeftToRead > 0){
                int rd =in.read(byteArray, nbrRd, nbrLeftToRead);
                if(rd < 0)
                    break;
                nbrRd += rd; // accumulate bytes read
                nbrLeftToRead -= rd;
            }
            System.out.println("Converted Into Image " + ix);
            //Converting the image
            ByteArrayInputStream byteArrayI = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(byteArrayI);
            System.out.println("Buffered Image " + ix);
            System.out.println("received screen " + ix);
            File of = new File("RecvdImg" + ix + ".jpg");//Creating .jpg File
            System.out.println("Filed " + ix);
            ImageIO.write(image, "PNG" ,of);//Writing .jpg File
            System.out.println("RecvdImg" + ix + ".jpg");
            o=null;
            o=new ImageIcon(image);
            panel = new ImageImplement(o);//Object for displaying Screenshot
            panel.setVisible(true);
            jp.add(panel);
            //sleep(3000);
            jp.revalidate();
            panel.repaint();
            ++ix;
            System.out.println("ix++ = "+ix);
            image.flush();
            out.writeInt(3);
        }
    }
    void sendMevent(Socket s) throws IOException{
        DataInputStream in=new DataInputStream(s.getInputStream());//Stream for reading data from server
        DataOutputStream out=new DataOutputStream(s.getOutputStream());
        //int button = me.getButton();
        //int x = me.getX();
        //int y= me.getY();
        //out.writeInt(x);
        //out.writeInt(y);
        //out.writeInt(1);

    }

    @Override
    public void run() {
        try {
            m=new Mouse();
            getFrameBuffer(Fr.s);
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}


class Fr extends JFrame{
    public static Socket s;
    RobotMouse robo;
    private final byte[] RFB_VER = "RFB 003.008\n".getBytes();
    private BufferedInputStream in;
    private BufferedOutputStream out;
    String host;
    int port;
    Fr() throws UnknownHostException,IOException,AWTException{

        this.setVisible(true);
        robo = new RobotMouse();
        JMenuBar mbr = new JMenuBar();
        this.setJMenuBar(mbr);
        this.setSize(400,400);
        JMenu fmenu=new JMenu("File");
        JMenuItem conn= new JMenuItem("Connect");
        JMenuItem exititem= new JMenuItem("Exit");
        mbr.add(fmenu);
        fmenu.add(conn);
        fmenu.add(exititem);
        conn.addActionListener((ActionEvent event) -> {
            try {
                showConnectDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        exititem.addActionListener((ActionEvent event) -> {
            System.exit(1);
        });

    }
    public static void setSocket(Socket sv){
        s=sv;
    }

    private int readU16int() throws IOException {

        byte[] buffer = new byte[2];

        int offset = 0, left = buffer.length;
        while (offset < buffer.length) {
            int numOfBytesRead = 0;
            numOfBytesRead = in.read(buffer, offset, left);
            offset = offset + numOfBytesRead;
            left = left - numOfBytesRead;
        }

        int value = ((buffer[0] & 0xFF) << 8)
                + (buffer[1] & 0xFF);

        return value;

    }
    private void showConnectDialog() throws UnknownHostException , IOException{
        JPanel connectDialog = new JPanel();
        JTextField hostField = new JTextField(20);
        //hostField.addAncestorListener(focusRequester);
        JTextField portField = new JTextField("5900");
        JLabel hostLabel = new JLabel("Host");
        hostLabel.setLabelFor(hostField);
        JLabel portLabel = new JLabel("Port");
        portLabel.setLabelFor(hostLabel);
        connectDialog.add(hostLabel);
        connectDialog.add(hostField);
        connectDialog.add(portLabel);
        connectDialog.add(portField);
        int choice = showConfirmDialog(this, connectDialog, "Connect", OK_CANCEL_OPTION);
        if (choice == OK_OPTION) {
            String host = hostField.getText();
            if (host == null || host.isEmpty()) {
                showMessageDialog(this, "Please enter a valid host", null, WARNING_MESSAGE);
                return;
            }
            try {
              port = parseInt(portField.getText());
            } catch (NumberFormatException e) {
                showMessageDialog(this, "Please enter a valid port", null, WARNING_MESSAGE);
                return;
            }
           connect(host, port);
            this.setVisible(false);

        }
    }
    private void sendProtocolVersion(Socket s) throws IOException, UnknownHostException {
        s.getOutputStream().write(RFB_VER);
        s.getOutputStream().flush();
    }
    private byte[] readU8Array(int len) throws IOException {
        byte[] buffer = new byte[len];
        int offset = 0, left = buffer.length;
        while (offset < buffer.length) {
            int numOfBytesRead = 0;
            numOfBytesRead = in.read(buffer, offset, left);
            offset = offset + numOfBytesRead;
            left = left - numOfBytesRead;
        }
        return buffer;
    }
    public void connect(String host, int port) throws UnknownHostException,IOException{
        Socket soc=new Socket(host,port);
        setSocket(soc);
        //String str= readProtocolVersion();
        //sendProtocolVersion(soc);
        //soc.getOutputStream().write(1);
        use(soc);
    }

    public void use(Socket sc) throws IOException{
        sc.getOutputStream().write(3);
        Th1 t=new Th1();
        t.start();

    ;}
}

public class Cas{
    public static void main(String args[]) throws AWTException,UnknownHostException,IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                   Fr fr=new Fr();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AWTException e){
                    e.printStackTrace();
                }
            }
        });
        //RobotScreen.robo = new RobotScreen();
        //Socket soc= new Socket("127.0.0.1",5902);


    }


}