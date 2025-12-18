package transaction;

import java.util.Date;
import user.User;
import listing.Listing;

public class Transaction {
    private String transactionId;
    private User buyer;
    private User seller;
    private Listing listing;
    private Date transactionDate;

    public Transaction(String transactionId, User buyer, User seller, Listing listing) {
        this(transactionId, buyer, seller, listing, new Date());
    }

    public Transaction(String transactionId, User buyer, User seller, Listing listing, Date date) {
        this.transactionId = transactionId;
        this.buyer = buyer;
        this.seller = seller;
        this.listing = listing;
        this.transactionDate = date;
    }

    // --- Getters (เพิ่มส่วนนี้) ---
    public String getTransactionId() { return transactionId; }
    public User getBuyer() { return buyer; }
    public User getSeller() { return seller; }
    public Listing getListing() { return listing; }
    public Date getTransactionDate() { return transactionDate; }
}