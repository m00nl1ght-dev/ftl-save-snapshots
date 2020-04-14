package net.blerf.ftl.parser.save.system;

import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.xml.WeaponBlueprint;

public class WeaponState {
    private String weaponId = null;
    private boolean armed = false;
    private int cooldownTicks = 0;
    private WeaponModuleState weaponMod = null;


    /**
     * Constructs an incomplete WeaponState.
     *
     * It will need a weaponId.
     *
     * For FTL 1.5.4+ saved games, a weapon module will be needed.
     */
    public WeaponState() {
    }

    /**
     * Copy constructor.
     *
     * The weapon module will be copy-constructed as well.
     */
    public WeaponState(WeaponState srcWeapon ) {
        weaponId = srcWeapon.getWeaponId();
        armed = srcWeapon.isArmed();
        cooldownTicks = srcWeapon.getCooldownTicks();

        if ( srcWeapon.getWeaponModule() != null ) {
            weaponMod = new WeaponModuleState( srcWeapon.getWeaponModule() );
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    public void commandeer() {
        setArmed( false );
        setCooldownTicks( 0 );

        if ( getWeaponModule() != null ) {
            getWeaponModule().commandeer();
        }
    }


    public void setWeaponId( String s ) { weaponId = s; }
    public String getWeaponId() { return weaponId; }

    public void setArmed( boolean b ) {
        armed = b;
        if ( b == false ) cooldownTicks = 0;
    }
    public boolean isArmed() { return armed; }

    /**
     * Sets time elapsed waiting for the weapon to cool down.
     *
     * This increments from 0, by 1 each second. Its goal is the value of
     * the 'coolown' tag in its WeaponBlueprint xml (0 when not armed).
     *
     * Since FTL 1.5.4, this is no longer saved.
     *
     * @see WeaponModuleState.setCooldownTicks(int)
     */
    public void setCooldownTicks( int n ) { cooldownTicks = n; }
    public int getCooldownTicks() { return cooldownTicks; }

    /**
     * Sets additional weapon fields.
     *
     * Advanced Edition added extra weapon fields at the end of saved game
     * files. They're nested inside this class for convenience.
     *
     * This was introduced in FTL 1.5.4.
     */
    public void setWeaponModule( WeaponModuleState weaponMod ) { this.weaponMod = weaponMod; }
    public WeaponModuleState getWeaponModule() { return weaponMod; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        WeaponBlueprint weaponBlueprint = DataManager.get().getWeapon( weaponId );
        String cooldownString = ( weaponBlueprint != null ? weaponBlueprint.getCooldown()+"" : "?" );

        result.append( String.format( "WeaponId:       %s\n", weaponId ) );
        result.append( String.format( "Armed:          %b\n", armed ) );
        result.append( String.format( "Cooldown Ticks: %2d (max: %2s) (Not used as of FTL 1.5.4)\n", cooldownTicks, cooldownString ) );

        result.append( "\nWeapon Module...\n" );
        if ( weaponMod != null ) {
            result.append( weaponMod.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        return result.toString();
    }
}
