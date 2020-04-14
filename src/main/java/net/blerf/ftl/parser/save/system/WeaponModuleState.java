package net.blerf.ftl.parser.save.system;

import net.blerf.ftl.model.XYPair;
import net.blerf.ftl.parser.save.AnimState;
import net.blerf.ftl.parser.save.projectile.ProjectileState;

import java.util.ArrayList;
import java.util.List;

public class WeaponModuleState {
    private int cooldownTicks = 0;
    private int cooldownTicksGoal = 0;
    private int subcooldownTicks = 0;
    private int subcooldownTicksGoal = 0;
    private int boost = 0;
    private int charge = 0;

    private List<XYPair> currentTargets = new ArrayList<>();
    private List<XYPair> prevTargets = new ArrayList<>();

    private boolean autofire = false;
    private boolean fireWhenReady = false;
    private int targetId = -1;
    private AnimState weaponAnim = new AnimState();
    private int protractAnimTicks = 0;
    private boolean firing = false;
    private boolean unknownPhi = false;
    private int animCharge = -1;
    private AnimState chargeAnim = new AnimState();
    private int lastProjectileId = -1;

    private List<ProjectileState> pendingProjectiles = new ArrayList<>();


    /**
     * Constructor.
     */
    public WeaponModuleState() {
    }

    /**
     * Copy constructor.
     *
     * Each target and projectile will be copy-constructed as well.
     */
    public WeaponModuleState(WeaponModuleState srcMod ) {
        cooldownTicks = srcMod.getCooldownTicks();
        cooldownTicksGoal = srcMod.getCooldownTicksGoal();
        subcooldownTicks = srcMod.getSubcooldownTicks();
        subcooldownTicksGoal = srcMod.getSubcooldownTicksGoal();
        boost = srcMod.getBoost();
        charge = srcMod.getCharge();

        for ( XYPair target : srcMod.getCurrentTargets() ) {
            currentTargets.add( new XYPair( target ) );
        }
        for ( XYPair target : srcMod.getPreviousTargets() ) {
            prevTargets.add( new XYPair( target ) );
        }

        autofire = srcMod.getAutofire();
        fireWhenReady = srcMod.getFireWhenReady();
        targetId = srcMod.getTargetId();
        weaponAnim = srcMod.getWeaponAnim();
        protractAnimTicks = srcMod.getProtractAnimTicks();
        firing = srcMod.isFiring();
        unknownPhi = srcMod.getUnknownPhi();
        animCharge = srcMod.getAnimCharge();
        chargeAnim = srcMod.getChargeAnim();
        lastProjectileId = srcMod.getLastProjectileId();

        for ( ProjectileState projectile : srcMod.getPendingProjectiles() ) {
            pendingProjectiles.add( new ProjectileState( projectile ) );
        }
    }


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public void commandeer() {
        setCooldownTicks( 0 );
        setCooldownTicksGoal( 0 );     // TODO: Vet this default.
        setSubcooldownTicks( 0 );      // TODO: Vet this default.
        setSubcooldownTicksGoal( 0 );  // TODO: Vet this default.
        setBoost( 0 );
        setCharge( 0 );

        getCurrentTargets().clear();
        getPreviousTargets().clear();

        setAutofire( false );
        setFireWhenReady( false );
        setTargetId( -1 );

        getWeaponAnim().setPlaying( false );
        getWeaponAnim().setCurrentFrame( 0 );
        getWeaponAnim().setProgressTicks( 0 );

        setProtractAnimTicks( 0 );
        setFiring( false );

        setAnimCharge( -1 );
        setUnknownPhi( false );

        getChargeAnim().setPlaying( false );
        getChargeAnim().setCurrentFrame( 0 );
        getChargeAnim().setProgressTicks( 0 );

        setLastProjectileId( -1 );

        getPendingProjectiles().clear();
    }


    /**
     * Sets time elapsed waiting for the weapon to cool down.
     *
     * @param n a positive int less than, or equal to, the goal (0 when not armed)
     *
     * @see #setCooldownTicksGoal(int)
     * @see WeaponState#setCooldownTicks(int)
     */
    public void setCooldownTicks( int n ) { cooldownTicks = n; }
    public int getCooldownTicks() { return cooldownTicks; }

    /**
     * Sets total time needed for the weapon to cool down.
     *
     * This can vary depending on weapon features and situational factors.
     *
     * @see #setCooldownTicks(int)
     */
    public void setCooldownTicksGoal( int n ) { cooldownTicksGoal = n; }
    public int getCooldownTicksGoal() { return cooldownTicksGoal; }

