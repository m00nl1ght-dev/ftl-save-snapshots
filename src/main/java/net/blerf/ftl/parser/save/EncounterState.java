package net.blerf.ftl.parser.save;

import java.util.ArrayList;
import java.util.List;

public class EncounterState {
    private int shipEventSeed = 0;
    private String surrenderEventId = "";
    private String escapeEventId = "";
    private String destroyedEventId = "";
    private String deadCrewEventId = "";
    private String gotAwayEventId = "";

    private String lastEventId = "";
    private int unknownAlpha = 0;
    private String text = "";
    private int affectedCrewSeed = -1;
    private List<Integer> choiceList = new ArrayList<>();


    public EncounterState() {
    }


    /**
     * Sets a seed to randomly generate the enemy ship (layout, etc).
     *
     * When the player ship visits a beacon, the resulting encounter
     * will use the beacon's enemy ship event seed.
     *
     * When not set, this is 0. After encountering ships, this value lingers.
     */
    public void setShipEventSeed( int n ) { shipEventSeed = n; }
    public int getShipEventSeed() { return shipEventSeed; }

    public void setSurrenderEventId( String s ) { surrenderEventId = s; }
    public void setEscapeEventId( String s ) { escapeEventId = s; }
    public void setDestroyedEventId( String s ) { destroyedEventId = s; }
    public void setDeadCrewEventId( String s ) { deadCrewEventId = s; }
    public void setGotAwayEventId( String s ) { gotAwayEventId = s; }

    public String getSurrenderEventId() { return surrenderEventId; }
    public String getEscapeEventId() { return escapeEventId; }
    public String getDestroyedEventId() { return destroyedEventId; }
    public String getDeadCrewEventId() { return deadCrewEventId; }
    public String getGotAwayEventId() { return gotAwayEventId; }

    /**
     * Sets the id of the most recent (possibly current) event id.
     *
     * As secondary and tertiary events are triggered at a beacon, this
     * value will be replaced by their ids.
     *
     * Sometimes this is blank.
     *
     * Matthew's hint: There are two kinds of event: static events, assigned
     * randomly based on the sector seed and "sector_data.xml" (like for
     * nebula beacons); and dynamic events. This value only tracks dynamic
     * events.
     */
    public void setLastEventId( String s ) { lastEventId = s; }
    public String getLastEventId() { return lastEventId; }

    /**
     * Unknown.
     *
     * This was introduced in FTL 1.6.1.
     */
    public void setUnknownAlpha( int n ) { unknownAlpha = n; }
    public int getUnknownAlpha() { return unknownAlpha; }

    /**
     * Sets the last situation-describing text shown in an event window.
     *
     * Any event - 'static', secondary, or wait - may set this value. It
     * may have no relation to the last event id.
     *
     * Note: Wait events triggered in-game set this value. Toggling waiting
     * programmatically does NOT set this value. That must be done
     * manually.
     *
     * FTL 1.6.1 introduced XML "id" attributes on elements, which
     * referenced text elsewhere. This value may be one of those references
     * instead of the actual text.
     *
     * After the event popup is dismissed, this value lingers.
     *
     * This may include line breaks ("\n").
     *
     * @see SavedGameState#setWaiting(boolean)
     */
    public void setText( String s ) { text = s; }
    public String getText() { return text; }

    /**
     * Sets a seed used to randomly select crew.
     *
     * When saved mid-event, this allows FTL to reselect the same crew.
     *
     * When no random selection has been made, this is -1.
     */
    public void setAffectedCrewSeed( int n ) { affectedCrewSeed = n; }
    public int getAffectedCrewSeed() { return affectedCrewSeed; }

    /**
     * Sets a list of breadcrumbs for choices made during the last event.
     *
     * Each integer in the list corresponds to a prompt, and the Integer's
     * value is the Nth choice that was clicked. (0-based)
     *
     * TODO: 52 was observed in the list once!?
     *
     * The event will still be in-progress if there aren't enough
     * breadcrumbs to renavigate to the end of the event.
     *
     * The list typically ends with a 0, since events usually conclude with
     * a lone "continue" choice.
     *
     * Note: If waiting, this list will cause a wait event to be selected
     * from fuel-related event lists, instead of a normal event.
     *
     * @see SavedGameState#setWaiting(boolean)
     */
    public void setChoiceList( List<Integer> choiceList ) { this.choiceList = choiceList; }
    public List<Integer> getChoiceList() { return choiceList; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        result.append( String.format( "Ship Event Seed:    %3d\n", shipEventSeed ) );
        result.append( String.format( "Surrender Event:    %s\n", surrenderEventId ) );
        result.append( String.format( "Escape Event:       %s\n", escapeEventId ) );
        result.append( String.format( "Destroyed Event:    %s\n", destroyedEventId ) );
        result.append( String.format( "Dead Crew Event:    %s\n", deadCrewEventId ) );
        result.append( String.format( "Got Away Event:     %s\n", gotAwayEventId ) );

        result.append( "\n" );

        result.append( String.format( "Last Event:         %s\n", lastEventId ) );
        result.append( String.format( "Alpha?:             %3d\n", unknownAlpha ) );

        result.append( "\nText...\n" );
        result.append( String.format( "%s\n", text ) );
        result.append( "\n" );

        result.append( String.format( "Affected Crew Seed: %3d\n", affectedCrewSeed ) );

        result.append( "\nLast Event Choices...\n" );
        first = true;
        for ( Integer choiceInt : choiceList ) {
            if ( first ) { first = false; }
            else { result.append( "," ); }
            result.append( choiceInt );
        }
        result.append( "\n" );

        return result.toString();
    }
}
