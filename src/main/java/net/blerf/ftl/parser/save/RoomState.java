package net.blerf.ftl.parser.save;

import java.util.ArrayList;
import java.util.List;

public class RoomState {
    private int oxygen = 100;
    private List<SquareState> squareList = new ArrayList<>();
    private int stationSquare = -1;
    private SavedGameParser.StationDirection stationDirection = SavedGameParser.StationDirection.NONE;


    /**
     * Constructs an incomplete RoomState.
     *
     * It will need squares.
     */
    public RoomState() {
    }

    /**
     * Copy constructor.
     *
     * Each SquareState will be copy-constructed as well.
     */
    public RoomState(RoomState srcRoom ) {
        oxygen = srcRoom.getOxygen();

        for ( SquareState square : srcRoom.getSquareList() ) {
            squareList.add( new SquareState( square ) );
        }

        stationSquare = srcRoom.getStationSquare();
        stationDirection = srcRoom.getStationDirection();
    }

    /**
     * Set's the oxygen percentage in the room.
     *
     * When this is below 5, a warning appears.
     *
     * At 0, the game changes the room's appearance.
     *   Since 1.03.1, it paints red stripes on the floor.
     *   Before that, it highlighted the walls orange.
     *
     * @param n 0-100
     */
    public void setOxygen( int n ) { oxygen = n; }
    public int getOxygen() { return oxygen; }

    /**
     * Sets a room square for a station, to man a system.
     *
     * When the system's capacity is 0, this is not set.
     *
     * The station's direction must be set as well.
     *
     * This was introduced in FTL 1.5.4.
     * @param n the room square index, or -1 for none
     */
    public void setStationSquare( int n ) { stationSquare = n; }
    public int getStationSquare() { return stationSquare; }

    /**
     * Sets which edge of a room square a station should be placed at.
     *
     * When the system's capacity is 0, this is not set.
     *
     * The station's room square must be set as well.
     *
     * This was introduced in FTL 1.5.4.
     * @param n 0=D,1=R,2=U,3=L,4=None
     */
    public void setStationDirection( SavedGameParser.StationDirection d ) { stationDirection = d; }
    public SavedGameParser.StationDirection getStationDirection() { return stationDirection; }


    /**
     * Adds a floor square to the room.
     *
     * Squares are indexed horizontally, left-to-right, wrapping
     * into the next row down.
     */
    public void addSquare( SquareState square ) {
        squareList.add( square );
    }
    public SquareState getSquare(int n ) {
        return squareList.get( n );
    }

    public List<SquareState> getSquareList() { return squareList; }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Oxygen: %3d%%\n", oxygen ) );
        result.append( String.format( "Station Square: %2d, Station Direction: %s\n", stationSquare, stationDirection.toString() ) );

        result.append( "Squares...\n" );
        for ( SquareState square : squareList ) {
            result.append( square.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        return result.toString();
    }
}
