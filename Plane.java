// class to define a 2d plane in 3d space
// currently only implemented to make a floor to make a more complete scene

public class Plane{
    // define some stuff about the plane
    double c = 0;
    double x = 0;
    double y = 0;
    double z = 0;
    // this angle must be the normal vectors direction
    double[] angleOfNorm = {0, 90};
    double[] normalVector = Scene.angleToVector(angleOfNorm[0], angleOfNorm[1]);
    boolean reflectivity = false;
    double size = 0;

    public Plane(double x_, double y_, double z_, double size_, double c_){
        x = x_;
        y = y_;
        z = z_;
        c = c_;
        size = size_;
 
    }
    
    public Plane(double x_, double y_, double z_, double c_, double yaw, double pitch, double size_, boolean reflectivity_){
        x = x_;
        y = y_;
        z = z_;
        c = c_;
        size = size_;
        reflectivity = reflectivity_;
        angleOfNorm[0] = yaw;
        angleOfNorm[1] = pitch;
        normalVector = Scene.angleToVector(angleOfNorm[0], angleOfNorm[1]);
    }

    public double distTo(double x_, double y_, double z_){
        // since it is a 2d plane just pass through to signed dist
        return signedDistTo(x_, y_, z_);
    }

    public double signedDistTo(double x_, double y_, double z_){
        // formula for dist of a infinant plane
        double dist = Scene.dotProduct(normalVector[0], normalVector[1], normalVector[2], x_-x, y_-y, z_-z);
        
        // limit the size of the plane by doing some trig
        double hypotonusDist = Math.sqrt(Math.pow(x_-x, 2) + Math.pow(y_-y, 2) + Math.pow(z_-z, 2));
        double distFromOrigin = Math.sqrt(Math.pow(hypotonusDist, 2) - Math.pow(dist, 2));
        
        // apply the limit to the dist
        double distConfined = distFromOrigin - size;
        if (distConfined < 0){
            distConfined = 0;
        }
        dist = Math.sqrt(Math.pow(dist, 2) + Math.pow(distConfined, 2));

        return dist;
    }

    public double[] getNormalVector(){
        return normalVector;

    }

}
