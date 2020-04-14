package net.blerf.ftl.parser.save.projectile;

public class EmptyProjectileInfo extends ExtendedProjectileInfo {

    /**
     * Constructor.
     */
    public EmptyProjectileInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected EmptyProjectileInfo(EmptyProjectileInfo srcInfo ) {
        super( srcInfo );
    }

    @Override
    public EmptyProjectileInfo copy() { return new EmptyProjectileInfo( this ); }

    @Override
    public String toString() {
        return "N/A\n";
    }
}
