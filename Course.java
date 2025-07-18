// Course class
class Course 
{
    private String courseID;
    private String courseName;
    private int userID; //Lecturer who teaches course
    private int credits;
    private int compulsory;
    public Course(String courseID)
    {
        setCourseID(courseID);
    }
    public Course(String courseID, String courseName, int userID, int credits, int compulsary) 
    {
        setCourseID(courseID);
        setCourseName(courseName);
        setUserID(userID);
        setCredits(credits);
        setCompulsory(compulsary);
         
    }
    //getters
     public String getCourseID() {
        return courseID;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public int getCompulsory() {
        return compulsory;
    }
    
    // Setters
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public void setCompulsory(int compulsory) {
        this.compulsory = compulsory;
    }
    
    public String toString()
    {
        String str = String.format("%-20s%-30s%-10d",this.getCourseID(),this.getCourseName(),this.getCredits()); 
        return str;
    }
}
    


