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
    double FOV = 70;
    int[] resolution = {155, 70}; // for 1440p monitors you need a fast cpu to run this at 60 fps
    //int[] resolution = {60, 35}; // for my laptop that is 1080 and scaled
    //int[] resolution = {200, 125}; // I dont know what cpu can run this at more than 10fps but if you zoom out your window it looks cool

    double renderDist = 100;
    double backroundColor = 1;
    double degreePerPixle = FOV / Math.sqrt((Math.pow(resolution[0], 2) + Math.pow(resolution[1], 2)));
    // 16 char pallet
    char[] pixValues = {' ', '.', ',', '-', '"', '~', ':', '^', '>', '!', '*', '+', '=', '$', '#', '@'};
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

        sceneObjects.add(new SceneObject(new Sphere(-6, -4, 6, 5, 16, true)));
        sceneObjects.add(new SceneObject(new Plane(5, 15, 0, 16, 90, 0, 5, true)));

        sceneObjects.add(new SceneObject(new Plane(0, 0, -4, 10, 16)));


        // load light sources
        sceneLights.add(new LightSource(0, 10, 6, 16));
        //sceneLights.add(new LightSource(4, -8, 4, 16));

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
        // get closest object in the scene
        SceneObject closestObject = null;
        double lowestDist = Double.POSITIVE_INFINITY;
        if (maxIters != MAXITERS){
            closestObject = getClosestObject(x, y, z);
            lowestDist = closestObject.signedDistTo(x, y, z);
        }
        else {
            lowestDist = 0.001;
        }
        // if render distance is hit or a object is contacted calculate its shading and return
        if (lowestDist < 0.0001){
            // once the first collision is found calculate the shading
            double[] normalVector = closestObject.getNormalVector(x, y, z);
            double[] lightVector = null;
            double luminence = 0;
            double luminenceFromLight;

            // calculate the luminance of the pixel being shaded
            for (LightSource light : sceneLights){
                lightVector = light.getNormalVector(x, y, z);
                // check if the light can actually reach the point
                double xNext = x + 0.01 * lightVector[0];
                double yNext = y + 0.01 * lightVector[1];
                double zNext = z + 0.01 * lightVector[2];
                if (inShadow(lightVector, xNext, yNext, zNext, light.x, light.y, light.z, xNext, yNext, zNext, MAXITERS)){
                    // if the light can reach the point give it the luminance that it deserves
                    luminenceFromLight = dotProduct(lightVector[0], lightVector[1], lightVector[2], normalVector[0], normalVector[1], normalVector[2]);
                    luminence += luminenceFromLight;
                }
                else{
                    // TODO make shadows dark but not black
                }
            }

            // check if the object has reflectivity and if so reflect the ray and find its next collision
            if (closestObject.getReflectivity() && bounceIter != 0){
                // calculate the new angle for the light to follow
                // using this equation r = d-2(d.n)n 
                double dotProduct = dotProduct(vector[0], vector[1], vector[2], normalVector[0], normalVector[1], normalVector[2]);
                vector[0] = vector[0] - (2 * dotProduct * normalVector[0]);
                vector[1] = vector[1] - (2 * dotProduct * normalVector[1]);
                vector[2] = vector[2] - (2 * dotProduct * normalVector[2]);

                // calculate next starting point
                double xNext = x + lowestDist * vector[0];
                double yNext = y + lowestDist * vector[1];
                double zNext = z + lowestDist * vector[2];
                
                // recursivly call this function to get the value
                return getPixelValue(vector, xNext, yNext, zNext, MAXITERS, bounceIter - 1);
            }


            double color = closestObject.getColor();
            return getReflectionColor(color, luminence);
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

    // function to find out if a given point should be illuminated or in shadow
    public boolean inShadow(double[] vector, double x, double y, double z, double xLight, double yLight, double zLight, double originalX, double originalY, double originalZ, int maxIters){
        // get closest object in the scene
        SceneObject closestObject = null;
        double lowestDist;
        if (maxIters != MAXITERS){
            closestObject = getClosestObject(x, y, z);
            lowestDist = closestObject.signedDistTo(x, y, z);
        }
        else {
            lowestDist = 0.01;
        }

        // if we hit an object the pixle should be in shadow so return false
        if (lowestDist < 0.001){
            return false;
        }
        // if we passed the light there where no objects inbetween so return true
        else if (((x < xLight && x < originalX) || (x > xLight && x > originalX))
                && ((y < yLight && y < originalY) || (y > yLight && y > originalY))
                && ((z < zLight && z < originalZ) || (z > zLight && z > originalZ))){
            return true;
        }
        
        // calculate next starting point
        double xNext = x + lowestDist * vector[0];
        double yNext = y + lowestDist * vector[1];
        double zNext = z + lowestDist * vector[2];
        return inShadow(vector, xNext, yNext, zNext, xLight, yLight, zLight, originalX, originalY, originalZ, maxIters - 1);
    }

    // get the closest object in the scene
    public SceneObject getClosestObject(double x,  double y, double z){
        // get closest signed distance
        double dist;
        double lowestDist = Double.POSITIVE_INFINITY;
        SceneObject closestObject = null;
        
        for (int i = 0; i < sceneObjects.size(); i++){
            dist = ((SceneObject) sceneObjects.get(i)).signedDistTo(x,y,z);

            if (dist < lowestDist){
                lowestDist = dist;
                closestObject = (SceneObject) sceneObjects.get(i);
            }
        }
        return closestObject;
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
        if (color >= pixValues.length - 1){
            return pixValues[pixValues.length - 1];
        }
        return pixValues[(int) color];
    }

    // calculate the dot product of two normalized vectors
    public static double dotProduct(double x1, double y1, double z1, double x2, double y2, double z2){
        return (x1*x2)+(y1*y2)+(z1*z2);
    }

    // convert angle values in to a unit vector
    public static double[] angleToVector(double a, double b){
        double z = Math.sin(b * Math.PI / 180);
        double x = (Math.cos(b * Math.PI / 180)) * Math.cos(a * Math.PI / 180);
        double y = (Math.cos(b * Math.PI / 180)) * Math.sin(a * Math.PI / 180);
        double[] vector = {x,y,z};
        return vector;
    }

    // its best not to use this since it is slower but it also can intrduce hard to find bugs
    public static double[] vectorToAngle(double[] vector){
        double x = vector[0];
        double y = vector[1];
        double z = vector[2];
        double yaw = Math.atan(x/y) * (180/Math.PI);
        double pitch = Math.atan(z/x) * (180/Math.PI);
        double[] angle = {yaw, pitch};
        return angle;
    }

    // normalizes a vector
    public static double[] normalizeVector(double x, double y, double z){
        double hypotonusDist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        double[] vector = {(x/hypotonusDist), (y/hypotonusDist), (z/hypotonusDist)};
        return vector;
    }

    // turns a vector around
    public static double[] invertVector(double[] vector){
        vector[0] = -vector[0];
        vector[1] = -vector[1];
        vector[2] = -vector[2];
        return vector;
    }

    // blend two colors together evenly
    public double blendColor(double c1, double c2){
        return (c1+c2)/2;
    }
    
}
