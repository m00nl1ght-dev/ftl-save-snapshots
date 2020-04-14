package net.blerf.ftl.parser.save;

public class AnimState {
    private boolean playing = false;
    private boolean looping = false;
    private int currentFrame = 0;
    private int progressTicks = 0;
    private int scale = 1000;
    private int x = -1000;
    private int y = -1000;


    /**
     * Constructor.
     */
    public AnimState() {
    }

    /**
     * Copy constructor.
     */
    public AnimState(AnimState srcAnim ) {
        playing = srcAnim.isPlaying();
        looping = srcAnim.isLooping();
        currentFrame = srcAnim.getCurrentFrame();
        progressTicks = srcAnim.getProgressTicks();
        scale = srcAnim.getScale();
        x = srcAnim.getX();
        y = srcAnim.getY();
    }

    public void setPlaying( boolean b ) { playing = b; }
    public boolean isPlaying() { return playing; }

    public void setLooping( boolean b ) { looping = b; }
    public boolean isLooping() { return looping; }

    /**
     * Sets the current frame of this anim (0-based).
     *
     * Start/end frames during playback vary. Anims, and their important
     * frames, are defined in "animations.xml".
     *
     * FTL seems to clobber this value upon loading, based on the
     * circumstances driving the anim, so editing it is probably useless.
     */
    public void setCurrentFrame( int n ) { currentFrame = n; }
    public int getCurrentFrame() { return currentFrame; }

    /**
     * Sets time elapsed while playing this anim.
     *
     * Technically this doesn't count, so much as remember how far into the
     * anim playback was when the current frame appeared.
     *
     * This value is 1000 / (animSheet's frame count) * (currentFrame).
     * Sometimes that's off by 1 due to rounding somewhere.
     *
     * TODO: That formula matched WeaponModuleState's weaponAnim, at least.
     *
     * FTL seems to clobber this value upon loading, based on the
     * circumstances driving the anim, so editing it is probably useless.
     */
    public void setProgressTicks( int n ) { progressTicks = n; }
    public int getProgressTicks() { return progressTicks; }

    /**
     * Sets a scale factor.
     *
     * Projectiles with flightAnimId "debris_small" set their deathAnim
     * scale to 250.
     *
     * @param n a pseudo-float (1000 is 1.0)
     */
    public void setScale( int n ) { scale = n; }
    public int getScale() { return scale; }

    /**
     * Unknown.
     *
     * Observed values: 0 (when playing), -1000 (when not playing).
     * One time, a missile exploded whose deathAnim had -32000.
     */
    public void setX( int n ) { x = n; }
    public void setY( int n ) { y = n; }
    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Playing:           %7b\n", playing ) );
        result.append( String.format( "Looping?:          %7b\n", looping ) );
        result.append( String.format( "Current Frame:     %7d\n", currentFrame ) );
        result.append( String.format( "Progress Ticks:    %7d\n", progressTicks ) );
        result.append( String.format( "Scale:             %7d (%5.03f)\n", scale, scale/1000f ) );
        result.append( String.format( "X,Y?:                %5d,%5d\n", x, y ) );

        return result.toString();
    }
}
