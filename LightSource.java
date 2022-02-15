public class LightSource {
    double x = 0;
    double y = 0;
    double z = 0;
    double yaw = 0;
    double pitch = 0;

    // intensity can be anything between 0 and 16
    double intensity = 0;

    public LightSource (double x_, double y_, double z_, double yaw_, double pitch_, double intensity_){
        x = x_;
        y = y_;
        z = z_;
        yaw = yaw_;
        pitch = pitch_;
        intensity = intensity_;
    }

    public double[] getAngleToSelf(double x_, double y_, double z_){
        // get the angle from point that is being shaded to the light source
        x_ = x_ - x;
        y_ = y_ - y;
        z_ = z_ - z;
        double yaw_ = Math.atan(y_/x_);
        double pitch_ = Math.atan(z_/y_);
        double[] angle = {yaw_, pitch_};
        return angle;
    }
}
