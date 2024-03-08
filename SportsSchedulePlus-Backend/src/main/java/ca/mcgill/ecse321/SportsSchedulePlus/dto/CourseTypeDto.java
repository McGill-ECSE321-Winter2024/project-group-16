package ca.mcgill.ecse321.SportsSchedulePlus.dto;



import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;


public class CourseTypeDTO {

    private Integer id;
    private String description;
    private boolean approvedByOwner;
    private float price;

    public CourseTypeDTO() {
        // Default constructor
    }

    public CourseTypeDTO(Integer id, String description, boolean approvedByOwner, float price) {
        this.id = id;
        this.description = description;
        this.approvedByOwner = approvedByOwner;
        this.price = price;
    }

    public CourseTypeDTO(CourseType courseType) {
        this.id = courseType.getId();
        this.description = courseType.getDescription();
        this.approvedByOwner = courseType.isApprovedByOwner();
        this.price = courseType.getPrice();

    }

    // Getters and setters for each attribute

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApprovedByOwner() {
        return approvedByOwner;
    }

    public void setApprovedByOwner(boolean approvedByOwner) {
        this.approvedByOwner = approvedByOwner;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}