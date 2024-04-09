package ca.mcgill.ecse321.SportsSchedulePlus.dto.coursetype;

import ca.mcgill.ecse321.SportsSchedulePlus.model.CourseType;

public class CourseTypeResponseDTO implements Comparable<CourseTypeResponseDTO>{
    private Integer id;
    private String name;
    private String description;
    private String image;
    private boolean approvedByOwner;
    private float price;
    private String state;

    public CourseTypeResponseDTO(Integer id, String name, String description, String image, boolean approvedByOwner, float price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.approvedByOwner = approvedByOwner;
        this.price = price;
    }

    // Constructor that takes a CourseType as input
    public CourseTypeResponseDTO(CourseType courseType) {
        this.id = courseType.getId();
        this.name = courseType.getName();
        this.description = courseType.getDescription();
        this.image = courseType.getImage();
        this.approvedByOwner = courseType.isApprovedByOwner();
        this.price = courseType.getPrice();
        this.state = courseType.getState().toString();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
    
    public String getState(){
        return this.state;
    }
    @Override
    public int compareTo(CourseTypeResponseDTO response){
       return this.name.compareTo(response.getName());
    }

   
}
