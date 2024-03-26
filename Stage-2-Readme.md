# Program Requirements - Stage 2

## Objective

Extend the program's functionality to include reading user credentials, verifying their correctness, and adding new students based on these credentials.

### Credential Requirements

- **User Provided Information**:
  - First and last name.
  - A valid email address.

- **Email Address Validation**:
  - Ensure the string resembles an email address: includes a name part, the "@" symbol, and a domain part, in accordance with RFC standards.

- **Name Validation Criteria**:
  - Only accept ASCII characters: A-Z, a-z, hyphens (-), and apostrophes (').
  - Allow names like "Jean-Claude" and "O'Neill", but not names containing non-ASCII characters.
  - No length restrictions on names. Divide long names into first and last names at the first space.
  - Names can contain hyphens and apostrophes but not as the first or last character or adjacent to each other.
  - Both the first and last names must be at least two characters long.

### Functionality Requirements

- **New Commands**:
  - Implement an "add students" command that allows entering student credentials or 'back' to return.
  - Implement a "back" command that ends the student-adding session with a summary message (e.g., "Total 5 students have been added") or provides a hint to exit the program.

- **Credential Verification**:
  - Verify entered credentials against the established patterns.
  - If credentials are correct, print "The student has been added."
  - If any part of the credentials is incorrect, indicate the specific issue (e.g., "Incorrect first name", "Incorrect last name", "Incorrect email").
  - For inputs not matching the valid credentials format, print "Incorrect credentials."

### Testing

- Utilize unit tests to ensure all name and email format requirements are correctly implemented.
