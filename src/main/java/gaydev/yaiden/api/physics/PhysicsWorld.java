
package gaydev.yaiden.api.physics;

import java.util.ArrayList;
import java.util.List;

import gaydev.yaiden.api.crash.InvalidPhysicsException;
import gaydev.yaiden.api.crash.YaidensError;

public final class PhysicsWorld {

    private static final YaidensError ERRORS = new YaidensError();

    private final List<PhysicsBody> bodies = new ArrayList<>();
    private final List<Spring> springs = new ArrayList<>();

    private final PhysicsVec3 gravity = new PhysicsVec3();

    private float fixedDeltaTime = 1.0F / 60.0F;
    private float accumulator = 0.0F;
    private int maxSubSteps = 8;
    private int lastStepCount = 0;

    private long totalSteps = 0L;

    public void addBody(PhysicsBody body)    {
        if (body == null) {
            throw ERRORS.YaidensException(
                "Cannot add a null body to PhysicsWorld."
            );
        }

        if (bodies.contains(body)) {
            return;
        }

        body.attachToWorld(this);
        bodies.add(body);
    }

    public boolean removeBody(PhysicsBody body) {
        if (body == null || !bodies.remove(body)) {
            return false;
        }

        body.detachFromWorld(this);

        return true;
    }

    public void addSpring(Spring spring)    {
        if (spring == null) {
            throw ERRORS.YaidensException(
                "Cannot add a null spring to PhysicsWorld."
            );
        }

        if (!springs.contains(spring)) {
            springs.add(spring);
        }
    }

    public boolean removeSpring(Spring spring) {
        return springs.remove(spring);
    }

    public void tick(float deltaTime)    {
        PhysicsValidator.requireNonNegative(
            deltaTime,
            "PhysicsWorld.deltaTime"
        );

        accumulator += deltaTime;
        lastStepCount = 0;

        while (accumulator >= fixedDeltaTime
                && lastStepCount < maxSubSteps) {

            simulateStep(fixedDeltaTime);

            accumulator -= fixedDeltaTime;
            lastStepCount++;
            totalSteps++;
        }

        // Avoid an unbounded backlog after a long pause.
        if (lastStepCount == maxSubSteps
                && accumulator >= fixedDeltaTime) {
            accumulator %= fixedDeltaTime;
        }
    }

    private void simulateStep(float deltaTime)    {
        // Apply world gravity as a force.
        for (PhysicsBody body : bodies) {
            if (!body.isFixed()) {
                body.addForce(
                    gravity.x * body.getMass(),
                    gravity.y * body.getMass(),
                    gravity.z * body.getMass()
                );
            }
        }

        // Springs must apply forces before bodies are updated.
        for (Spring spring : springs) {
            spring.applyForce();
        }

        // Integrate the bodies after all forces are collected.
        for (PhysicsBody body : bodies) {
            body.tick(deltaTime);
        }
    }

    public void setGravity(float x, float y, float z)    {
        PhysicsValidator.requireFinite(x, "gravity.x");
        PhysicsValidator.requireFinite(y, "gravity.y");
        PhysicsValidator.requireFinite(z, "gravity.z");

        gravity.set(x, y, z);
    }

    public PhysicsVec3 getGravity() {
        PhysicsVec3 result = new PhysicsVec3();
        result.set(gravity);
        return result;
    }

    public void setFixedDeltaTime(float fixedDeltaTime)    {
        PhysicsValidator.requirePositive(
            fixedDeltaTime,
            "PhysicsWorld.fixedDeltaTime"
        );

        this.fixedDeltaTime = fixedDeltaTime;
        accumulator = 0.0F;
    }

    public float getFixedDeltaTime() {
        return fixedDeltaTime;
    }

    public void setMaxSubSteps(int maxSubSteps)    {
        if (maxSubSteps <= 0) {
            throw ERRORS.YaidensException(
                "maxSubSteps must be greater than zero."
            );
        }

        this.maxSubSteps = maxSubSteps;
    }

    public int getMaxSubSteps() {
        return maxSubSteps;
    }

    public int getLastStepCount() {
        return lastStepCount;
    }

    public long getTotalSteps() {
        return totalSteps;
    }

    public float getInterpolationAlpha() {
        return accumulator / fixedDeltaTime;
    }

    public List<PhysicsBody> getBodies() {
        return List.copyOf(bodies);
    }

    public List<Spring> getSprings() {
        return List.copyOf(springs);
    }

    public int getBodyCount() {
        return bodies.size();
    }

    public int getSpringCount() {
        return springs.size();
    }

    public void clearSprings() {
        springs.clear();
    }

    public void clear() {
        clearSprings();

        for (PhysicsBody body : bodies) {
            body.detachFromWorld(this);
        }

        bodies.clear();
        accumulator = 0.0F;
        lastStepCount = 0;
    }
}