    public void setSubcooldownTicks( int n ) { subcooldownTicks = n; }
    public int getSubcooldownTicks() { return subcooldownTicks; }

    public void setSubcooldownTicksGoal( int n ) { subcooldownTicksGoal = n; }
    public int getSubcooldownTicksGoal() { return subcooldownTicksGoal; }

    /**
     * Sets the boost level on a weapon whose cooldown decreases with
     * consecutive shots.
     *
     * This is represented in-game on the HUD as "Name +X".
     * Example: LASER_CHAINGUN.
     *
     * @param number of consecutive shots, up to the blueprint's boost count limit, or 0
     */
    public void setBoost( int n ) { boost = n; }
    public int getBoost() { return boost; }

    /**
     * Sets the number of charges on a charge weapon, or 0.
     *
     * Charges increment when a weapon's cooldown is allowed to elapse
     * additional times without firing... up to a maximum count.
     *
     * Immediately before firing, this value is cached in another field for
     * animation purposes, and this resets to 0.
     *
     * This is represented in-game on the HUD as circles with dots.
     * Example: ION_CHARGEGUN.
     *
     * Note: Modded WeaponBlueprints with "chargeLevels" greater than 7
     * crash FTL 1.5.4+.
     *
     * Note: Modded WeaponBlueprints with both the beam "type" and
     * "chargeLevels" crash FTL.
     *
     * @see #setChargeAnim(AnimState)
     */
    public void setCharge( int n ) { charge = n; }
    public int getCharge() { return charge; }

    /**
     * Sets currently targeted locations.
     *
     * This is a list of coordinates relative to the top-left corner of the
     * enemy ship's floor layout. There will be a coordinate for every
     * projectile the weapon will fire, or pairs of start/end coords for a
     * beam weapon's line.
     *
     * The moment the player places a target reticle, this is populated.
     *
     * Immediately before firing, these coordinates are copied to become the
     * previous targets, and if autofire is off this list is cleared.
     *
     * Note: These are NOT pseudo-floats.
     *
     * TODO: Confirm autofire behavior.
     *
     * @see #setPreviousTargets(List)
     */
    public void setCurrentTargets( List<XYPair> targetList ) { currentTargets = targetList; }
    public List<XYPair> getCurrentTargets() { return currentTargets; }

    /**
     * Sets previously targeted locations.
     *
     * The moment the player places a target reticle, this is populated.
     *
     * Note: These are NOT pseudo-floats.
     *
     * @see #setCurrentTargets(List)
     */
    public void setPreviousTargets( List<XYPair> targetList ) { prevTargets = targetList; }
    public List<XYPair> getPreviousTargets() { return prevTargets; }

    /**
     * Toggles whether fireWhenReady will be disabled after any volley.
     *
     * TODO: Determine what other fields must be set along with this.
     *
     * @see #setFireWhenReady(boolean)
     */
    public void setAutofire( boolean b ) { autofire = b; }
    public boolean getAutofire() { return autofire; }

    /**
     * Toggles whether this weapon will fire its next volley once the
     * cooldown expires.
     *
     * The moment the player places a target reticle, this is set to true.
     *
     * Immediately before firing, if autofire is off, this is set to false.
     *
     * TODO: Determine what other fields must be set along with this.
     *
     * @see #setAutofire(boolean)
     */
    public void setFireWhenReady( boolean b ) { fireWhenReady = b; }
    public boolean getFireWhenReady() { return fireWhenReady; }

    /**
     * Unknown.
     *
     * When not set, this is -1.
     *
     * The moment the player places a target reticle, this is set.
     *
     * @param n player ship (0) or nearby ship (1)
     */
    public void setTargetId( int n ) { targetId = n; }
    public int getTargetId() { return targetId; }

    /**
     * Sets the weapon anim state, depicting idle/cooldown/fire.
     */
    public void setWeaponAnim( AnimState anim ) { weaponAnim = anim; }
    public AnimState getWeaponAnim() { return weaponAnim; }

    /**
     * Sets time elapsed while this weapon slides out from the hull.
     *
     * This counts from 0 (retracted) to 1000 (protracted) and back.
     * Upon pausing or saving, this will snap to whichever number it was
     * approaching at the time.
     *
     * TODO: Determine what happens when edited to a value somewhere in
     * between.
     */
    public void setProtractAnimTicks( int n ) { protractAnimTicks = n; }
    public int getProtractAnimTicks() { return protractAnimTicks; }

    /**
     * Toggles whether this weapon is currently firing.
     */
    public void setFiring( boolean b ) { firing = b; }
    public boolean isFiring() { return firing; }

