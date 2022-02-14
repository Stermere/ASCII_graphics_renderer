// the interface between the scene renderer and different object types

public class SceneObject {
    // all but one object should be null
    Sphere sphere = null;
    Plane plane = null;

    public SceneObject(Sphere object){
        sphere = object;
    }
    public SceneObject(Plane object){
        plane = object;
    }

    public double signedDistTo(double x_, double y_, double z_){
        if (sphere != null){
            return sphere.signedDistTo(x_, y_, z_);
        }
        else if (plane != null) {
            return plane.signedDistTo(z_);
        }
        return (Double) null;

    }

    public double[] getNormalVector(double x_, double y_, double z_){
        if (sphere != null){
            return sphere.getNormalVector(x_, y_, z_);
        }
        else if (plane != null) {
            return plane.getNormalVector(z_);
        }
        return null;
    
    }

    public double getColor(){
        if (sphere != null){
            return sphere.c;
        }
        else if (plane != null) {
            return plane.c;
        }
        return (Double) null;
    }
}
