# AV Boulder Dash

<img src ="https://github.com/user-attachments/assets/ff00860e-46fe-4614-b7d8-a8fd748db577" width=100%>

## Intro

This repository is a copy of my group's implementation of Boulder Dash. My group's repository can be found <a href="https://github.com/Karamveer9200/Boulder-Dash-Remake">here!</a> 

Boulder Dash is a classic 1984 Atari game. The player digs through dirt-filled caves, collecting diamonds while avoiding falling rocks and enemies, with the goal of reaching the exit before time runs out.

This repository is a showcase of our game and my work.

## Demo

<img src ="https://github.com/user-attachments/assets/16e9bd1f-c4a8-4a12-84f0-38829a0e31ea" width=49.5%>
<img src ="https://github.com/user-attachments/assets/d4f13dd3-ba48-4137-88f9-dfe2f923c69b" width=49.5%>

Watch a full gameplay and program showcase in this <a href="https://youtu.be/5HLWyaX4BHc">youtube video!</a> 

## Features

- 3 Playable Levels with increasing difficulty.
- Multiple tile types (Path, Dirt, Walls, Magic Walls, Keys, Locks).
- Multiple actors (Boulder, Diamond, Amoeba).
- 😈 and 💩 are enemies that move forward along edges in their paths.
- 🐸 intelligently seeks the player using Dijkstra’s algorithm for shortest-path tracking.
- Complex interactions between all entities, creating dynamic gameplay.
- Time limit for each level.
- Game over and reset for each level.
- Profiles with saving and loading levels in their saved state.
- Scores calculated when levels are beaten and high score table to track different profiles' scores.

For specifically this repository, I focused on organizing and improving the project beyond the group’s original submission. I:

- Restructured the project for a cleaner, more extendable layout.
- Refactored large classes and methods to improve readability and maintainability.
- Fixed small bugs that remained in the group’s final version.

## Experience and Lessons Learnt

- <b>Importance of planning and adaptability:</b> Our initial UML class diagram turned out to be heavily flawed and poorly structured. Only during implementation did we realize what was planned would not work so we had to completely redesign a system plan under time pressure.

- <b>Team collaboration and delegation:</b> Each member took ownership of different sections of the program while regularly reviewing and integrating each other’s work. This taught us the value of clear communication, accountability, and collaborative problem solving.

- <b>Version control in practice:</b> This was my first time working with Git in a shared repository. I learned how to manage merge conflicts, resolve issues quickly, and maintain a clean project history despite the chaos of multiple contributors.

- <b>Balancing quality with deadlines:</b> As the deadline approached, we learned that shipping a functional product sometimes takes precedence over endlessly refactoring code. This taught me how to prioritize features, make trade-offs, and focus on delivering value.

- <b>Refactoring and extending large codebases:</b> Nine months after the group submission, I revisited the project and navigated legacy code. This process taught me the importance of readability, maintainability, and scalability of an existing codebase.

## Set up

This repository is focused on showcasing the source code and JavaFX setup, so no pre-packaged executable is included.
### Requirements
- JDK 21
- JavaFX SDK 21.0.5

### Steps

*Steps shown are based on IntelliJ IDEA. Other IDEs will follow a similar process.*

1) Clone this repository into your IDE as a Java Project.
2) Download <a href="https://gluonhq.com/products/javafx/">JavaFX SDK 21.0.5</a>  and save it somewhere accessible on your computer.
3) In your IDE, go to Project Structure and add both the lib folder from JavaFX SDK and the src folder of this project.
4) Go to Run/Debug Configurations → VM options and add: --module-path "path/to/javafx-sdk-21.0.5/lib" --add-modules javafx.controls,javafx.fxml
5) Make sure the 'resourses' folder is marked as 'Resources Root' and java folder is marked as 'Sources Root'.
6) Run the game and enjoy!

## Acknowledgements

A massive thank you to my teammates for their collaboration and hard work on this project:
- [@Rago179](https://github.com/Rago179)
- [@Karamveer9200](https://github.com/Karamveer9200)
- [@ibbybk](https://github.com/ibbybk)
- [@Superaka5](https://github.com/Superaka5)
- [@rhysll](https://github.com/rhysll)
- [@Tahi-rahman](https://github.com/Tahi-rahman)
