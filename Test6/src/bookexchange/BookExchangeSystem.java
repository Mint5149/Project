package bookexchange;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors; 
import user.User;
import listing.Listing;
import transaction.Transaction;
import book.Book;

public class BookExchangeSystem {
    private List<User> users;
    private List<Listing> listings;
    private List<Transaction> transactions; 
    private User currentUser; 

    private static final String USER_FILE = "users.txt";
    private static final String LISTING_FILE = "listings.txt";
    private static final String TRANSACTION_FILE = "transactions.txt";

    public BookExchangeSystem() {
        this.users = new ArrayList<>();
        this.listings = new ArrayList<>();
        this.transactions = new ArrayList<>(); 
        
        loadUsers(); 
        loadListings(); 
        loadTransactions();
    }

    // --- Persistence Methods ---
    private void loadUsers() {
        java.io.File file = new java.io.File(USER_FILE);
        if (!file.exists()) return;

        try (java.util.Scanner scanner = new java.util.Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    User u = new User(parts[0], parts[1], parts[2], parts[3]);
                    users.add(u);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(USER_FILE))) {
            for (User u : users) {
                writer.println(u.getUserId() + "," + u.getUsername() + "," + u.getPassword() + "," + u.getContactInfo());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadListings() {
        java.io.File file = new java.io.File(LISTING_FILE);
        if (!file.exists()) return;

        try (java.util.Scanner scanner = new java.util.Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String desc = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    String ownerId = parts[4];
                    String extra1 = parts[5]; 
                    String extra2 = parts[6]; 
                    String type = parts[7];   

                    User owner = null;
                    for (User u : users) {
                        if (u.getUserId().equals(ownerId)) {
                            owner = u;
                            break;
                        }
                    }

                    if (owner != null) {
                        item.Item itemObj;
                        if (type.equals("BOOK")) {
                            itemObj = new Book(title, desc, owner, extra1, extra2);
                        } else {
                            itemObj = new studymaterial.StudyMaterial(title, desc, owner, extra1, extra2);
                        }
                        Listing l;
                        if (parts.length >= 10) { // Check for timestamp (10 fields)
                             // Format: ... | type | timestamp | isAvailable
                             // Index 8 = timestamp, Index 9 = isAvailable
                             long timestamp = Long.parseLong(parts[8]);
                             l = new Listing(id, itemObj, price, new java.util.Date(timestamp));
                             if (parts[9].equalsIgnoreCase("FALSE")) {
                                 l.markAsSold();
                             }
                        } else {
                            // Fallback for old format (9 fields)
                             l = new Listing(id, itemObj, price);
                             if (parts.length >= 9 && parts[8].equalsIgnoreCase("FALSE")) {
                                 l.markAsSold();
                             }
                        }
                        listings.add(l);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveListings() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(LISTING_FILE))) {
            for (Listing l : listings) {
                item.Item item = l.getItem();
                String type = (item instanceof Book) ? "BOOK" : "MATERIAL";
                String extra1 = ""; 
                String extra2 = ""; 

                if (item instanceof Book) {
                    Book b = (Book) item;
                    extra1 = (b.getAuthor() != null) ? b.getAuthor() : "";
                    extra2 = (b.getCourseCode() != null) ? b.getCourseCode() : "";
                } else if (item instanceof studymaterial.StudyMaterial) {
                   studymaterial.StudyMaterial sm = (studymaterial.StudyMaterial) item;
                   extra1 = (sm.getSubject() != null) ? sm.getSubject() : "";
                   extra2 = (sm.getCourseCode() != null) ? sm.getCourseCode() : "";
                } 
                
                 writer.println(l.getListingId() + "|" + item.getTitle() + "|" + item.getDescription() + "|" + 
                               l.getPrice() + "|" + l.getOwner().getUserId() + "|" + 
                               extra1 + "|" + extra2 + "|" + type + "|" + l.getListingDate().getTime() + "|" + l.isAvailable());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextListingId() {
        int maxId = 0;
        for (Listing l : listings) {
            if (l.getListingId() > maxId) maxId = l.getListingId();
        }
        return maxId + 1;
    }

    public void recordTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        saveTransactions();
    }

    private void loadTransactions() {
        java.io.File file = new java.io.File(TRANSACTION_FILE);
        if (!file.exists()) return;

        try (java.util.Scanner scanner = new java.util.Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String trxId = parts[0];
                    String buyerId = parts[1];
                    String buyerName = parts[2];
                    String sellerId = parts[3];
                    int listingId = Integer.parseInt(parts[4]);
                    long timestamp = Long.parseLong(parts[5]);

                    User buyer = new User(buyerId, buyerName, "", ""); // Recreate guest/buyer user
                    User seller = null;
                    for (User u : users) {
                        if (u.getUserId().equals(sellerId)) {
                            seller = u;
                            break;
                        }
                    }

                    Listing listing = null;
                    for (Listing l : listings) {
                        if (l.getListingId() == listingId) {
                            listing = l;
                            break;
                        }
                    }

                    if (seller != null && listing != null) {
                        transactions.add(new Transaction(trxId, buyer, seller, listing, new java.util.Date(timestamp)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTransactions() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(TRANSACTION_FILE))) {
            for (Transaction t : transactions) {
                writer.println(t.getTransactionId() + "|" + 
                               t.getBuyer().getUserId() + "|" + 
                               t.getBuyer().getUsername() + "|" + 
                               t.getSeller().getUserId() + "|" + 
                               t.getListing().getListingId() + "|" + 
                               t.getTransactionDate().getTime());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getUserTransactions(User user) {
        return transactions.stream()
            .filter(t -> t.getBuyer().getUserId().equals(user.getUserId()) || 
                         t.getSeller().getUserId().equals(user.getUserId()))
            .collect(Collectors.toList());
    }

    public boolean registerUser(User user) {
        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(user.getUserId())) return false;
        }
        users.add(user);
        saveUsers(); 
        return true;
    }
    
    public void addListing(Listing listing) { 
        listings.add(listing); 
        saveListings();
    }
    
    public boolean removeListing(int listingId) { 
        boolean removed = listings.removeIf(l -> l.getListingId() == listingId);
        if (removed) saveListings();
        return removed;
    }

    public Object login(String id, String pass) {
        for (User u : users) {
            if (u.getUserId().equals(id) && u.getPassword().equals(pass)) {
                this.currentUser = u;
                return u;
            }
        }
        return null;
    }
    
    public void logout() { this.currentUser = null; }
    public User getCurrentUser() { return currentUser; }
    public List<Listing> getAllListings() { return listings; }
    public BookExchangeSystem getSystem() { return this; }
}