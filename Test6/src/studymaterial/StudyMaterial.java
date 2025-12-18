package studymaterial; // <--- สำคัญมาก! ต้องมีบรรทัดนี้

import item.Item;      // <--- ต้อง Import Item
import user.User;      // <--- ต้อง Import User

public class StudyMaterial extends Item {
    // Attributes
    private String subject;
    private String courseCode;

    // Constructor
    public StudyMaterial(String title, String description, User owner, String subject, String courseCode) {
        super(title, description, owner);
        this.subject = subject;
        this.courseCode = courseCode;
    }

    @Override
    public void displayInfo() {
        System.out.println("Study Material: " + getTitle() + " | Subject: " + subject + " | Code: " + courseCode);
    }
    
    // Getter
    public String getSubject() { return subject; }
    public String getCourseCode() { return courseCode; }

    @Override
    public int getDescription() {
        return 0;
    }
}