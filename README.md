# Snake
The classic snake game

Build with maven.

Used maven version: 3.5.4 and java jdk: 1.8.0_191

To compile and run:
mvn clean
mvn package
java -jar target/<NAME_OF_JARFILE>.jar

![In Game](https://github.com/JacobEkedahl/Snake/blob/master/snake/src/main/resources/preview/gamedark.png?raw=true "In Game")![Game Over](https://github.com/JacobEkedahl/Snake/blob/master/snake/src/main/resources/preview/gameoverdark.png?raw=true "Game Over")![Settings](https://github.com/JacobEkedahl/Snake/blob/master/snake/src/main/resources/preview/settingsWithcolor.png?raw=true "Setings")
![Stats](https://github.com/JacobEkedahl/Snake/blob/master/snake/src/main/resources/preview/statistics.PNG?raw=true "Stats")

Settings is found in Settings.java
It is a singleton class used by most viewclasses and controller.

Changing the color of snake and game will change theme of the whole game. More settings can be added if wanted, (size of snake for example).

Opensource! :)

