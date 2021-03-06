package net.blerf.ftl.parser.save.drone;

public class DroneState {
    private String droneId = null;
    private boolean armed = false;
    private boolean playerControlled = false;
    private int bodyX = -1, bodyY = -1;
    private int bodyRoomId = -1;
    private int bodyRoomSquare = -1;
    private int health = 1;
    private ExtendedDroneInfo droneInfo = null;


    /**
     * Constructs an incomplete DroneState.
     *
     * It will need a droneId.
     *
     * For FTL 1.5.4+ saved games, extended info may be needed.
     */
    public DroneState() {
    }

    /**
     * Constructs an incomplete DroneState.
     *
     * For FTL 1.5.4+ saved games, extended info may be needed.
     */
    public DroneState(String droneId ) {
        this.droneId = droneId;
    }

    /**
     * Copy constructor.
     *
     * The extended info will be copy-constructed as well.
     */
    public DroneState(DroneState srcDrone ) {
        droneId = srcDrone.getDroneId();
        armed = srcDrone.isArmed();
        playerControlled = srcDrone.isPlayerControlled();
        bodyX = srcDrone.getBodyX();
        bodyY = srcDrone.getBodyY();
        bodyRoomId = srcDrone.getBodyRoomId();
        bodyRoomSquare = srcDrone.getBodyRoomSquare();
        health = srcDrone.getHealth();

        if ( srcDrone.getExtendedDroneInfo() != null ) {
            droneInfo = new ExtendedDroneInfo( srcDrone.getExtendedDroneInfo() );
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public void commandeer() {
        setArmed( false );
        setPlayerControlled( false );

        if ( getExtendedDroneInfo() != null ) {
            getExtendedDroneInfo().commandeer();
        }
    }


    public void setDroneId( String s ) { droneId = s; }
    public String getDroneId() { return droneId; }

    /**
     * Sets whether this drone is powered.
     *
     * @see ExtendedDroneInfo#setArmed(boolean)
     */
    public void setArmed( boolean b ) { armed = b; }
    public boolean isArmed() { return armed; }

    /**
     * Sets whether this drone is controlled by the player.
     *
     * When the drone is not armed, this should be set to false.
     */
    public void setPlayerControlled( boolean b ) { playerControlled = b; }
    public boolean isPlayerControlled() { return playerControlled; }

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
     *
     * Note: This is only set by drones which have a body on their own ship.
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
     *
     * Note: This is only set by drones which have a body on their own ship.
     */
    public void setBodyRoomId( int n ) { bodyRoomId = n; }
    public void setBodyRoomSquare( int n ) { bodyRoomSquare = n; }
    public int getBodyRoomId() { return bodyRoomId; }
    public int getBodyRoomSquare() { return bodyRoomSquare; }

    public void setHealth( int n ) { health = n; }
    public int getHealth() { return health; }

    /**
     * Sets additional drone fields.
     *
     * Advanced Edition added extra drone fields at the end of saved game
     * files. They're nested inside this class for convenience.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setExtendedDroneInfo( ExtendedDroneInfo droneInfo ) { this.droneInfo = droneInfo; }
    public ExtendedDroneInfo getExtendedDroneInfo() { return droneInfo; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "DroneId:           %s\n", droneId ) );
        result.append( String.format( "Armed:             %5b\n", armed ) );
        result.append( String.format( "Health:            %5d\n", health ) );
        result.append( String.format( "Body Position:     %3d,%3d\n", bodyX, bodyY ) );
        result.append( String.format( "Body Room Id:      %5d\n", bodyRoomId ) );
        result.append( String.format( "Body Room Square:  %5d\n", bodyRoomSquare ) );
        result.append( String.format( "Player Controlled: %5b\n", playerControlled ) );

        result.append( "\nExtended Drone Info...\n" );
        if ( droneInfo != null ) {
            result.append( droneInfo.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        return result.toString();
    }
}
