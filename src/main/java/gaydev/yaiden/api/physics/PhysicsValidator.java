
package gaydev.yaiden.api.physics;

import gaydev.yaiden.api.crash.InvalidPhysicsException;
import gaydev.yaiden.api.crash.YaidensError;

public final class PhysicsValidator {

    private static final YaidensError ERRORS = new YaidensError();

    private PhysicsValidator() {
    }

    public static void requirePositive(float value, String name)    {
        if (!Float.isFinite(value) || value <= 0.0F) {
            throw ERRORS.YaidensException(
                    name + " must be finite and greater than zero. Got: " + value
            );
        }
    }

    public static void requireNonNegative(float value, String name)    {
        if (!Float.isFinite(value) || value < 0.0F) {
            throw ERRORS.YaidensException(
                    name + " must be finite and non-negative. Got: " + value
            );
        }
    }

    public static void requireFinite(float value, String name)    {
        if (!Float.isFinite(value)) {
            throw ERRORS.YaidensException(
                    name + " must be finite. Got: " + value
            );
        }
    }

    public static void requireValidVector(
            PhysicsVec3 vector,
            String name
    )    {
        if (vector == null) {
            throw ERRORS.YaidensException(name + " cannot be null");
        }

        requireFinite(vector.x, name + ".x");
        requireFinite(vector.y, name + ".y");
        requireFinite(vector.z, name + ".z");
    }

    public static void requireValidBody(PhysicsBody body, String name)    {
        if (body == null) {
            throw ERRORS.YaidensException(name + " cannot be null");
        }

        requirePositive(body.getMass(), name + ".mass");
        requireValidVector(body.getPosition(), name + ".position");
        requireValidVector(body.getVelocity(), name + ".velocity");
        requireValidVector(body.getForce(), name + ".force");
    }
}
