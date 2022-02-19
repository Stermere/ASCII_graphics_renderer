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
    static int BOUNCECOUNT = 5;


    // settings
    double FOV = 90;
    //int[] resolution = {125, 70}; // for 1440p monitors you need a fast cpu to run this at 30 fps
    //int[] resolution = {60, 35}; // for my laptop that is 1080 and scaled
    int[] resolution = {200, 125}; // I dont know what cpu can run this at more than 10fps but if you zoom out your window it looks cool

    double renderDist = 100;
    double backroundColor = 0;
    double degreePerPixle = FOV / Math.sqrt((Math.pow(resolution[0], 2) + Math.pow(resolution[1], 2)));
    // 16 char pallet
    char[] pixValues = {' ', '.', ',', '-', '"', '~', ':', ';', '^', '>', '=', '+', '!', '*', '#', '$', '@'};
    // 16 char pallet 2
    //char[] pixValues = {'.','-',',',':','^','~','*','=','+','>','a','q','#','$','%','@'};

    // objects in the scene
    ArrayList<SceneObject> sceneObjects = new ArrayList<SceneObject>();
    ArrayList<LightSource> sceneLights = new ArrayList<LightSource>();
    
    // load a scene
    // when making a scene remember to always have at least one light source
    public void loadScene(){
        // real load goes here
        // for now this will be a good test
        sceneObjects.add(new SceneObject(new Sphere(4.3, -1.1, 0.1, 1.0, 16)));
        sceneObjects.add(new SceneObject(new Sphere(4.3, 1.0, -1.5, 1.1, 16)));
        sceneObjects.add(new SceneObject(new Sphere(4.3, 1.5, 0.0, 0.4, 16)));
        sceneObjects.add(new SceneObject(new Sphere(6, -0.2, 0.0, 0.7, 16)));
        sceneObjects.add(new SceneObject(new Sphere(6, -4, -2.1, 0.9, 16)));

        sceneObjects.add(new SceneObject(new Sphere(3, -6, 2, 1.5, 16, true)));

        sceneObjects.add(new SceneObject(new Plane(-2, 16)));

        // load light sources
        sceneLights.add(new LightSource(0, 0, 4, 16));

    }

    // renders a frame
    public char[][] renderFrame(){
        char[][] frame = new char[resolution[1]][resolution[0]];
        double angleB = camPitch + ((resolution[1] / 2) * degreePerPixle);
        for (int t = 0; t < resolution[1]; t++){
            double angleA = camYaw - ((resolution[0] / 2) * degreePerPixle);
            for (int p = 0; p < resolution[0]; p++){
                double[] vector = angleToVector(angleA, angleB);
                int maxIters = MAXITERS;
                int bounceCount = BOUNCECOUNT;
                double pixcol = getPixelValue(vector, camX, camY, camZ, maxIters, bounceCount);
                char pixval = getChar(pixcol);
                frame[t][p] = pixval;

                angleA = angleA + degreePerPixle;
            }
            angleB = angleB - degreePerPixle;
        }
        return frame;
    }

    // get the value of a ray cast on the scene
    public double getPixelValue(double[] vector, double x, double y, double z, int maxIters, int bounceIter){
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
        else {
            lowestDist = 0.1;
        }
        // if render distance is hit or a object is contacted calculate its shading and return
        if (lowestDist < 0.0001){
            // once the first collision is found calculate the shading
            double[] normalVector = closestObject.getNormalVector(x, y, z);
            double[] lightVector = null;
            double luminence;
            double highestLuminence = 0;

            // calculate the luminance of the pixel being shaded
            for (LightSource light : sceneLights){
                lightVector = light.getNormalVector(x, y, z);
                luminence = dotProduct(lightVector[0], lightVector[1], lightVector[2], normalVector[0], normalVector[1], normalVector[2]);
                if (luminence > highestLuminence){
                    highestLuminence = luminence;
                }
            }
            // check if the object has reflectivity and if so reflect the ray and find its next collision
            if (closestObject.getReflectivity() && bounceIter != 0){
                // calculate the new angle for the light to follow
                vector[0] = vector[0] + normalVector[0];
                vector[1] = vector[1] + normalVector[1];
                vector[2] = vector[2] + normalVector[2];
                // calculate next starting point
                double xNext = x + lowestDist * vector[0];
                double yNext = y + lowestDist * vector[1];
                double zNext = z + lowestDist * vector[2];
                
                // recursivly call this function to get the value
                return getPixelValue(vector, xNext, yNext, zNext, maxIters - 1, bounceIter - 1);
            }


            
            double color = closestObject.getColor();
            return getReflectionColor(color, highestLuminence);
        }
        if (lowestDist > renderDist || maxIters <= 0){
            return 0;
        }
        // calculate next starting point
        double xNext = x + lowestDist * vector[0];
        double yNext = y + lowestDist * vector[1];
        double zNext = z + lowestDist * vector[2];
        
        // recursivly call this function to get the value
        return getPixelValue(vector, xNext, yNext, zNext, maxIters - 1, bounceIter);
    }

    // take the shading and add the color to the value
    public double getReflectionColor(double color, double luminence){
        color = color * luminence;
        return color;
    }

    // convert a color value to a char
    public char getChar(double color){
        if (color < 0){
            return pixValues[0];
        }
        return pixValues[(int) color];
    }

    // calculate the dot product of two normalized vectors
    public double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2){
        return (x1*x2)+(y1*y2)+(z1*z2);
    }

    // convert angle values in to a unit vector
    public double[] angleToVector(double a, double b){
        double z = Math.sin(b * Math.PI / 180);
        double x = (Math.cos(b * Math.PI / 180)) * Math.cos(a * Math.PI / 180);
        double y = (Math.cos(b * Math.PI / 180)) * Math.sin(a * Math.PI / 180);
        double[] vector = {x,y,z};
        return vector;
    }

    public double[] vectorToAngle(double[] vector){
        double x = vector[0];
        double y = vector[1];
        double z = vector[2];
        double yaw = Math.atan(x/y) * (180/Math.PI);
        double pitch = Math.atan(z/x) * (180/Math.PI);
        double[] angle = {yaw, pitch};
        return angle;
    }
    
}
