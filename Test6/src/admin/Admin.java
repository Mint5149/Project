public class Admin {
    // Attributes
    private String adminId;
    private String username;
    private String password;
    private String email;

    // Constructor
    public Admin(String adminId, String username, String password, String email) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Methods
    public void viewSystemStats() {
        // TODO: เรียกใช้ getSystemStats จาก System
    }

    public void viewAllUsers() {
        // TODO: เรียกใช้ getAllUsers จาก System
    }

    public void banUser(String userId) {
        // TODO: เรียกใช้ banUser จาก System
    }

    public void unbanUser(String userId) {
        // TODO: เรียกใช้ unbanUser จาก System
    }

    public void forceRemoveListing(int id) {
        // TODO: เรียกใช้ removeListing จาก System
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}