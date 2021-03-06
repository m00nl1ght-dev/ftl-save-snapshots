package net.blerf.ftl.parser.save.system;

import net.blerf.ftl.parser.save.SavedGameParser;

public class SystemState {
    private SavedGameParser.SystemType systemType;
    private int capacity = 0;
    private int power = 0;
    private int damagedBars = 0;
    private int ionizedBars = 0;
    private int repairProgress = 0;
    private int damageProgress = 0;
    private int deionizationTicks = Integer.MIN_VALUE;

    private int batteryPower = 0;
    private int hackLevel = 0;
    private boolean hacked = false;
    private int temporaryCapacityCap = 1000;
    private int temporaryCapacityLoss = 0;
    private int temporaryCapacityDivisor = 1;


    /**
     * Constructor.
     */
    public SystemState(SavedGameParser.SystemType systemType ) {
        this.systemType = systemType;
    }

    /**
     * Copy constructor.
     */
    public SystemState(SystemState srcSystem ) {
        systemType = srcSystem.getSystemType();
        capacity = srcSystem.getCapacity();
        power = srcSystem.getPower();
        damagedBars = srcSystem.getDamagedBars();
        ionizedBars = srcSystem.getIonizedBars();
        repairProgress = srcSystem.getRepairProgress();
        damageProgress = srcSystem.getDamageProgress();
        deionizationTicks = srcSystem.getDeionizationTicks();
        batteryPower = srcSystem.getBatteryPower();
        hackLevel = srcSystem.getHackLevel();
        hacked = srcSystem.isHacked();
        temporaryCapacityCap = srcSystem.getTemporaryCapacityCap();
        temporaryCapacityLoss = srcSystem.getTemporaryCapacityLoss();
        temporaryCapacityDivisor = srcSystem.getTemporaryCapacityDivisor();
    }

    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    public void commandeer() {
        if ( !systemType.isSubsystem() ) {
            setPower( 0 );
        }
        // TODO: Find out if NOT resetting subsystem power is okay.
        // Otherwise, damage, etc. will need to be taken into account.

        setBatteryPower( 0 );
        setHackLevel( 0 );
        setHacked( false );
        setTemporaryCapacityCap( 1000 );
        setTemporaryCapacityLoss( 0 );
        setTemporaryCapacityDivisor( 1 );
    }

    public SavedGameParser.SystemType getSystemType() { return systemType; }

    /**
     * Sets the number of power bars this system can use.
     *
     * A capacity of zero means the system is not currently installed.
     */
    public void setCapacity( int n ) { capacity = n; }
    public int getCapacity() { return capacity; }

    /**
     * Sets the number of reserve power bars assigned to this system.
     *
     * Power bars appear at the bottom of the stack.
     *
     * Note: For Weapons and DroneCtrl systems, this value ideally would
     * be set whenever a weapon/drone is armed/disarmed and vice versa.
     *
     * FTL seems to recalculate Weapons system power upon loading, so this
     * value can be sloppily set to 0 or ignored while editing.
     *
     * @see #setBatteryPower(int)
     */
    public void setPower( int n ) { power = n; }
    public int getPower() { return power; }

    /**
     * Sets the number of unusable power bars, in need of repair.
     *
     * Damaged bars appear at the top of the stack, painted over capacity
     * limit bars.
     */
    public void setDamagedBars( int n ) { damagedBars = n; }
    public int getDamagedBars() { return damagedBars; }

    /**
     * Sets the number of ionized power bars.
     *
     * In-game when N ion damage is applied, up to N power/battery bars are
     * deallocated. Any others remain to power the system, but for a short
     * time, transfers to/from that system will not be possible.
     *
     * A countdown will repeat N times, decrementing this value to 0. N may
     * exceed the total number of power bars on a system, to increase the
     * total time required to remove all the 'ionized bars'.
     *
     * This should be less than or equal to MAX_IONIZED_BARS, or FTL's
     * interface will be unable to find an image to display the number, and
     * a warning graphic will appear.
     *
     * When a system disables itself (white lock), this will be -1. For
     * the Cloaking system in FTL 1.01-1.03.3, setting this to -1 would
     * engage the cloak. Systems which do not normally disable themselves
     * will remain locked until they get hit with a weapon that produces
     * ion damage. See ExtendedSystemInfo classes for timer fields that
     * might used to unlock systems on their own.
     *
     * TODO: Teleporter has not been tested. AE systems have not been
     * tested.
     *
     * @see net.blerf.ftl.constants.FTLConstants#getMaxIonizedBars()
     */
    public void setIonizedBars( int n ) { ionizedBars = n; }
    public int getIonizedBars() { return ionizedBars; }

