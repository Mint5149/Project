package listing;

import java.util.Date; 
import item.Item;
import user.User;

public class Listing {
    // Attributes
    private int listingId;
    private Item item;
    private double price;
    private boolean isAvailable;
    private Date listingDate; 

    // Constructor
    public Listing(int listingId, Item item, double price) {
        this(listingId, item, price, new Date());
    }

    public Listing(int listingId, Item item, double price, Date date) {
        this.listingId = listingId;
        this.item = item;
        this.price = price;
        this.isAvailable = true;
        this.listingDate = date; 
    }

    // Methods
    public double getPrice() {
        return price;
    }

    public String getItemDetails() {
        return item.getTitle() + " (Price: " + price + ")";
    }

    public void markAsSold() {
        this.isAvailable = false;
    }

    // Getters
    public int getListingId() { return listingId; }
    public Item getItem() { return item; }
    public boolean isAvailable() { return isAvailable; }
    public User getOwner() { return item.getOwner(); }
    public Date getListingDate() { return listingDate; }
}