# Application based on Spring Boot 3 and Thymeleaf

## Description

This application was created as a solution to the challenge of converting existing Java applications into a visual format with a graphical user interface (GUI).

The primary goal was to enhance interactivity with Java scripts. One distinctive feature of this application is its independence from a traditional database. All data is stored in a JSON file, which is automatically created with sample data upon the first start of the application.

This allows users to explore and interact with the application immediately.

Upon launching the application, an automatic action is triggered to open the default web browser on the machine running the application.
It navigates to the localhost link, facilitating the autonomous opening of the index page (first endpoint).
The application has been tested on macOS High Sierra and Windows 11, and no issues were encountered with the automatic opening of browsers to access the first endpoint.

While the application is relatively basic, it serves as an experimental platform to explore data manipulation, specifically CRUD operations.

## Compatibility
***Tested on:***
- macOS High Sierra
- Windows 11

## Getting Started
1. Clone the repository.
2. Build and run the application <br>

    - ***Eclipse ide*** :<br>
    (run with command in goal ***clean install*** and the jar will be generate on directory target with name ***"aziendale-0.0.1-SNAPSHOT.jar"*** then you start jar with double click on it);<br>

    - ***Visual Studio Code ide*** : For Visual Studio Code you have to do some extra steps <br>
    [you have to download Maven, then you have to add it to the environment variables for installation and configuration 
    Link: [maven install](https://maven.apache.org/install.html) ;<br>
    after to have the actual changes you have to do a reboot of the PC;<br>
    then on Visual Studio Code you have to install the extension for Java , after verifying that Maven is correctly called by the system (from any cmd,  by running the<br> 
    command :***mvn -v*** ;<br>
    to check the version installed on your PC); then you can open the project in Visual Studio Code, open a terminal and run the <br>
    command: ***mvn clean install***].
    then you start jar with double click on it;<br>

3. Open your web browser and navigate to [//localhost:8080/index](//localhost:8080/index).

## Notes
- The application initializes the JSON file with sample data for a seamless initial experience.
- Data is persisted in the JSON file, eliminating the need for a traditional database.
