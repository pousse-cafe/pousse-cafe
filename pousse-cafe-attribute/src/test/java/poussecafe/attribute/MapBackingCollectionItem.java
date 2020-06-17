package poussecafe.attribute;

public class MapBackingCollectionItem {

    String key;

    String value;

    public MapBackingCollectionItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        MapBackingCollectionItem other = (MapBackingCollectionItem) obj;
        return key.equals(other.key)
                && value.equals(other.value);
    }
}