    /**
     * Unknown.
     *
     * Matthew's hint: fireshot.
     */
    public void setUnknownPhi( boolean b ) { unknownPhi = b; }
    public boolean getUnknownPhi() { return unknownPhi; }

    /**
     * Sets the cached charge.
     *
     * Immediately before firing, FTL copies the main charge value minus 1
     * and caches it here for reference while playing the chargeAnim. After
     * firing, this is resynchronized to the main charge value minus 1.
     *
     * When not set, this is -1.
     *
     * TODO: Boost weapons set this too!?
     *
     * This was introduced in FTL 1.5.13.
     *
     * @see #setChargeAnim(AnimState)
     */
    public void setAnimCharge( int n ) { animCharge = n; }
    public int getAnimCharge() { return animCharge; }

    /**
     * Sets the charge anim state.
     *
     * This is overlaid onto the weaponAnim to add minor accents.
     * It's current frame depends on the cached charge.
     *
     * In "dlcAnimations.xml", if the weaponAnim's name is "X", the
     * boostAnim's name will be "X_charge". (Yes, really.)
     *
     * TODO: Sort out charge vs boost.
     *
     * This was introduced in FTL 1.5.13.
     *
     * @see #setAnimCharge(int)
     */
    public void setChargeAnim( AnimState anim ) { chargeAnim = anim; }
    public AnimState getChargeAnim() { return chargeAnim; }

    /**
     * Unknown.
     *
     * When not set, this is -1.
     */
    public void setLastProjectileId( int n ) { lastProjectileId = n; }
    public int getLastProjectileId() { return lastProjectileId; }


    /**
     * Sets a list of queued projectiles about to be fired.
     *
     * This is often seen with laser burst weapons mid-volley, but any
     * weapon will momentarily queue at least one projectile an instant
     * before firing.
     */
    public void setPendingProjectiles( List<ProjectileState> pendingProjectiles ) { this.pendingProjectiles = pendingProjectiles; }
    public List<ProjectileState> getPendingProjectiles() { return pendingProjectiles; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append( String.format( "Cooldown Ticks:          %7d\n", cooldownTicks ) );
        result.append( String.format( "Cooldown Goal:           %7d\n", cooldownTicksGoal ) );
        result.append( String.format( "Subcooldown Ticks?:      %7d\n", subcooldownTicks ) );
        result.append( String.format( "Subcooldown Ticks Goal?: %7d\n", subcooldownTicksGoal ) );
        result.append( String.format( "Boost:                   %7d\n", boost ) );
        result.append( String.format( "Charge:                  %7d\n", charge ) );

        result.append( "\nCurrent Targets?... (Reticle Coords)\n" );
        first = true;
        for ( XYPair target : currentTargets ) {
            if ( first ) { first = false; }
            else { result.append( ",\n" ); }
            result.append( String.format( "  X,Y: %3d,%3d\n", target.x, target.y ) );
        }

        result.append( "\nPrevious Targets?... (Reticle Coords)\n" );
        first = true;
        for ( XYPair target : prevTargets ) {
            if ( first ) { first = false; }
            else { result.append( ",\n" ); }
            result.append( String.format( "  X,Y: %3d,%3d\n", target.x, target.y ) );
        }

        result.append( "\n" );

        result.append( String.format( "Autofire:                %7b\n", autofire ) );
        result.append( String.format( "Fire When Ready?:        %7b\n", fireWhenReady ) );
        result.append( String.format( "Target Id?:              %7d\n", targetId ) );

        result.append( "\nWeapon Anim...\n" );
        if ( weaponAnim != null ) {
            result.append( weaponAnim.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        result.append( "\n" );

        result.append( String.format( "Protract Anim Ticks:     %7d (0=Retracted or 1000=Protracted)\n", protractAnimTicks ) );
        result.append( String.format( "Firing:                  %7b\n", firing ) );
        result.append( String.format( "Phi?:                    %7b\n", unknownPhi ) );
        result.append( String.format( "Anim Charge:             %7d (Caches charge while firing to use in chargeAnim)\n", animCharge ) );

        result.append( "\nCharge Anim?...\n" );
        if ( chargeAnim != null) {
            result.append( chargeAnim.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        result.append( "\n" );

        result.append( String.format( "Last Projectile Id?:     %7d\n", lastProjectileId ) );

        result.append( "\nPending Projectiles... (Queued before firing)\n" );
        int projectileIndex = 0;
        first = true;
        for ( ProjectileState projectile : pendingProjectiles ) {
            if ( first ) { first = false; }
            else { result.append( ",\n" ); }
            result.append( String.format( "Projectile # %2d:\n", projectileIndex++ ) );
            result.append( projectile.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        return result.toString();
    }
}
