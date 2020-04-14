package net.blerf.ftl.parser.save.drone;

public class StandaloneDroneState {
    private String droneId = null;
    private DronePodState dronePod = null;
    private int unknownAlpha = 0;
    private int unknownBeta = 0;
    private int unknownGamma = 0;


    /**
     * Constructs an incomplete StandaloneDroneState.
     */
    public StandaloneDroneState() {
    }

    public void setDroneId( String s ) { droneId = s; }
    public String getDroneId() { return droneId; }

    public void setDronePod( DronePodState pod ) { dronePod = pod; }
    public DronePodState getDronePod() { return dronePod; }

    public void setUnknownAlpha( int n ) { unknownAlpha = n; }
    public void setUnknownBeta( int n ) { unknownBeta = n; }
    public void setUnknownGamma( int n ) { unknownGamma = n; }

    public int getUnknownAlpha() { return unknownAlpha; }
    public int getUnknownBeta() { return unknownBeta; }
    public int getUnknownGamma() { return unknownGamma; }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "DroneId:           %s\n", droneId ) );

        result.append( "\nDrone Pod...\n" );
        result.append( dronePod.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );

        result.append( "\n" );

        result.append( String.format( "Alpha?:            %3d\n", unknownAlpha ) );
        result.append( String.format( "Beta?:             %3d\n", unknownBeta ) );
        result.append( String.format( "Gamma?:            %3d\n", unknownGamma ) );

        return result.toString();
    }
}
