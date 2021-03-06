package net.blerf.ftl.parser.save.drone;

/**
 * Extended boarder drone info.
 *
 * Boarder drones exclusively store body info in ExtendedDronePodInfo.
 * The traditional DroneState's body fields remain at inoperative defaults.
 *
 * In FTL 1.01-1.03.3, Boarder drone bodies were actual crew on foreign
 * ships.
 *
 * @see DroneState
 */
public class BoarderDronePodInfo extends ExtendedDronePodInfo {
    private int unknownAlpha = 0;
    private int unknownBeta = 0;
    private int unknownGamma = 0;
    private int unknownDelta = 0;
    private int bodyHealth = 1;
    private int bodyX = -1, bodyY = -1;
    private int bodyRoomId = -1;
    private int bodyRoomSquare = -1;

    /**
     * Constructor.
     */
    public BoarderDronePodInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected BoarderDronePodInfo(BoarderDronePodInfo srcInfo ) {
        super( srcInfo );
        unknownAlpha = srcInfo.getUnknownAlpha();
        unknownBeta = srcInfo.getUnknownBeta();
        unknownGamma = srcInfo.getUnknownGamma();
        unknownDelta = srcInfo.getUnknownDelta();
        bodyHealth = srcInfo.getBodyHealth();
        bodyX = srcInfo.getBodyX();
        bodyY = srcInfo.getBodyY();
        bodyRoomId = srcInfo.getBodyRoomId();
        bodyRoomSquare = srcInfo.getBodyRoomSquare();
    }

    @Override
    public BoarderDronePodInfo copy() { return new BoarderDronePodInfo( this ); }


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    @Override
    public void commandeer() {
        // TODO
    }


    public void setUnknownAlpha( int n ) { unknownAlpha = n; }
    public int getUnknownAlpha() { return unknownAlpha; }

    public void setUnknownBeta( int n ) { unknownBeta = n; }
    public int getUnknownBeta() { return unknownBeta; }

    public void setUnknownGamma( int n ) { unknownGamma = n; }
    public int getUnknownGamma() { return unknownGamma; }

    public void setUnknownDelta( int n ) { unknownDelta = n; }
    public int getUnknownDelta() { return unknownDelta; }

    public void setBodyHealth( int n ) { bodyHealth = n; }
    public int getBodyHealth() { return bodyHealth; }

    /**
     * Sets the position of the drone's body image.
     *
     * Technically the roomId/square fields set the goal location.
     * This field is where the body really is, possibly en route.
     *
     * It's the position of the body image's center, relative to the
     * top-left corner of the floor layout of the ship it's on.
     *
     * This value lingers, even after the body is gone.
     */
    public void setBodyX( int n ) { bodyX = n; }
    public void setBodyY( int n ) { bodyY = n; }
    public int getBodyX() { return bodyX; }
    public int getBodyY() { return bodyY; }

    /**
     * Sets the room this drone's body is in (or at least trying to move
     * toward).
     *
     * When no body is present, this is -1.
     *
     * roomId and roomSquare need to be specified together.
     */
    public void setBodyRoomId( int n ) { bodyRoomId = n; }
    public void setBodyRoomSquare( int n ) { bodyRoomSquare = n; }
    public int getBodyRoomId() { return bodyRoomId; }
    public int getBodyRoomSquare() { return bodyRoomSquare; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Alpha?:             %7d\n", unknownAlpha ) );
        result.append( String.format( "Beta?:              %7d\n", unknownBeta ) );
        result.append( String.format( "Gamma?:             %7d\n", unknownGamma ) );
        result.append( String.format( "Delta?:             %7d\n", unknownDelta ) );
        result.append( String.format( "Body Health:        %7d\n", bodyHealth ) );
        result.append( String.format( "Body Position:      %7d,%7d\n", bodyX, bodyY ) );
        result.append( String.format( "Body Room Id:       %7d\n", bodyRoomId ) );
        result.append( String.format( "Body Room Square:   %7d\n", bodyRoomSquare ) );

        return result.toString();
    }
}
