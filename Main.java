// Developed by Collin Kees
// A raycasting graphics engine using accii to display the scene

public class Main {
    static Scene scene = new Scene();
    public static void main(String[] args){
        int index = 0;
        scene.loadScene();
        boolean running = true;
        // render the first frame without calling clear first
        char[][] frame = scene.renderFrame();
        displayFrame(frame);

        // keep track time taken to render and display each frame
        long avgTimePerframe = 0;

        // main loop
        while (running){
            long startTime = System.nanoTime();
            frame = scene.renderFrame();
            clearFrame();
            displayFrame(frame);
            // move the camera up and down and the light source around the circles
            scene.camZ = scene.camZ + (Math.sin((index * Math.PI / 180) * 2) / 21.5);
            scene.gloablLightAngle[0] = scene.gloablLightAngle[0] + (Math.sin((index * Math.PI / 180)));
            scene.globalLightVector = scene.angleToVector(scene.gloablLightAngle);
            index++;
            avgTimePerframe = avgTimePerframe + (System.nanoTime() - startTime);
            if (index == 2000){
                running = false;
            }
        }
        avgTimePerframe = avgTimePerframe / index;
        System.out.println("on average rasterization took: " + avgTimePerframe/1000000 + " ms");
        System.out.println("this translates to " + 1000/(avgTimePerframe/1000000) + " frames per second");
    }
    // display a frame
    public static void displayFrame(char[][] frame){
        for (char[] line : frame){
            String row = "";
            for (char pix : line){
                row = row + pix + ' ';
            }
            System.out.println(row);
        }
    }
    // clear the last frame printed
    public static void clearFrame(){
        for (int i = 0; i < scene.resolution[1]; i++){
            System.out.print("\033[F\r");
        }
    }
}
