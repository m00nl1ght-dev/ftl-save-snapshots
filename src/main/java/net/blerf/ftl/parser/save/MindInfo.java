package net.blerf.ftl.parser.save;

import net.blerf.ftl.parser.save.system.ExtendedSystemInfo;

public class MindInfo extends ExtendedSystemInfo {
    private int mindControlTicksGoal = 0;
    private int mindControlTicks = 0;


    /**
     * Constructor.
     */
    public MindInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected MindInfo(MindInfo srcInfo ) {
        super( srcInfo );
        mindControlTicksGoal = srcInfo.getMindControlTicksGoal();
        mindControlTicks = srcInfo.getMindControlTicks();
    }

    @Override
    public MindInfo copy() { return new MindInfo( this ); }

    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     */
    @Override
    public void commandeer() {
        setMindControlTicks( 0 );
        setMindControlTicksGoal( 0 );
    }

    /**
     * Sets elapsed time while crew are mind controlled.
     *
     * After reaching or passing the goal, this value lingers.
     *
     * When the goal is reached, the Mind system will get 4 ionized bars
     * (ionized bars had been -1 while disrupting).
     *
     * @param n a positive int less than, or equal to, the goal
     *
     * @see #setMindControlTicksGoal(int)
     */
    public void setMindControlTicks( int n ) { mindControlTicks = n; }
    public int getMindControlTicks() { return mindControlTicks; }

    /**
     * Sets total time crew will stay mind controlled.
     *
     * This can vary depending on the system level when mind control is
     * initially engaged. When not engaged, this value lingers.
     *
     * @see #setMindControlTicks(int)
     */
    public void setMindControlTicksGoal( int n ) { mindControlTicksGoal = n; }
    public int getMindControlTicksGoal() { return mindControlTicksGoal; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "SystemId:                 %s\n", SavedGameParser.SystemType.MIND.getId() ) );
        result.append( String.format( "Mind Ctrl Ticks:        %7d\n", mindControlTicks ) );
        result.append( String.format( "Mind Ctrl Ticks Goal:   %7d\n", mindControlTicksGoal ) );

        return result.toString();
    }
}