    /**
     * Sets progress toward repairing one damaged power bar.
     *
     * A growing portion of the bottommost damaged bar will turn yellow.
     *
     * Note: Repair progress and damage progress can both be non-zero at the
     * same time. They affect different bars.
     *
     * @param n 0-100 (0 when not repairing)
     */
    public void setRepairProgress( int n ) { repairProgress = n; }
    public int getRepairProgress() { return repairProgress; }

    /**
     * Sets progress toward damaging one power bar.
     *
     * A growing portion of the topmost empty/energy/battery/power bar will
     * turn red.
     *
     * This is typically caused by fire or boarders attempting sabotage.
     *
     * Note: Repair progress and damage progress can both be non-zero at the
     * same time. They affect different bars.
     *
     * @param n 0-100 (0 when not damaging)
     */
    public void setDamageProgress( int n ) { damageProgress = n; }
    public int getDamageProgress() { return damageProgress; }

    /**
     * Sets elapsed time while waiting to remove each ionized power bar.
     *
     * The system is inoperative while any ionized bars remain, and any
     * power assigned will be unavailable. If this system is using battery
     * power, and the battery deactivates, a lock countdown will complete
     * immediately (but not a plain ion countdown).
     *
     * The game's interface responds as this increments, including
     * resetting after intervals. If not needed, it may be 0, or
     * more often, MIN_INT.
     *
     * It was thought that in FTL 1.01-1.03.3, deionization of each bar
     * counted to 5000. In FTL 1.5.13, it was observed at 14407 (with half
     * the circle remaining).
     *
     * TODO:
     * Nearly every system has been observed with non-zero values,
     * but aside from Teleporter/Cloaking, normal use doesn't reliably
     * set such values. Might be unspecified garbage when not actively
     * counting. Sometimes has huge positive and negative values.
     *
     * This value is reset upon loading.
     * (TODO: Check if still true in FTL 1.5.4.)
     *
     * @see #setIonizedBars(int)
     */
    public void setDeionizationTicks( int n ) { deionizationTicks = n; }
    public int getDeionizationTicks() { return deionizationTicks; }


    /**
     * Sets the number of battery power bars assigned to this system.
     *
     * Battery bars have an orange border and will appear above reserve
     * power bars in the stack. When the battery system is fully discharged,
     * they will be lost, but spare reserve power at that moment will be
     * allocated to replace them.
     *
     * Note: For Weapons and DroneCtrl systems, this value must be set
     * whenever a weapon/drone is armed/disarmed and vice versa.
     *
     * Note: Whenever this value changes, the ship's Battery extended system
     * info must be updated.
     *
     * This was introduced in FTL 1.5.4.
     *
     * @see #setPower(int)
     * @see BatteryInfo#setUsedBattery(int)
     */
    public void setBatteryPower( int n ) { batteryPower = n; }
    public int getBatteryPower() { return batteryPower; }

    /**
     * Unknown.
     *
     * Observed values: 0 (no hacking drone pod), 1 (pod passively
     * attached, set on contact), 2 (disrupting).
     *
     * If the hacking system of the other ship is inoperative, this will be
     * set to 0, even though there is still a pod attached.
     *
     * TODO: Revise this description.
     *
     * This was introduced in FTL 1.5.4.
     *
     * @see #setHacked(int)
     */
    public void setHackLevel( int n ) { hackLevel = n; }
    public int getHackLevel() { return hackLevel; }

    /**
     * Toggles whether this system has a hacking drone pod attached.
     *
     * This only describes attachment (set the moment the pod makes
     * contact), not disruption.
     *
     * If the hacking system of the other ship is inoperative, this will be
     * set to false, even though there is still a pod attached.
     *
     * This was introduced in FTL 1.5.4.
     *
     * @see #setHackLevel(int)
     */
    public void setHacked( boolean b ) { hacked = b; }
    public boolean isHacked() { return hacked; }

    /**
     * Sets an upper limit on this system's usable capacity.
     *
     * The effect lasts for the current beacon only, or until reset.
     *
     * In the game's xml resources, the cap value comes from a "status" tag
     * with the "limit=" attribute.
     *
     * Mods are reportedly only capable of using one flavor of capacity
     * limit, but in saved games, they're all set, and the most restrictive
     * one applies.
     *
     * Under normal circumstances, the cap is 1000.
     * At a beacon with a nebula, the Sensors system has cap of 0.
     *
     * This was introduced in FTL 1.5.4.
     *
     * @see #setTemporaryCapacityLoss(int)
     * @see #setTemporaryCapacityDivisor(int)
     */
    public void setTemporaryCapacityCap( int n ) { temporaryCapacityCap = n; }
    public int getTemporaryCapacityCap() { return temporaryCapacityCap; }

