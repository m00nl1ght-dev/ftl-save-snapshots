package net.blerf.ftl.parser.save.system;

import net.blerf.ftl.parser.save.SavedGameParser;

public class ShieldsInfo extends ExtendedSystemInfo {
    private int shieldLayers = 0;
    private int energyShieldLayers = 0;
    private int energyShieldMax = 0;
    private int shieldRechargeTicks = 0;

    private boolean shieldDropAnimOn = false;
    private int shieldDropAnimTicks = 0;

    private boolean shieldRaiseAnimOn = false;
    private int shieldRaiseAnimTicks = 0;

    private boolean energyShieldAnimOn = false;
    private int energyShieldAnimTicks = 0;

    private int unknownLambda = 0;
    private int unknownMu = 0;


    public ShieldsInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ShieldsInfo(ShieldsInfo srcInfo ) {
        super( srcInfo );
        shieldLayers = srcInfo.getShieldLayers();
        energyShieldLayers = srcInfo.getEnergyShieldLayers();
        energyShieldMax = srcInfo.getEnergyShieldMax();
        shieldRechargeTicks = srcInfo.getShieldRechargeTicks();

        shieldDropAnimOn = srcInfo.isShieldDropAnimOn();
        shieldDropAnimTicks = srcInfo.getShieldDropAnimTicks();

        shieldRaiseAnimOn = srcInfo.isShieldRaiseAnimOn();
        shieldRaiseAnimTicks = srcInfo.getShieldRaiseAnimTicks();

        energyShieldAnimOn = srcInfo.isEnergyShieldAnimOn();
        energyShieldAnimTicks = srcInfo.getEnergyShieldAnimTicks();

        unknownLambda = srcInfo.getUnknownLambda();
        unknownMu = srcInfo.getUnknownMu();
    }

    @Override
    public ShieldsInfo copy() { return new ShieldsInfo( this ); }

    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    @Override
    public void commandeer() {
        setShieldLayers( 0 );
        setShieldRechargeTicks( 0 );
        setShieldDropAnimOn( false );
        setShieldDropAnimTicks( 0 );   // TODO: Vet this default.
        setShieldRaiseAnimOn( false );
        setShieldRaiseAnimTicks( 0 );  // TODO: Vet this default.
    }

    /**
     * Sets the current number of normal shield layers.
     *
     * This is indicated in-game by filled bubbles.
     */
    public void setShieldLayers( int n ) { shieldLayers = n; }
    public int getShieldLayers() { return shieldLayers; }

    /**
     * Sets the current number of energy shield layers.
     *
     * This is indicated in-game by green rectangles.
     */
    public void setEnergyShieldLayers( int n ) { energyShieldLayers = n; }
    public int getEnergyShieldLayers() { return energyShieldLayers; }

    /**
     * Sets the number of energy shield layers when fully charged.
     *
     * This is 0 until set by a mechanism that adds energy layers. This
     * value lingers after a temporary energy shield is exhausted.
     */
    public void setEnergyShieldMax( int n ) { energyShieldMax = n; }
    public int getEnergyShieldMax() { return energyShieldMax; }

    /**
     * Sets elapsed time while waiting for the next normal shield layer
     * to recharge.
     *
     * This counts to 2000. When not recharging, it is 0.
     */
    public void setShieldRechargeTicks( int n ) { shieldRechargeTicks = n; }
    public int getShieldRechargeTicks() { return shieldRechargeTicks; }


    /**
     * Toggles whether the regular shield drop animation is being played.
     *
     * Note: The drop and raise anims can both play simultaneously.
     */
    public void setShieldDropAnimOn( boolean b ) { shieldDropAnimOn = b; }
    public boolean isShieldDropAnimOn() { return shieldDropAnimOn; }

    /**
     * Sets elapsed time while playing the regular shield drop anim.
     *
     * @param n 0-1000
     */
    public void setShieldDropAnimTicks( int n ) { shieldDropAnimTicks = n; }
    public int getShieldDropAnimTicks() { return shieldDropAnimTicks; }


    /**
     * Toggles whether the regular shield raise animation is being played.
     *
     * Note: The drop and raise anims can both play simultaneously.
     */
    public void setShieldRaiseAnimOn( boolean b ) { shieldRaiseAnimOn = b; }
    public boolean isShieldRaiseAnimOn() { return shieldRaiseAnimOn; }

    /**
     * Sets elapsed time while playing the regular shield raise anim.
     *
     * @param n 0-1000
     */
    public void setShieldRaiseAnimTicks( int n ) { shieldRaiseAnimTicks = n; }
    public int getShieldRaiseAnimTicks() { return shieldRaiseAnimTicks; }


    /**
     * Toggles whether the energy shield animation is being played.
     */
    public void setEnergyShieldAnimOn( boolean b ) { energyShieldAnimOn = b; }
    public boolean isEnergyShieldAnimOn() { return energyShieldAnimOn; }

    /**
     * Sets elapsed time while playing the energy shield anim.
     *
     * @param n 0-1000
     */
    public void setEnergyShieldAnimTicks( int n ) { energyShieldAnimTicks = n; }
    public int getEnergyShieldAnimTicks() { return energyShieldAnimTicks; }


    public void setUnknownLambda( int n ) { unknownLambda = n; }
    public void setUnknownMu( int n ) { unknownMu = n; }

    public int getUnknownLambda() { return unknownLambda; }
    public int getUnknownMu() { return unknownMu; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "SystemId:                 %s\n", SavedGameParser.SystemType.SHIELDS.getId() ) );
        result.append( String.format( "Shield Layers:            %5d (Currently filled bubbles)\n", shieldLayers ) );
        result.append( String.format( "Energy Shield Layers:     %5d\n", energyShieldLayers ) );
        result.append( String.format( "Energy Shield Max:        %5d (Layers when fully charged)\n", energyShieldLayers ) );
        result.append( String.format( "Shield Recharge Ticks:    %5d\n", shieldRechargeTicks ) );
        result.append( "\n" );
        result.append( String.format( "Shield Drop Anim:   Play: %-5b, Ticks: %4d\n", shieldDropAnimOn, shieldDropAnimTicks ) );
        result.append( String.format( "Shield Raise Anim:  Play: %-5b, Ticks: %4d\n", shieldRaiseAnimOn, shieldRaiseAnimTicks ) );
        result.append( String.format( "Energy Shield Anim: Play: %-5b, Ticks: %4d\n", energyShieldAnimOn, energyShieldAnimTicks ) );
        result.append( String.format( "Lambda?, Mu?:           %7d,%7d (Some kind of coord?)\n", unknownLambda, unknownMu ) );

        return result.toString();
    }
}
