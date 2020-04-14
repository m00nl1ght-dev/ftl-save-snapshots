package net.blerf.ftl.parser.save.system;

import net.blerf.ftl.parser.save.SavedGameParser;

public class ArtilleryInfo extends ExtendedSystemInfo {
    private WeaponModuleState weaponMod = null;


    /**
     * Constructor.
     *
     * It will need a WeaponModuleState.
     */
    public ArtilleryInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ArtilleryInfo(ArtilleryInfo srcInfo ) {
        super( srcInfo );
        weaponMod = new WeaponModuleState( srcInfo.getWeaponModule() );
    }

    @Override
    public ArtilleryInfo copy() { return new ArtilleryInfo( this ); }

    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    @Override
    public void commandeer() {
        if ( getWeaponModule() != null ) {
            getWeaponModule().commandeer();
        }
    }

    public void setWeaponModule( WeaponModuleState weaponMod ) { this.weaponMod = weaponMod; }
    public WeaponModuleState getWeaponModule() { return weaponMod; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "SystemId:                 %s\n", SavedGameParser.SystemType.ARTILLERY.getId() ) );

        result.append( "\nWeapon Module...\n" );
        if ( weaponMod != null ) {
            result.append( weaponMod.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        return result.toString();
    }
}
