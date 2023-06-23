# ClickGame
Speed ClickGame in java

* This JavaFX application is a simple clicking game where players must click on falling objects to gain or lose points. The game features a start screen and a final screen showing the top 5 scores. The FallingObject class represents the falling objects in the game, each with an ImageView, x and y coordinates, and a falling speed (dy).
The ClickGame class extends the Application class and is the main class for the game. It initializes and manages the game state, including starting and stopping the animation loop, adding, and resetting falling objects, and handling user clicks. It also handles the game's start, final, and "play again" screens.
The game starts with a wallpaper and a start button. When the start button is clicked, the game begins with three falling objects. Players must click on these objects to gain or lose points depending on the object clicked. The game speeds up as more objects are clicked and ends after the player has clicked on 30 objects.
At the end of the game, the top 5 scores are displayed along with a "play again" button. By clicking the "play again" button, players can restart the game with the same settings.
