package RFBDemo;

import RobotService.RobotMouse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


class Th extends Thread{
    static public Socket su;
    static public ServerSocket sv;
    static void setSocket(Socket s,ServerSocket sx){
        su=s;
        sv=sx;
    }
   /* private byte[] readU8Array(int len) throws IOException {
        byte[] buffer = new byte[len];
        int offset = 0, left = buffer.length;
        while (offset < buffer.length) {
            int numOfBytesRead = 0;
            numOfBytesRead = in.read(buffer, offset, left);
            offset = offset + numOfBytesRead;
            left = left - numOfBytesRead;
        }
        return buffer;
    }*/
   /* private String readProtocolVersion() throws IOException {
        //byte[] buffer = readU8Array(12);
        return new String(buffer);
    }*/
    @Override
    public void run() {
        try {
            Robot bot=new Robot();
            ServerSocket sersco=new ServerSocket(5900);
            Socket s=sersco.accept();
            RobotMouse robo=new RobotMouse();
            DataOutputStream out=new DataOutputStream(s.getOutputStream());
            DataInputStream in=new DataInputStream(s.getInputStream());
            RobotMouse rmouse=new RobotMouse();
            int x=100,ix=0;
            while (x>0){
                switch(s.getInputStream().read()){
                    case 3:
                        setSocket(s,sersco);
                        System.out.println("Started " + ix);
                        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                        System.out.println("Created Robot " + ix);
                        ByteArrayOutputStream byteArrayO = new ByteArrayOutputStream();
                        ImageIO.write(image,"PNG",byteArrayO);
                        byte [] byteArray = byteArrayO.toByteArray();
                        System.out.println("Converteed Bytes " + ix);
                        out.writeInt(byteArray.length);
                        System.out.println("Sent Length " + ix + "Length = "+byteArray.length);
                        out.write(byteArray);
                        System.out.println("Sent Bytes" + ix);
                        image.flush();
                        break;

                    case 4:
                        int sx=in.readInt();
                        int sy=in.readInt();
                        int btype=in.readInt();
                        bot.mouseMove(sx,sy);
                        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        break;
                }
                System.out.println("ix++="+ix);
                ix++;
                System.out.println("x="+x);
                x--;
                if (x==1){
                    x=100;
                }
                //sleep(200);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (AWTException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
public class Srvr2 {
    public static void main(String args[]) throws AWTException,IOException {
        Th t=new Th();
        t.start();
        Socket s=Th.su;
        //Th.sv.close();

    }
}
