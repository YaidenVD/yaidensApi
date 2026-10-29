
package gaydev.yaiden.api.physics;

public class Spring {

    private final PhysicsBody bodyA;
    private final PhysicsBody bodyB;

    private float restLength;
    private float stiffness;
    private float damping;

    public Spring(
            PhysicsBody bodyA,
            PhysicsBody bodyB,
            float restLength,
            float stiffness,
            float damping
    ) {
        if (bodyA == null || bodyB == null) {
            throw new IllegalArgumentException("Bodies cannot be null");
        }

        if (bodyA == bodyB) {
            throw new IllegalArgumentException("Bodies must be different");
        }

        if (restLength < 0.0F || stiffness < 0.0F || damping < 0.0F) {
            throw new IllegalArgumentException(
                    "Spring parameters cannot be negative"
            );
        }

        this.bodyA = bodyA;
        this.bodyB = bodyB;
        this.restLength = restLength;
        this.stiffness = stiffness;
        this.damping = damping;
    }

    public void applyForce() {
        PhysicsVec3 positionA = bodyA.getPosition();
        PhysicsVec3 positionB = bodyB.getPosition();

        float dx = positionB.x - positionA.x;
        float dy = positionB.y - positionA.y;
        float dz = positionB.z - positionA.z;

        float distanceSquared = dx * dx + dy * dy + dz * dz;

        if (distanceSquared < 0.000001F) {
            return;
        }

        float distance = (float) Math.sqrt(distanceSquared);

        // Normalized direction from A to B
        float nx = dx / distance;
        float ny = dy / distance;
        float nz = dz / distance;

        // Hooke's law: F = -k * displacement
        float displacement = distance - restLength;

        // Relative velocity along the spring direction
        PhysicsVec3 velocityA = bodyA.getVelocity();
        PhysicsVec3 velocityB = bodyB.getVelocity();

        float relativeVelocity =
                (velocityB.x - velocityA.x) * nx
                + (velocityB.y - velocityA.y) * ny
                + (velocityB.z - velocityA.z) * nz;

        float forceMagnitude =
                stiffness * displacement
                - damping * relativeVelocity;

        float fx = forceMagnitude * nx;
        float fy = forceMagnitude * ny;
        float fz = forceMagnitude * nz;

        if (!bodyA.isFixed()) {
            bodyA.addForce(fx, fy, fz);
        }

        if (!bodyB.isFixed()) {
            bodyB.addForce(-fx, -fy, -fz);
        }
    }

    public PhysicsBody getBodyA() {
        return bodyA;
    }

    public PhysicsBody getBodyB() {
        return bodyB;
    }

    public float getRestLength() {
        return restLength;
    }

    public void setRestLength(float restLength) {
        if (restLength < 0.0F) {
            throw new IllegalArgumentException(
                    "Rest length cannot be negative"
            );
        }

        this.restLength = restLength;
    }

    public float getStiffness() {
        return stiffness;
    }

    public void setStiffness(float stiffness) {
        if (stiffness < 0.0F) {
            throw new IllegalArgumentException(
                    "Stiffness cannot be negative"
            );
        }

        this.stiffness = stiffness;
    }

    public float getDamping() {
        return damping;
    }

    public void setDamping(float damping) {
        if (damping < 0.0F) {
            throw new IllegalArgumentException(
                    "Damping cannot be negative"
            );
        }

        this.damping = damping;
    }
}
