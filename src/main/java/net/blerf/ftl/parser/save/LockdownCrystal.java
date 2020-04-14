package net.blerf.ftl.parser.save;

public class LockdownCrystal {
    private int currentPosX = 0, currentPosY = 0;
    private int speed = 0;
    private int goalPosX = 0, goalPosY = 0;
    private boolean arrived = false;
    private boolean done = false;
    private int lifetime = 0;
    private boolean superFreeze = false;
    private int lockingRoom = 0;
    private int animDirection = 0;
    private int shardProgress = 0;


    public LockdownCrystal() {
    }

    public void setCurrentPositionX( int n ) { currentPosX = n; }
    public void setCurrentPositionY( int n ) { currentPosY = n; }
    public void setSpeed( int n ) { speed = n; }
    public void setGoalPositionX( int n ) { goalPosX = n; }
    public void setGoalPositionY( int n ) { goalPosY = n; }
    public void setArrived( boolean b ) { arrived = b; }
    public void setDone( boolean b ) { done = b; }
    public void setLifetime( int n ) { lifetime = n; }
    public void setSuperFreeze( boolean b ) { superFreeze = b; }
    public void setLockingRoom( int n ) { lockingRoom = n; }
    public void setAnimDirection( int n ) { animDirection = n; }
    public void setShardProgress( int n ) { shardProgress = n; }

    public int getCurrentPositionX() { return currentPosX; }
    public int getCurrentPositionY() { return currentPosY; }
    public int getSpeed() { return speed; }
    public int getGoalPositionX() { return goalPosX; }
    public int getGoalPositionY() { return goalPosY; }
    public boolean hasArrived() { return arrived; }
    public boolean isDone() { return done; }
    public int getLifetime() { return lifetime; }
    public boolean isSuperFreeze() { return superFreeze; }
    public int getLockingRoom() { return lockingRoom; }
    public int getAnimDirection() { return animDirection; }
    public int getShardProgress() { return shardProgress; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Current Position:  %8d,%8d (%9.03f,%9.03f)\n", currentPosX, currentPosY, currentPosX/1000f, currentPosY/1000f ) );
        result.append( String.format( "Speed?:            %8d\n", speed ) );
        result.append( String.format( "Goal Position:     %8d,%8d (%9.03f,%9.03f)\n", goalPosX, goalPosY, goalPosX/1000f, goalPosY/1000f ) );
        result.append( String.format( "Arrived?:          %8b\n", arrived ) );
        result.append( String.format( "Done?:             %8b\n", done ) );
        result.append( String.format( "Lifetime?:         %8d\n", lifetime ) );
        result.append( String.format( "SuperFreeze?:      %8b\n", superFreeze ) );
        result.append( String.format( "Locking Room?:     %8d\n", lockingRoom ) );
        result.append( String.format( "Anim Direction?:   %8d\n", animDirection ) );
        result.append( String.format( "Shard Progress?:   %8d\n", shardProgress ) );

        return result.toString();
    }
}
