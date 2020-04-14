package net.blerf.ftl.parser.save.drone;

public abstract class ExtendedDronePodInfo {

    protected ExtendedDronePodInfo() {
    }

    protected ExtendedDronePodInfo(ExtendedDronePodInfo srcInfo ) {
    }

    /**
     * Blindly copy-constructs objects.
     *
     * Subclasses override this with return values of their own type.
     */
    public abstract ExtendedDronePodInfo copy();


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    public abstract void commandeer();
}
