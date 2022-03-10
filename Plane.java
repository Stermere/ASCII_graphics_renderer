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

    public Plane(double x_, double y_, double z_, double c_){
        z = z_;
        c = c_;
        

    }
    
    public Plane(double x_, double y_, double z_, double c_, double yaw, double pitch, boolean reflectivity_){
        x = x_;
        y = y_;
        z = z_;
        c = c_;
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
        double[] norm = getNormalVector();
        // formula for dist 
        //float dist = dotProduct(p.normal, (vectorSubtract(point, p.point)));
        double dist = Scene.dotProduct(norm[0], norm[1], norm[2], x_-x, y_-y, z_-z);
        
        return dist;
    }

    public double[] getNormalVector(){
        return normalVector;

    }
}
