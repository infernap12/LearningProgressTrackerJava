# Program Requirements - Stage 3

## Objectives

Extend the program's functionality from the previous stages to:

- Prevent the use of an already registered email address for adding new students.
- Introduce a "list" command to display all added students and their unique IDs.
- Implement an "add points" command to update students' learning progress.
- Recognize the "find" command to display details of a student by their ID.

### Additional Functional Requirements

- **Email Uniqueness**:
    - Check if the provided email is already in use. If it is, respond with: "This email is already taken."

- **Listing Students**:
    - Implement a "list" command that prints "Students:" followed by the student IDs in the order they were added.
    - If no students have been added, print "No students found."
    - Each student ID must be unique.

- **Adding Learning Progress Points**:
    - Recognize the "add points" command and respond with: "Enter an id and points or 'back' to return."
    - Accept learning progress data in the format: `studentId number number number number`, corresponding to the courses (Java, DSA, Databases, Spring), where each `number` is a non-negative integer.
    - If a student with the specified ID does not exist, print: "No student is found for id=%s.", where %s is the invalid ID.
    - For incorrect points format (missing numbers, extra numbers, or invalid numbers), print: "Incorrect points format."
    - On correct data entry and existing student ID, update the student's record and print: "Points updated."
    - Stop reading learning progress data once "back" is entered.

- **Finding Students**:
    - Recognize the "find" command and respond with: "Enter an id or 'back' to return."
    - On entering an ID, print the student's details in the format: "id points: Java=%d; DSA=%d; Databases=%d; Spring=%d", where %d represents the points earned by the student in the respective course.
    - If no student is found for the entered ID, print: "No student is found for id=%s.", where %s is the invalid ID.

### Testing

- Utilize unit tests to ensure the functionality for checking email uniqueness, listing students, adding learning progress points, and finding students by ID is correctly implemented.
