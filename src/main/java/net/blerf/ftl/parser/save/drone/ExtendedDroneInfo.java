package net.blerf.ftl.parser.save.drone;

/**
 * Extra drone info stored separately from the original DroneState.
 *
 * This was introduced in FTL 1.5.4.
 */
public class ExtendedDroneInfo {
    private boolean deployed = false;
    private boolean armed = false;
    private DronePodState dronePod = null;


    /**
     * Constructs an incomplete ExtendedDroneInfo.
     *
     * It will need a DronePodState.
     */
    public ExtendedDroneInfo() {
    }

    /**
     * Copy-constructor.
     *
     * The drone pod will be copy-constructed as well.
     */
    public ExtendedDroneInfo(ExtendedDroneInfo srcInfo ) {
        deployed = srcInfo.isDeployed();
        armed = srcInfo.isArmed();

        if ( srcInfo.getDronePod() != null ) {
            dronePod = new DronePodState( srcInfo.getDronePod() );
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
        setDeployed( false );
        setArmed( false );

        if ( getDronePod() != null ) {
            getDronePod().commandeer();
        }
    }

    /**
     * Sets whether the drone's body/pod exists.
     *
     * Re-arming an already deployed drone doesn't cost a drone part.
     *
     * After defeating a nearby ship, and the window disappears, player
     * drone pods there are lost and this is set to false.
     */
    public void setDeployed( boolean b ) { deployed = b; }
    public boolean isDeployed() { return deployed; }

    /**
     * Sets whether this drone is powered.
     *
     * TODO: See what happens when this conflists with the DroneState.
     *
     * @see DroneState#setArmed(boolean)
     */
    public void setArmed( boolean b ) { armed = b; }
    public boolean isArmed() { return armed; }

    /**
     * Sets a drone pod, which varies by DroneType.
     *
     * For BATTLE and REPAIR, this should be null.
     */
    public void setDronePod( DronePodState pod ) { dronePod = pod; }
    public DronePodState getDronePod() { return dronePod; }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Deployed:        %5b\n", deployed ) );
        result.append( String.format( "Armed:           %5b\n", armed ) );

        result.append( "\nDrone Pod...\n" );
        if ( dronePod != null ) {
            result.append( dronePod.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        } else {
            result.append( "N/A\n" );
        }

        return result.toString();
    }
}
