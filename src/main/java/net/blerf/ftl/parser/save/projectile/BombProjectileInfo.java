package net.blerf.ftl.parser.save.projectile;

public class BombProjectileInfo extends ExtendedProjectileInfo {
    private int unknownAlpha = 0;
    private int fuseTicks = 400;
    private int unknownGamma = 0;
    private int unknownDelta = 0;
    private boolean arrived = false;


    /**
     * Constructor.
     */
    public BombProjectileInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected BombProjectileInfo(BombProjectileInfo srcInfo ) {
        super( srcInfo );
        unknownAlpha = srcInfo.getUnknownAlpha();
        fuseTicks = srcInfo.getFuseTicks();
        unknownGamma = srcInfo.getUnknownGamma();
        unknownDelta = srcInfo.getUnknownDelta();
        arrived = srcInfo.hasArrived();
    }

    @Override
    public BombProjectileInfo copy() { return new BombProjectileInfo( this ); }

    /**
     * Unknown.
     *
     * Observed values: 0.
     */
    public void setUnknownAlpha( int n ) { unknownAlpha = n; }
    public int getUnknownAlpha() { return unknownAlpha; }

    /**
     * Sets time elapsed while this bomb is about to detonate.
     *
     * After fading into a room, this decrements ticks from 400. At 0, the
     * bomb's casing (flightAnimId) disappears, and the explosion anim
     * plays. This value continues decrementing to some negative number
     * (varies by weapon), until the explosion completes, and the
     * projectile is gone.
     *
     * Changing this from a positive value to a higher one will delay the
     * detonation. Once negative, the explosion will have already started,
     * and setting a new positive value will only make the casing visible
     * amidst the blast for whatever time is left of that animation.
     *
     * Observed values: 400 (During fade-in), 356, -205, -313, -535.
     *
     * @see #setArrived(boolean)
     */
    public void setFuseTicks( int n ) { fuseTicks = n; }
    public int getFuseTicks() { return fuseTicks; }

    /**
     * Unknown.
     *
     * Observed values: 0.
     */
    public void setUnknownGamma( int n ) { unknownGamma = n; }
    public int getUnknownGamma() { return unknownGamma; }

    /**
     * Unknown.
     *
     * Observed values: 0.
     */
    public void setUnknownDelta( int n ) { unknownDelta = n; }
    public int getUnknownDelta() { return unknownDelta; }

    /**
     * Sets whether this bomb has begun fading in.
     *
     * When FTL sees this is false, the value will become true, and the
     * bomb will begin fading into a room. If set to false once more, the
     * fade will start over. Fuse ticks will cease decrementing while this
     * is false or while the fade is still in progress.
     *
     * When FTL sees this is true, the bomb casing is fully opaque and fuse
     * ticks immediately resume decrementing.
     *
     * Newly spawned projectiles have this set to false.
     *
     * @see #setFuseTicks(int)
     */
    public void setArrived( boolean b ) { arrived = b; }
    public boolean hasArrived() { return arrived; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Type:               Bomb Info\n" ) );
        result.append( String.format( "Alpha?:             %7d\n", unknownAlpha ) );
        result.append( String.format( "Fuse Ticks:         %7d (Explodes at 0)\n", fuseTicks ) );
        result.append( String.format( "Gamma?:             %7d\n", unknownGamma ) );
        result.append( String.format( "Delta?:             %7d\n", unknownDelta ) );
        result.append( String.format( "Arrived:            %7b\n", arrived ) );

        return result.toString();
    }
}
