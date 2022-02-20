// Developed by Collin Kees
// A raycasting graphics engine using accii to display the scene

public class Main {
    static Scene scene = new Scene();
    public static void main(String[] args){
        int index = 0;
        scene.loadScene();
        LightSource light = scene.sceneLights.get(0);
        boolean running = true;

        // keep track time taken to render and display each frame
        long avgTimePerframe = 0;
        
        // set the camera's initial position
        scene.camZ = 0;
        scene.camX = -2;
        scene.camPitch = 0;

        // set up the keyboard listener and display window
        char[][] frame = scene.renderFrame();
        OutputBox output = new OutputBox(scene.resolution, frame);

        // main loop
        while (running){
            long startTime = System.nanoTime();
            frame = scene.renderFrame();

            output.outputFrame(output.charToString(frame));

            output.input.updateCamPos(scene);

            // move the camera up and down and the light source around the circles
            //scene.camZ = scene.camZ + (Math.cos((index * Math.PI / 180) * 2) / 30);
            //light.setY(light.y + (Math.cos((index * (Math.PI / 180) * 5) * 1)));

            // move the camera in a circle around the circles
            //scene.camX += 0.2 * Math.sin(scene.camYaw *  Math.PI / 180);
            //scene.camY -= 0.2 * Math.cos(scene.camYaw *  Math.PI / 180);
            //scene.camYaw += 1.5;


            index++;
            avgTimePerframe = avgTimePerframe + (System.nanoTime() - startTime);
            if (index == -1){
                running = false;
            }
        }
        avgTimePerframe = avgTimePerframe / index;
        System.out.println("on average rasterization took: " + avgTimePerframe/1000000 + " ms");
        System.out.println("this translates to " + 1000/(avgTimePerframe/1000000) + " frames per second");
    }
}
