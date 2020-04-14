package net.blerf.ftl.parser.save;

import java.util.ArrayList;
import java.util.List;

public class StoreShelf {
    private SavedGameParser.StoreItemType itemType = SavedGameParser.StoreItemType.WEAPON;
    private List<StoreItem> items = new ArrayList<>(3);  // TODO: Magic number.


    /**
     * Constructor.
     *
     * Up to 3 StoreItems may to be added (Set the StoreItemType, too.)
     * Fewer StoreItems mean empty space on the shelf.
     */
    public StoreShelf() {
    }

    /**
     * Copy constructor.
     *
     * Each StoreItem will be copy-constructed as well.
     */
    public StoreShelf(StoreShelf srcShelf ) {
        itemType = srcShelf.getItemType();

        for ( StoreItem tmpItem : srcShelf.getItems() ) {
            addItem( new StoreItem( tmpItem ) );
        }
    }

    public void setItemType( SavedGameParser.StoreItemType type ) { itemType = type; }
    public SavedGameParser.StoreItemType getItemType() { return itemType; }

    public void addItem( StoreItem item ) {
        items.add( item );
    }
    public List<StoreItem> getItems() { return items; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append( String.format( "Item Type: %s\n", itemType ) );
        for ( StoreItem item : items ) {
            if ( first ) { first = false; }
            else { result.append( ",\n" ); }
            result.append( item.toString().replaceAll( "(^|\n)(.+)", "$1  $2" ) );
        }

        return result.toString();
    }
}
