# Nooro Weather Application

A simple application to search for a location and see its current weather.

## Setup Instructions

1. **Clone the repository:** `git clone https://github.com/Kyle-Borth/NooroWeather.git`
2. **Open the project in Android Studio:** Launch Android Studio and select "Open an existing Android Studio project". Navigate to the cloned repository and select the project directory.
3. **Add your WeatherAPI.com API Key:** See details in section below. This key should not be shared or checked into source control.
4. **Sync Gradle:**  Android Studio should automatically prompt you to sync the project with Gradle. If not, you can trigger this manually by clicking on the "Sync Project with Gradle Files" button in the toolbar.
5. **Run the application:** Once Gradle sync is complete, click on the "Run" button in the toolbar to build and run the application on your chosen device or emulator.

## WeatherAPI.com API Key

This app requires and API Key provided by WeatherAPI.com to function properly. 

1. If you have not done so already, create an account here https://www.weatherapi.com/signup.aspx
2. Once you are logged in, your API key can be found at the top of your profile screen
3. In the `local.properties` file, add this line ```WEATHER_API_KEY="yourapikey"```
4. Replace `yourapikey` with your key from step 2

## Contact

Kyle Borth - kyle.borth@outlook.com

Project Link: https://github.com/Kyle-Borth/NooroWeather