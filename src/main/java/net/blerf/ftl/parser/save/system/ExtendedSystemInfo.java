package net.blerf.ftl.parser.save.system;

public abstract class ExtendedSystemInfo {

    protected ExtendedSystemInfo() {
    }

    protected ExtendedSystemInfo(ExtendedSystemInfo srcInfo ) {
    }

    /**
     * Blindly copy-constructs objects.
     *
     * Subclasses override this with return values of their own type.
     */
    public abstract ExtendedSystemInfo copy();

    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    public abstract void commandeer();
}
