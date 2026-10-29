package gaydev.yaiden.api.physics;

public class PhysicsVec3 {

    public float x;
    public float y;
    public float z;

    public PhysicsVec3() {
        this(0.0F, 0.0F, 0.0F);
    }

    public PhysicsVec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PhysicsVec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PhysicsVec3 set(PhysicsVec3 other) {
        return set(other.x, other.y, other.z);
    }

    public PhysicsVec3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PhysicsVec3 add(PhysicsVec3 other) {
        return add(other.x, other.y, other.z);
    }

    public PhysicsVec3 multiply(float value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public PhysicsVec3 copy() {
        return new PhysicsVec3(x, y, z);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void zero() {
        x = 0.0F;
        y = 0.0F;
        z = 0.0F;
    }
}