    /**
     * Sets a number to subtract from this system's usable capacity.
     *
     * The effect lasts for the current beacon only, or until reset.
     *
     * In the game's xml resources, the cap value comes from a "status" tag
     * with the "loss=" attribute.
     *
     * Mods are reportedly only capable of using one flavor of capacity
     * limit, but in saved games, they're all set, and the most restrictive
     * one applies.
     *
     * Under normal circumstances, the loss is 0.
     *
     * This was introduced in FTL 1.5.4.
     *
     * @see #setTemporaryCapacityCap(int)
     * @see #setTemporaryCapacityDivisor(int)
     */
    public void setTemporaryCapacityLoss( int n ) { temporaryCapacityLoss = n; }
    public int getTemporaryCapacityLoss() { return temporaryCapacityLoss; }

    /**
     * Sets a number to divide this system's usable capacity by.
     *
     * The real capacity will be rounded up to the nearest multiple of N
     * before dividing.
     *
     * The effect lasts for the current beacon only, or until reset.
     *
     * In the game's xml resources, the cap value comes from a "status" tag
     * with the "divide=" attribute.
     *
     * Mods are reportedly only capable of using one flavor of capacity
     * limit, but in saved games, they're all set, and the most restrictive
     * one applies.
     *
     * Under normal circumstances, the divisor is 1.
     *
     * This was introduced in FTL 1.5.4.
     *
     * @see #setTemporaryCapacityCap(int)
     * @see #setTemporaryCapacityLoss(int)
     */
    public void setTemporaryCapacityDivisor( int n ) { temporaryCapacityDivisor = n; }
    public int getTemporaryCapacityDivisor() { return temporaryCapacityDivisor; }


    /**
     * Returns the effective capacity after applying limits (min 0).
     *
     * Damage is not considered.
     *
     * @see #getUsableCapacity()
     */
    public int getLimitedCapacity() {
        int capLimit = temporaryCapacityCap;
        int lossLimit = capacity - temporaryCapacityLoss;
        int divLimit = (capacity + temporaryCapacityDivisor-1) / temporaryCapacityDivisor;

        int limit = Math.max( 0, Math.min( capLimit, Math.min( lossLimit, divLimit ) ) );
        return limit;
    }

    /**
     * Returns the effective capacity after applying limits and damage
     * (min 0).
     *
     * The result is the maximum total power, battery, or zoltan bars.
     *
     * @see #getLimitedCapacity()
     */
    public int getUsableCapacity() {
        int limitedCapacity = getLimitedCapacity();
        int damagedCapacity = capacity - damagedBars;
        return Math.max( 0, Math.min( limitedCapacity, damagedCapacity ) );
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "SystemId:              %s\n", systemType.getId() ) );
        if (capacity > 0) {
            result.append( String.format( "Capacity:                %3d\n", capacity ) );
            result.append( String.format( "Power:                   %3d\n", power ) );
            result.append( String.format( "Damaged Bars:            %3d\n", damagedBars ) );
            result.append( String.format( "Ionized Bars:            %3d\n", ionizedBars ) );
            result.append( String.format( "Repair Progress:         %3d%%\n", repairProgress ) );
            result.append( String.format( "Damage Progress:         %3d%%\n", damageProgress ) );
            result.append( String.format( "Deionization Ticks:    %5s\n", (deionizationTicks == Integer.MIN_VALUE ? "N/A" : deionizationTicks) ) );
            result.append( String.format( "Battery Power:           %3d\n", batteryPower ) );
            result.append( String.format( "Hack Level:              %3d\n", hackLevel ) );
            result.append( String.format( "Hacked:                %5b\n", hacked ) );
            result.append( String.format( "Temp Capacity Cap:     %5d\n", temporaryCapacityCap ) );
            result.append( String.format( "Temp Capacity Loss:      %3d\n", temporaryCapacityLoss ) );
            result.append( String.format( "Temp Capacity Divisor:   %3d\n", temporaryCapacityDivisor ) );
        }
        else {
            result.append( String.format( "(Not installed)\n" ) );
        }

        return result.toString();
    }
}
