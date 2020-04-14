package net.blerf.ftl.parser.save.projectile;

public abstract class ExtendedProjectileInfo {

    protected ExtendedProjectileInfo() {
    }

    protected ExtendedProjectileInfo(ExtendedProjectileInfo srcInfo ) {
    }

    /**
     * Blindly copy-constructs objects.
     *
     * Subclasses override this with return values of their own type.
     */
    public abstract ExtendedProjectileInfo copy();
}
