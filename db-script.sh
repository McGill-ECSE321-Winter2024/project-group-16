#!/bin/bash

# Create customers
curl -X POST -H "Content-Type: application/json" -d '{"name": "Emily Johnson", "email": "emily.johnson@gmail.com", "password": "P@ssw0rd1"}' "http://localhost:8080/customers"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Michael Smith", "email": "michael.smith@gmail.com", "password": "P@ssw0rd1"}' "http://localhost:8080/customers"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Sophia Williams", "email": "sophia.williams@gmail.com", "password": "P@ssw0rd1"}' "http://localhost:8080/customers"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Daniel Brown", "email": "daniel.brown@gmail.com", "password": "P@ssw0rd1"}' "http://localhost:8080/customers"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Lily Garcia", "email": "lily.garcia@gmail.com", "password": "P@ssw0rd1"}' "http://localhost:8080/customers"

# Apply for customers to become instructors using PUT
curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/customers/2/apply"
curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/customers/3/apply"
curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/customers/4/apply"

# Approve customers as instructors using PUT
curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/customers/emily.johnson@gmail.com/approve"
curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/customers/michael.smith@gmail.com/approve"
curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/customers/sophia.williams@gmail.com/approve"

# Create sports course types with images and prices
curl -X POST -H "Content-Type: application/json" -d '{"name": "Basketball Training", "description": "Intensive basketball training program for all skill levels.", "image": "https://nationaltoday.com/wp-content/uploads/2021/12/Play-Basketball-Day-1200x834.jpg", "approvedByOwner": true, "price": 100}' "http://localhost:8080/courseTypes"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Tennis Lessons", "description": "Professional tennis coaching to improve your game.", "image": "https://magazine.fortevillageresort.com/wp-content/uploads/2022/01/tennis.jpg", "approvedByOwner": true, "price": 150}' "http://localhost:8080/courseTypes"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Golf Lessons", "description": "Improve your golf swing and technique with professional instruction.", "image": "https://media.npr.org/assets/img/2023/03/01/gettyimages-1410422468_wide-f64095a661d8b05ad0433ef9da08b1f83dd23d24.jpg", "approvedByOwner": true, "price": 200}' "http://localhost:8080/courseTypes"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Soccer Camp", "description": "Learn soccer skills and tactics in a fun and supportive environment.", "image": "https://jcccampsatmedford.org/wp-content/uploads/2020/03/soccer-ball-ss-img.jpg", "approvedByOwner": true, "price": 120}' "http://localhost:8080/courseTypes"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Swimming Lessons", "description": "Beginner to advanced swimming lessons for all ages.", "image": "https://domf5oio6qrcr.cloudfront.net/medialibrary/9833/GettyImages-526245433.jpg", "approvedByOwner": true, "price": 80}' "http://localhost:8080/courseTypes"

# Create scheduled courses
curl -X POST -H "Content-Type: application/json" -d '{"date": "2024-04-20", "startTime": "09:00:00", "endTime": "11:00:00", "location": "Basketball Court", "instructorId": 7, "courseType": {"id": 1}}' "http://localhost:8080/scheduledCourses"
curl -X POST -H "Content-Type: application/json" -d '{"date": "2024-04-22", "startTime": "10:00:00", "endTime": "12:00:00", "location": "Tennis Court", "instructorId": 8, "courseType": {"id": 2}}' "http://localhost:8080/scheduledCourses"
curl -X POST -H "Content-Type: application/json" -d '{"date": "2024-04-25", "startTime": "13:00:00", "endTime": "15:00:00", "location": "Golf Course", "instructorId": 9, "courseType": {"id": 3}}' "http://localhost:8080/scheduledCourses"
curl -X POST -H "Content-Type: application/json" -d '{"date": "2024-04-27", "startTime": "14:00:00", "endTime": "16:00:00", "location": "Soccer Field", "instructorId": 7, "courseType": {"id": 4}}' "http://localhost:8080/scheduledCourses"
curl -X POST -H "Content-Type: application/json" -d '{"date": "2024-04-29", "startTime": "15:00:00", "endTime": "17:00:00", "location": "Swimming Pool", "instructorId": 8, "courseType": {"id": 5}}' "http://localhost:8080/scheduledCourses"

echo "Script execution completed."

