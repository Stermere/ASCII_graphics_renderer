// class to define a 2d plane in 3d space
// currently only implemented to make a floor to make a more complete scene

import java.lang.Math;

public class Plane {
    double z = 0;
    double c = 0;
    boolean reflectivity = false;

    public Plane(double z_, double c_){
        z = z_;
        c = c_;

    }

    public Plane(double z_, double c_, boolean reflectivity_){
        z = z_;
        c = c_;
        reflectivity = reflectivity_;

    }

    public double distTo(double z_){
        // since it is a 2d plane just pass through to signed dist
        return signedDistTo(z_);
    }

    public double signedDistTo(double z_){
        return Math.abs(z - z_);
    }

    public double[] getNormalVector(double z_){
        // return the unit vector of a flat plane for now 
        // since this is only used for the ground might as well not waste time calculating the same vector
        double[] unitVector = {0, 0, 1};
        return unitVector;
    }
}
