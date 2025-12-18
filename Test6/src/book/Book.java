package book; 

import item.Item;
import user.User;

public class Book extends Item {
    // Attributes
    private String author;
    private String courseCode;

    // Constructor
    public Book(String title, String description, User owner, String author, String courseCode) {
        super(title, description, owner);
        this.author = author;
        this.courseCode = courseCode;
    }

    @Override
    public void displayInfo() {
        System.out.println("Book Title: " + getTitle());
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public String getAuthor() { return author; }

	@Override
	public int getDescription() {
		// TODO Auto-generated method stub
		return 0;
	}
}