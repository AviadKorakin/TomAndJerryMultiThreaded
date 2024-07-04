# Tom and Jerry Game

Welcome to the Tom and Jerry game! This project is an Android application featuring exciting gameplay with Tom and Jerry, incorporating multithreading, maps, media playback, location services, and sensors.

## Features

1. **Multithreading**:
    - Utilizes `ExecutorService` and `Thread` for managing game updates on a separate thread from the main UI thread.
    - Ensures smooth and responsive gameplay by offloading intensive operations.

2. **Maps API**:
    - Integrates Google Maps to display locations associated with scores.
    - When a score is clicked, the corresponding latitude and longitude are sent to a map, updating it to show the new location.

3. **Splash API**:
    - Implements a splash screen to enhance user experience during the app launch.

4. **MediaPlayer**:
    - Manages sound effects using the `MediaPlayer` class.
    - Includes functionality to play, pause, and release sounds efficiently.

5. **Location (FusedLocationProvider)**:
    - Uses `FusedLocationProviderClient` to get the device's location.
    - Provides accurate and efficient location updates for in-game features.

6. **Sensors**:
    - Incorporates device sensors for interactive gameplay elements.

## Usage

### Multithreading Executor and Thread Implementation

- The game logic runs on a separate thread using `Thread`. This ensures the main UI thread remains responsive.

### Maps API

- The game includes a feature to display scores with associated locations on a map. Clicking a score updates the map to show the relevant latitude and longitude.

### Splash API

- A splash screen is displayed when the app launches, providing a smooth transition into the game.

### MediaPlayer

- The `MyMediaPlayer` class manages sound effects. It includes methods to play, pause, and release sounds, ensuring efficient memory usage.

### Location (FusedLocationProvider)

- The `MyLocation` class retrieves the device's last known location using `FusedLocationProviderClient`.

### Sensors

- The game interacts with device sensors to enhance gameplay.

## Watch the Game

For a video demonstration of the game, click [here](https://drive.google.com/file/d/1OQtpxdAJTNcrI2djZqfihZwlQrUZ-Eao/view?usp=sharing).

## Acknowledgments

- Special thanks to the open-source community for providing various libraries and tools used in this project.

## Contact

For any questions or support, please contact aviad825@gmail.com.

**Note: This project is for personal use only.**

Enjoy the game!
