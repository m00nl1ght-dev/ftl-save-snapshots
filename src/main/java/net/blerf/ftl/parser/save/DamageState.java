package net.blerf.ftl.parser.save;

public class DamageState {
    private int hullDamage = 0;
    private int shieldPiercing = 0;
    private int fireChance = 0;
    private int breachChance = 0;
    private int ionDamage = 0;
    private int systemDamage = 0;
    private int personnelDamage = 0;
    private boolean hullBuster = false;
    private int ownerId = -1;
    private int selfId = -1;
    private boolean lockdown = false;
    private boolean crystalShard = false;
    private int stunChance = 0;
    private int stunAmount = 0;


    /**
     * Constructor.
     */
    public DamageState() {
    }

    /**
     * Copy constructor.
     */
    public DamageState(DamageState srcDamage ) {
        hullDamage = srcDamage.getHullDamage();
        shieldPiercing = srcDamage.getShieldPiercing();
        fireChance = srcDamage.getFireChance();
        breachChance = srcDamage.getBreachChance();
        ionDamage = srcDamage.getIonDamage();
        systemDamage = srcDamage.getSystemDamage();
        personnelDamage = srcDamage.getPersonnelDamage();
        boolean hullBuster = srcDamage.isHullBuster();
        ownerId = srcDamage.getOwnerId();
        selfId = srcDamage.getSelfId();
        lockdown = srcDamage.isLockdown();
        crystalShard = srcDamage.isCrystalShard();
        stunChance = srcDamage.getStunChance();
        stunAmount = srcDamage.getStunAmount();
    }

    public void setHullDamage( int n ) { hullDamage = n; }
    public void setShieldPiercing( int n ) { shieldPiercing = n; }
    public void setFireChance( int n ) { fireChance = n; }
    public void setBreachChance( int n ) { breachChance = n; }
    public void setIonDamage( int n ) { ionDamage = n; }
    public void setSystemDamage( int n ) { systemDamage = n; }

    public int getHullDamage() { return hullDamage; }
    public int getShieldPiercing() { return shieldPiercing; }
    public int getFireChance() { return fireChance; }
    public int getBreachChance() { return breachChance; }
    public int getIonDamage() { return ionDamage; }
    public int getSystemDamage() { return systemDamage; }

    /**
     * Sets damage to apply to personnel.
     *
     * This is dealt per-square to each crew in the room hit. A Beam weapon
     * can injure someone twice if it follows them into another room.
     */
    public void setPersonnelDamage( int n ) { personnelDamage = n; }
    public int getPersonnelDamage() { return personnelDamage; }

    /**
     * Toggles whether this projectile deals double hull damage against
     * systemless rooms.
     *
     * This is based on the 'hullBust' tag (0/1) of a WeaponBlueprint's xml.
     */
    public void setHullBuster( boolean b ) { hullBuster = b; }
    public boolean isHullBuster() { return hullBuster; }

    /**
     * Unknown.
     *
     * When not set, this is -1.
     *
     * This only seems to be set by projectiles from bomb weapons: 1 when
     * from the nearby ship, once it materializes (-1 a moment before).
     */
    public void setOwnerId( int n ) { ownerId = n; }
    public int getOwnerId() { return ownerId; }

    /**
     * Unknown.
     *
     * When not set, this is -1.
     */
    public void setSelfId( int n ) { selfId = n; }
    public int getSelfId() { return selfId; }

    public void setLockdown( boolean b ) { lockdown = b; }
    public void setCrystalShard( boolean b ) { crystalShard = b; }
    public void setStunChance( int n ) { stunChance = n; }
    public void setStunAmount( int n ) { stunAmount = n; }

    public boolean isLockdown() { return lockdown; }
    public boolean isCrystalShard() { return crystalShard; }
    public int getStunChance() { return stunChance; }
    public int getStunAmount() { return stunAmount; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Hull Damage:       %7d\n", hullDamage ) );
        result.append( String.format( "ShieldPiercing:    %7d\n", shieldPiercing ) );
        result.append( String.format( "Fire Chance:       %7d\n", fireChance ) );
        result.append( String.format( "Breach Chance:     %7d\n", breachChance ) );
        result.append( String.format( "Ion Damage:        %7d\n", ionDamage ) );
        result.append( String.format( "System Damage:     %7d\n", systemDamage ) );
        result.append( String.format( "Personnel Damage:  %7d\n", personnelDamage ) );
        result.append( String.format( "Hull Buster:       %7b (2x Hull damage vs systemless rooms)\n", hullBuster ) );
        result.append( String.format( "Owner Id?:         %7d\n", ownerId ) );
        result.append( String.format( "Self Id?:          %7d\n", selfId ) );
        result.append( String.format( "Lockdown:          %7b\n", lockdown ) );
        result.append( String.format( "Crystal Shard:     %7b\n", crystalShard ) );
        result.append( String.format( "Stun Chance:       %7d\n", stunChance ) );
        result.append( String.format( "Stun Amount:       %7d\n", stunAmount ) );

        return result.toString();
    }
}
