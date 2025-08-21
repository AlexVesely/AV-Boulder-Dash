# AV Boulder Dash

<img src ="https://github.com/user-attachments/assets/ff00860e-46fe-4614-b7d8-a8fd748db577" width=100%>

## Intro

This repository is a copy of my group's implementation of Boulder Dash. My group's repository can be found <a href="https://github.com/Karamveer9200/Boulder-Dash-Remake">here!</a> 

Boulder Dash is a classic 1984 Atari game. The player digs through dirt-filled caves, collecting diamonds while avoiding falling boulders and enemies, with the goal of reaching the exit before time runs out.

My key contributions included:
- Documenting meeting minutes and keeping the team focussed during weekly meetings.
- Building the game’s foundation with a 2D grid and player movement.
- Creating file handling for the game so that game states can be saved and loaded.
- Developing most of the main menu and implementing 'Player Profile' and 'High Score Table' functionality.

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

After completing the game with my group I returned to focus on organizing and improving the project beyond the group’s original submission. I:

- Restructured the project for a cleaner, more extendable layout (I couldn't fork the group repository to tidy it as it was too messy).
- Refactored large classes and methods to improve readability and maintainability.
- Fixed small bugs that remained in the group’s final version.

## Experience and Lessons Learnt

- <b>Planning and adaptability:</b> Our initial UML class diagram was deeply flawed. Attempting to implement it revealed its shortcomings, teaching me the importance of planning well but staying adaptable.

- <b>Team collaboration:</b> Each member owned a section of the code, but frequent reviews and integration taught us the importance of communication and accountability.

- <b>Version control in practice:</b> This was my first real Git collaboration. I learned how to manage merge conflicts, resolve issues quickly, and maintain a clean project history despite the chaos of multiple contributors.

- <b>Balancing quality with deadlines:</b> Sometimes shipping functional code matters more than perfection. This taught me to prioritize features and trade-offs under pressure.

- <b>Refactoring and extending large codebases:</b> Eight months later, I revisited the project and navigated legacy code. This process taught me the importance of readability and maintainability of an existing codebase.

## Set up

This repository is focused on showcasing the source code, so no pre-packaged executable is included.
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
