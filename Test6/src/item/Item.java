package item; // <--- [สำคัญมาก] ต้องมี

import user.User; // <--- ต้อง import User

public abstract class Item {
    // Attributes
    protected String title;
    protected String description;
    protected User owner;

    // Constructor
    public Item(String title, String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    // Abstract Method
    public abstract void displayInfo();

    // Getters
    public String getTitle() { return title; }
    public User getOwner() { return owner; }

	public abstract int getDescription();
}