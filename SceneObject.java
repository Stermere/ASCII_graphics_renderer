// the interface between the scene renderer and different object types

/* to be honest if you are reading this I hate this class and want to find a more clean way to do this 
 * that doesnt also cause the scene renderer to become less clean and since that is the current code that grows
 * and changes more frequently this is where the bad code hides
*/

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
            return plane.signedDistTo(x_, y_, z_);
        }
        return (Double) null;

    }

    public double[] getNormalVector(double x_, double y_, double z_){
        if (sphere != null){
            return sphere.getNormalVector(x_, y_, z_);
        }
        else if (plane != null) {
            return plane.getNormalVector();
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

    public boolean getReflectivity(){
        if (sphere != null){
            return sphere.reflectivity;
        }
        else if (plane != null){
            return plane.reflectivity;
        }
        return false;
    }

    public void changeCords(double x, double y, double z){
        if (sphere != null){
            sphere.x += x;
            sphere.y += y;
            sphere.z += z;
            
        }
        else if (plane != null){
            plane.x += x;
            plane.y += y;
            plane.z += z;

            
        }
    }
}
