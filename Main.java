// Developed by Collin Kees
// A raycasting graphics engine using accii to display the scene

public class Main {
    static Scene scene = new Scene();
    public static void main(String[] args) throws InterruptedException{
        int index = 0;
        scene.loadScene();
        LightSource light = scene.sceneLights.get(0);
        boolean running = true;

        // keep track time taken to render and display each frame
        long avgTimePerframe = 0;

        // keep track of the last frame and allow a target fps to be aimed for
        long startTime = 0;
        long timeLastFrame = 0;
        long remaining_time = 0;
        int targetFPS = 60;
        // convert fps to time per frame in ns
        long frameTime = 1000000000 / targetFPS;
        
        // set the camera's initial position

        // set up the keyboard listener and display window
        char[][] frame = scene.renderFrame();
        OutputBox output = new OutputBox(scene.resolution, frame);

        // main loop
        while (running){
            startTime = System.nanoTime();
            frame = scene.renderFrame();

            // update the output box
            output.outputFrame(output.charToString(frame));
            output.input.updateCamPos(scene);

            // make spheres wobble
            int i = 0;
            for (SceneObject object : scene.sceneObjects){
                if (object.sphere != null){
                    double x_ = (Math.cos(((index + (i * 10)) * Math.PI / 180) * 5) * 0.05);
                    double y_ = (Math.cos(((index + (i * 10)) * Math.PI / 180) * 5) * 0.05);
                    double z_ = (Math.cos(((index + (i * 10)) * Math.PI / 180) * 5) * 0.05);
                    object.changeCords(x_, y_, z_);
                    i++;
                }
            }

            // move the camera up and down and the light source around the circles
            //scene.camZ = scene.camZ + (Math.cos((index * Math.PI / 180) * 2) / 30);
            //light.setY(light.y + (Math.cos((index * (Math.PI / 180) * 5) * 1)));

            // move the camera in a circle around the circles
            //scene.camX += 0.2 * Math.sin(scene.camYaw *  Math.PI / 180);
            //scene.camY -= 0.2 * Math.cos(scene.camYaw *  Math.PI / 180);
            //scene.camYaw += 1.5;

            // update loop info
            index++;
            avgTimePerframe = avgTimePerframe + (System.nanoTime() - startTime);

            // if the frame rendered faster than needed take a short break to not overshoot the target fps
            timeLastFrame = System.nanoTime() - startTime;
            remaining_time = frameTime - timeLastFrame;
            if (remaining_time > 0){
                // convert ns to ms
                Thread.sleep(remaining_time/1000000);
            }

            if (index == -1){
                running = false;
            }
        }

        // benchmarking code to test fps
        avgTimePerframe = avgTimePerframe / index;
        System.out.println("on average rasterization took: " + avgTimePerframe/1000000 + " ms");
        System.out.println("this translates to " + 1000/(avgTimePerframe/1000000) + " frames per second");
    }
}
