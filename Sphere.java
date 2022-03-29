// Sphere class
public class Sphere{
    // store the center of the Sphere, its radius, and its color
    double x = 0;
    double y = 0;
    double z = 0;
    double r = .5;
    double c = 16;
    boolean reflectivity = false;

    public Sphere(double x_, double y_, double z_, double r_, double c_){
        x = x_;
        y = y_;
        z = z_;
        r = r_;
        c = c_;
    }

    public Sphere(double x_, double y_, double z_, double r_, double c_, boolean reflectivity_){
        x = x_;
        y = y_;
        z = z_;
        r = r_;
        c = c_;
        reflectivity = reflectivity_;
    }

    public Sphere(double x_, double y_, double z_){
        x = x_;
        y = y_;
        z = z_;
    }

    // return distance to center
    public double distTo(double x_, double y_, double z_){
        return Math.sqrt((Math.pow(x-x_,2) + Math.pow(y-y_,2) + Math.pow(z-z_,2)));
    }

    // return distance to side
    public double signedDistTo(double x_, double y_, double z_){
        return Math.sqrt((Math.pow(x-x_,2) + Math.pow(y-y_,2) + Math.pow(z-z_,2))) - r;
    }

    public double[] getNormalVector(double x_, double y_, double z_){
        double hypotonusDist = Math.sqrt(Math.pow(x_-x, 2) + Math.pow(y_-y, 2) + Math.pow(z_-z, 2));
        double[] vector = {((x_-x)/hypotonusDist), ((y_-y)/hypotonusDist), ((z_-z)/hypotonusDist)};
        return vector;
    }
}

