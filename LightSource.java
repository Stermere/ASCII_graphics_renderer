public class LightSource {
    double x = 0;
    double y = 0;
    double z = 0;
    double yaw = 0;
    double pitch = 0;

    // intensity can be anything between 0 and 16
    double intensity = 0;

    public LightSource (double x_, double y_, double z_, double intensity_){
        x = x_;
        y = y_;
        z = z_;
        intensity = intensity_;
    }

    public double[] getNormalVector(double x_, double y_, double z_){
        // get the normal of the light that is refelcting of the surface
        double hypotonusDist = Math.sqrt(Math.pow(x_-x, 2) + Math.pow(y_-y, 2) + Math.pow(z_-z, 2));
        double[] vector = {((x-x_)/hypotonusDist), ((y-y_)/hypotonusDist), ((z-z_)/hypotonusDist)};
        return vector;
    }

    public void setX(double val){
        x = val;
    }

    public void setY(double val){
        y = val;
    }

    public void setZ(double val){
        z = val;
    }
}
