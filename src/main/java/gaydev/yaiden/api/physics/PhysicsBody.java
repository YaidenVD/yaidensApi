package gaydev.yaiden.api.physics;

import gaydev.yaiden.api.crash.InvalidPhysicsException;
import gaydev.yaiden.api.crash.YaidensError;

public class PhysicsBody {

    private final PhysicsVec3 position = new PhysicsVec3();
    private final PhysicsVec3 previousPosition = new PhysicsVec3();
    private final PhysicsVec3 velocity = new PhysicsVec3();
    private final PhysicsVec3 force = new PhysicsVec3();
    private PhysicsWorld world;
    public static float TICK = 1.0F / 20.0F;

    private float mass = 1.0F;
    private float damping = 0.90F;

    private boolean gravityEnabled = false;
    private float gravity = -9.81F;

    private boolean fixed = false;

    public PhysicsBody() {
    }

    public void tick(float deltaTime)    {
        PhysicsValidator.requireNonNegative(deltaTime, "PhysicsBody.deltaTime");
        previousPosition.set(position);

        if (fixed) {
            velocity.zero();
            force.zero();
            return;
        }

        float inverseMass = 1.0F / mass;

        // Acceleration from forces
        float accelerationX = force.x * inverseMass;
        float accelerationY = force.y * inverseMass;
        float accelerationZ = force.z * inverseMass;

        // Gravity
        if (gravityEnabled) {
            accelerationY += gravity;
        }

        // Integrate velocity
        velocity.x += accelerationX * deltaTime;
        velocity.y += accelerationY * deltaTime;
        velocity.z += accelerationZ * deltaTime;

        // Damping
        velocity.multiply((float) Math.pow(damping, deltaTime * 60.0F));

        // Integrate position
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        position.z += velocity.z * deltaTime;

        // Forces only last for one tick
        force.zero();
        PhysicsValidator.requireValidVector(position, "PhysicsBody.position");
        PhysicsValidator.requireValidVector(velocity, "PhysicsBody.velocity");
        PhysicsValidator.requireValidVector(force, "PhysicsBody.force");
    }

    public void addForce(float x, float y, float z) {
        force.add(x, y, z);
    }

    public void addForce(PhysicsVec3 force) {
        addForce(force.x, force.y, force.z);
    }

    public PhysicsVec3 getPosition() {
        return position;
    }

    public PhysicsVec3 getPreviousPosition() {
        return previousPosition;
    }

    public PhysicsVec3 getVelocity() {
        return velocity;
    }

    public PhysicsVec3 getForce() {
        return force;
    }

    public PhysicsVec3 getInterpolatedPosition(float partialTick) {
        return new PhysicsVec3(
                previousPosition.x + (position.x - previousPosition.x) * partialTick,
                previousPosition.y + (position.y - previousPosition.y) * partialTick,
                previousPosition.z + (position.z - previousPosition.z) * partialTick
        );
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass)    {
        PhysicsValidator.requirePositive(mass, "PhysicsBody.mass");
    this.mass = mass;
}

    public float getDamping() {
        return damping;
    }

    public void setDamping(float damping) {
        this.damping = Math.max(0.0F, Math.min(1.0F, damping));
    }

    public boolean isGravityEnabled() {
        return gravityEnabled;
    }

    public void setGravityEnabled(boolean gravityEnabled) {
        this.gravityEnabled = gravityEnabled;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public void setPosition(float x, float y, float z)    {
        PhysicsValidator.requireFinite(x, "PhysicsBody.position.x");
        PhysicsValidator.requireFinite(y, "PhysicsBody.position.y");
        PhysicsValidator.requireFinite(z, "PhysicsBody.position.z");

        position.set(x, y, z);
        previousPosition.set(position);
}

    public void setVelocity(float x, float y, float z)    {
        PhysicsValidator.requireFinite(x, "PhysicsBody.velocity.x");
        PhysicsValidator.requireFinite(y, "PhysicsBody.velocity.y");
        PhysicsValidator.requireFinite(z, "PhysicsBody.velocity.z");

    velocity.set(x, y, z);
    }

    void attachToWorld(PhysicsWorld world) {
    if (this.world != null && this.world != world) {
        throw new YaidensError().YaidensException(
            "PhysicsBody already belongs to another PhysicsWorld."
        );
    }

    this.world = world;
}

void detachFromWorld(PhysicsWorld world) {
    if (this.world == world) {
        this.world = null;
    }
    }
}