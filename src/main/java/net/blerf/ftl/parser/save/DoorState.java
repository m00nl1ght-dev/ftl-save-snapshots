package net.blerf.ftl.parser.save;

public class DoorState {
    private boolean open = false;
    private boolean walkingThrough = false;

    private int currentMaxHealth = 0;
    private int health = 0;
    private int nominalHealth = 0;
    private int unknownDelta = 0;
    private int unknownEpsilon = 0;


    /**
     * Constructor.
     */
    public DoorState() {
    }

    /**
     * Copy constructor.
     */
    public DoorState(DoorState srcDoor ) {
        open = srcDoor.isOpen();
        walkingThrough = srcDoor.isWalkingThrough();
        currentMaxHealth = srcDoor.getCurrentMaxHealth();
        health = srcDoor.getHealth();
        nominalHealth = srcDoor.getNominalHealth();
        unknownDelta = srcDoor.getUnknownDelta();
        unknownEpsilon = srcDoor.getUnknownEpsilon();
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    public void commandeer() {
        setCurrentMaxHealth( getNominalHealth() );
        setHealth( getCurrentMaxHealth() );

        setUnknownDelta( 0 );    // TODO: Vet this default.
        setUnknownEpsilon( 0 );  // TODO: Vet this default.
    }

    public void setOpen( boolean b ) { open = b; }
    public void setWalkingThrough( boolean b ) { walkingThrough = b; }

    public boolean isOpen() { return open; }
    public boolean isWalkingThrough() { return walkingThrough; }


    /**
     * Sets current max door health.
     *
     * This is affected by situational modifiers like Crystal lockdown,
     * but it likely copies the nominal value at some point.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setCurrentMaxHealth( int n ) { currentMaxHealth = n; }
    public int getCurrentMaxHealth() { return currentMaxHealth; }

    /**
     * Sets the current door health.
     *
     * Starting at current max, this decreases as someone tries to break it
     * down.
     *
     * TODO: After combat in which a hacking drone boosts the door's health,
     * the current max returns to normal, but the actual health stays high
     * for some reason.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setHealth( int n ) { health = n; }
    public int getHealth() { return health; }

    /**
     * Sets nominal max door health.
     * This is the value to which the current max will eventually reset.
     *
     * Observed values:
     *   04 = Level 0 (un-upgraded or damaged Doors system).
     *   08 = Level 1 (???)
     *   12 = Level 2 (confirmed)
     *   16 = Level 3 (confirmed)
     *   20 = Level 4 (Level 3, plus manned; confirmed)
     *   18 = Level 3 (max, plus manned) (or is it 15, 10 while unmanned?)
     *   50 = Lockdown.
     *
     * TODO: The Mantis Basilisk ship's doors went from 4 to 12 when the
     * 1-capacity Doors system was manned. Doors that were already hacked at
     * the time stayed at 16.
     *
     * TODO: Check what the Rock B Ship's doors have (it lacks a Doors
     * system). Damaged system is 4 (hacked doors were still 16).
     *
     * TODO: Investigate why an attached hacking drone adds to ALL THREE
     * healths (set on contact). Observed diffs: 4 to 16.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setNominalHealth( int n ) { nominalHealth = n; }
    public int getNominalHealth() { return nominalHealth; }

    /**
     * Unknown.
     *
     * Observed values: 0 (normal), 1 (while level 2 Doors system is
     * unmanned), 1 (while level 1 Doors system is manned), 2 (while level 3
     * Doors system is unmanned), 3 (while level 3 Doors system is manned),
     * 2 (hacking pod passively attached, set on
     * contact). Still 2 while hack-disrupting.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownDelta( int n ) { unknownDelta = n; }
    public int getUnknownDelta() { return unknownDelta; }

    /**
     * Sets hacking drone lockdown status.
     *
     * Observed values:
     *   0 = N/A
     *   1 = Hacking drone pod passively attached.
     *   2 = Hacking drone pod attached and disrupting.
     *
     * A hacking system launches a drone pod that will latch onto a target
     * system room, granting visibility. While the pod is attached and there
     * is power to the hacking system, the doors of the room turn purple,
     * locked to the crew of the targeted ship, but passable to the hacker's
     * crew.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setUnknownEpsilon( int n ) { unknownEpsilon = n; }
    public int getUnknownEpsilon() { return unknownEpsilon; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Open: %-5b, Walking Through: %-5b\n", open, walkingThrough ) );
        result.append( String.format( "Full HP: %3d, Current HP: %3d, Nominal HP: %3d, Delta?: %3d, Epsilon?: %3d\n", currentMaxHealth, health, nominalHealth, unknownDelta, unknownEpsilon ) );

        return result.toString();
    }
}
