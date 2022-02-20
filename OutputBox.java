// the gui that drives allows the grachics engine to output its frames and recive input from a key listener

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

// class that handels the gui set up usage
public class OutputBox {
    JFrame window = new JFrame("Render Window");
    JTextArea renderArea;
    int outputSize;

    // constructor sets up the window
    public OutputBox(int[] resolution, char[][] charFrame){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1920,1080);
        outputSize = resolution[0] * resolution[1];
        String frame = charToString(charFrame);
        renderArea = new JTextArea(frame, resolution[0], resolution[1]);
        renderArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        renderArea.setForeground(Color.WHITE);
        renderArea.setBackground(Color.BLACK);
        window.add(renderArea);
        window.setVisible(true);
    }


    // turn the char array in to a string
    public String charToString(char[][] frame){
        String frameString = "";
        for (char[] line : frame){
            String row = "";
            for (char pix : line){
                row = row + pix + ' ';
            }
            frameString += (row + "\n");
        }
        return frameString;
    }


    // update the data inside the render area
    public void outputFrame(String frame){
        renderArea.replaceRange(frame, 0, outputSize);
    }
}

// class to handle movment of the camera
class Input implements KeyListener {
    // hold the state of a key
    boolean w = false;
    boolean a = false;
    boolean s = false;
    boolean d = false;
    boolean q = false;
    boolean e = false;
    
    public Input(){
        return;
    }

    //key pressed handeler
    public void keyPressed(KeyEvent e){
        char key = e.getKeyChar();
    }

    // key released handeler
    public void keyReleased(KeyEvent e){
        System.exit(1);
    }

    // key typed handeler currently unused
    public void keyTyped(KeyEvent e){
        return;
    }
}
