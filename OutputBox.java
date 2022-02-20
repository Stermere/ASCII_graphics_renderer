// the gui that drives allows the grachics engine to output its frames and recive input from a key listener

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

// class that handels the gui set up usage
public class OutputBox {
    JFrame window = new JFrame("Render Window");
    Input input = new Input();
    JTextArea renderArea;
    int outputSize;

    // constructor sets up the window
    public OutputBox(int[] resolution, char[][] charFrame){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1920,1080);
        outputSize = resolution[0] * resolution[1] * 2 + resolution[1];
        String frame = charToString(charFrame);
        renderArea = new JTextArea(frame, resolution[0], resolution[1]);
        renderArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        renderArea.setForeground(Color.WHITE);
        renderArea.setBackground(Color.BLACK);
        window.add(renderArea);
        renderArea.addKeyListener(input);
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
class Input extends KeyAdapter {
    // keep track of the keyboard keys we care about
    boolean w = false;
    boolean a = false;
    boolean s = false;
    boolean d = false;
    boolean q = false;
    boolean e = false;

    // these should be replaced with a mouse listener
    boolean i = false;
    boolean j = false;
    boolean k = false;
    boolean l = false;



    public Input(){
        return;
    }

    //key pressed handeler
    public void keyPressed(KeyEvent keyEvent){
        char key = keyEvent.getKeyChar();
        switch (key){
            case 'w':
                w = true;
                break;
            case 'a':
                a = true;
                break;
            case 's':
                s = true;
                break;
            case 'd':
                d = true;
                break;
            case 'q':
                q = true;
                break;
            case 'e':
                e = true;
                break;
            case 'i':
                i = true;
                break;
            case 'j':
                j = true;
                break;
            case 'k':
                k = true;
                break;
            case 'l':
                l = true;
                break;
            default:
                break;
        }
        
    }

    // key released handeler
    public void keyReleased(KeyEvent keyEvent){
        char key = keyEvent.getKeyChar();
        switch (key){
            case 'w':
                w = false;
                break;
            case 'a':
                a = false;
                break;
            case 's':
                s = false;
                break;
            case 'd':
                d = false;
                break;
            case 'q':
                q = false;
                break;
            case 'e':
                e = false;
                break;
            case 'i':
                i = false;
                break;
            case 'j':
                j = false;
                break;
            case 'k':
                k = false;
                break;
            case 'l':
                l = false;
                break;
            default:
                break;
        }
    }

    // update the camaras position in the scene (call every frame)
    public void updateCamPos(Scene scene){
        double[] unitVector = scene.angleToVector(scene.camYaw, scene.camPitch);
        double[] sideVector = scene.angleToVector(scene.camYaw + 90, scene.camPitch);

        // check what key is pressed and update the camera
        if (w){
            scene.camX += unitVector[0] * 0.1;
            scene.camY += unitVector[1] * 0.1;
            scene.camZ += unitVector[2] * 0.1;
        }
        if (a){
            scene.camX -= sideVector[0] * 0.1;
            scene.camY -= sideVector[1] * 0.1;
        }
        if (s){
            scene.camX -= unitVector[0] * 0.1;
            scene.camY -= unitVector[1] * 0.1;
            scene.camZ -= unitVector[2] * 0.1;
        }
        if (d){
            scene.camX += sideVector[0] * 0.1;
            scene.camY += sideVector[1] * 0.1;
        }
        if (q){
            scene.camZ += 0.1;
        }
        if (e){
            scene.camZ -= 0.1;
        }
        if (i){
            scene.camPitch += 1;
        }
        if (k){
            scene.camPitch -= 1;
        }
        if (j){
            scene.camYaw -= 1;
        }
        if (l){
            scene.camYaw += 1;
        }



    }
}
