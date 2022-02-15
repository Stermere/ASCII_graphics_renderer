// class that holds the objects of a scene and does the computation for marching
import java.util.ArrayList;

public class Scene {
    // camera control variables
    double camX = 0;
    double camY = 0;
    double camZ = 0;
    double camYaw = 0; // yaw
    double camPitch = 0; // pitch
    
    // maximum iterations of of ray marching
    static int MAXITERS = 1000;

    // settings
    double FOV = 90;
    int[] resolution = {125, 70};
    double renderDist = 100;
    double[] gloablLightAngle = {-30, 20};
    double[] globalLightVector = angleToVector(gloablLightAngle);
    double backroundColor = 0;
    double degreePerPixle = FOV / Math.sqrt((Math.pow(resolution[0], 2) + Math.pow(resolution[1], 2)));
    // 16 char pallet
    char[] pixValues = {'.', ',', '-', '"', '~', ':', ';', '^', '>', '=', '+', '!', '*', '#', '$', '@'};
    // 16 char pallet
    //char[] pixValues = {'.','-',',',':','^','~','*','=','+','>','a','q','#','$','%','@'};

    // objects in the scene
    ArrayList sceneObjects = new ArrayList<SceneObject>();
    
    // load a scene
    public void loadScene(){
        // real load goes here
        // for now this will be a good test
        sceneObjects.add(new SceneObject(new Sphere(4.3, -1.1, 0.1, 1.0, 16)));
        sceneObjects.add(new SceneObject(new Sphere(4.3, 1.5, -1.5, 0.2, 16)));
        sceneObjects.add(new SceneObject(new Sphere(4.3, 1.0, -1.5, 1.1, 16)));
        sceneObjects.add(new SceneObject(new Sphere(-4.3, 1.5, 2.5, 2.0, 16)));
        sceneObjects.add(new SceneObject(new Sphere(4.3, 1.5, 0.0, 0.4, 16)));
        sceneObjects.add(new SceneObject(new Sphere(0.0, 4.0, -1.5, 0.2, 16)));
        sceneObjects.add(new SceneObject(new Sphere(0.0, -3.5, -1.5, 0.2, 16)));        
        sceneObjects.add(new SceneObject(new Sphere(0.0, 4.0, .6, 0.2, 16)));
        sceneObjects.add(new SceneObject(new Sphere(0.0, -3.5, -0.5, 0.2, 16)));
        sceneObjects.add(new SceneObject(new Plane(-2, 16)));


    }

    // renders a frame
    public char[][] renderFrame(){
        char[][] frame = new char[resolution[1]][resolution[0]];
        double angleB = camPitch + ((resolution[1] / 2) * degreePerPixle);
        for (int t = 0; t < resolution[1]; t++){
            double angleA = camYaw - ((resolution[0] / 2) * degreePerPixle);
            for (int p = 0; p < resolution[0]; p++){
                int maxIters = MAXITERS;
                char pixval = getPixelValue(angleA, angleB, camX, camY, camZ, maxIters);
                frame[t][p] = pixval;

                angleA = angleA + degreePerPixle;
            }
            angleB = angleB - degreePerPixle;
        }
        return frame;
    }
    // get the value of a ray cast on the scene
    public char getPixelValue(double a, double b, double x, double y, double z, int maxIters){
        // get closest signed distance
        double dist = Double.POSITIVE_INFINITY;
        double lowestDist = Double.POSITIVE_INFINITY;
        SceneObject closestObject = null;
        if (maxIters != MAXITERS){
            for (int i = 0; i < sceneObjects.size(); i++){
                dist = ((SceneObject) sceneObjects.get(i)).signedDistTo(x,y,z);

                if (dist < lowestDist){
                    lowestDist = dist;
                    closestObject = (SceneObject) sceneObjects.get(i);
                }
            }
        }
        else    {
            lowestDist = 0.1;
        }
        // if render distance is hit or a object is contacted calculate its shading and return
        if (lowestDist < 0.0001){
            // once the first collision is found calculate the shading
            double[] normalVector = closestObject.getNormalVector(x, y, z);

            // calculate the angle of the light hitting the object



            double luminence = dotProduct(globalLightVector[0], globalLightVector[1], globalLightVector[2], normalVector[0], normalVector[1], normalVector[2]);
            double color = closestObject.getColor();

            return getChar(color, luminence);
        }
        if (lowestDist > renderDist || maxIters <= 0){
            return ' ';
        }
        // calculate next starting point
        double zNext = z + lowestDist * Math.sin(b * Math.PI / 180);
        double xNext = x + (lowestDist * Math.cos(b * Math.PI / 180)) * Math.cos(a * Math.PI / 180);
        double yNext = y + (lowestDist * Math.cos(b * Math.PI / 180)) * Math.sin(a * Math.PI / 180);
        
        // recursivly call this function to get the value
        return getPixelValue(a, b, xNext, yNext, zNext, maxIters - 1);
    }
    // translate a color value to a char
    public char getChar(double color, double luminence){
        if (luminence < 0){
            return pixValues[0];
        }
        color = color*luminence;
        return pixValues[(int) color];
    }
    // calculate the dot product of two normalized vectors
    public double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2){
        return (x1*x2)+(y1*y2)+(z1*z2);
    }
    // convert angle values in to a unit vector
    public double[] angleToVector(double[] angle){
        double z = Math.sin(angle[1] * Math.PI / 180);
        double x = (Math.cos(angle[1] * Math.PI / 180)) * Math.cos(angle[0] * Math.PI / 180);
        double y = (Math.cos(angle[1] * Math.PI / 180)) * Math.sin(angle[0] * Math.PI / 180);
        double[] vector = {x,y,z};
        return vector;
    }
    
}
