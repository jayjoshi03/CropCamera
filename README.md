# CropCamera
Interview Task complete my best. I hope you like my work.

CropCamera is an Android application that allows users to capture images using the device's rear camera, crop them to a specified viewport, and save the cropped images to the device's storage. Additionally, the app includes a homescreen widget for quick access to the camera functionality and the ability to start/stop a foreground service for obtaining the device's live location.

## Features
- Capture images using the device's rear camera
- Crop captured images to match the size of a specified viewport
- Save cropped images to the device's storage
- Homescreen widget for quick access to camera functionality
- Start/stop a foreground service for obtaining the device's live location
- Display live location in a persistent notification
- Store location data in a CSV file when the service is running

## Getting Started
To get started with the CropCamera app, follow these steps:

### Prerequisites

- Android Studio IDE
- Android device or emulator with API level 21 or higher

### Installation
1. Clone the repository to your local machine:

2. Open the project in Android Studio.

3. Build and run the app on your device or emulator.

## Usage

- Upon launching the app, you will be presented with the home screen.
- Tap the "Open Camera" button to navigate to the camera screen.
- Use the camera interface to capture an image. The captured image will be automatically cropped to match the size of the viewport.
- The cropped image will be saved to the device's storage.
- Use the homescreen widget to quickly access the camera functionality or start/stop the foreground service for obtaining the device's live location.

## Architecture

The CropCamera app follows the MVVM (Model-View-ViewModel) architecture pattern. It separates the presentation layer (UI), business logic, and data handling.

## Acknowledgements
- Thanks to [Google](https://www.google.com) for providing resources and documentation on Android development.
- Special thanks to the open-source community for their contributions and support.

