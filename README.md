![](weather_app.png)
# Weather App:

An Android weather application implemented using the MVVM pattern, Retrofit2, Dagger Hilt, LiveData, ViewModel, Navigation Components, View Binding and some other libraries from the [Android Jetpack](https://developer.android.com/jetpack) . Weather App fetches data from the [OpenWeatherMap API](https://openweathermap.org/api) to provide real time weather information.


## Architecture
The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [Navigation Components](https://developer.android.com/guide/navigation) to manage fragment operations.
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

## Technologies used:

* [Retrofit](https://square.github.io/retrofit/) a REST Client for Android which makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based webservice.
* [Dagger Hilt](https://dagger.dev/hilt/) for dependency injection.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) to store and manage UI-related data in a lifecycle conscious way.
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) to handle data in a lifecycle-aware fashion.
* [Navigation Component](https://developer.android.com/guide/navigation) to handle all navigations and also passing of data between destinations.
* [Material Design](https://material.io/develop/android/docs/getting-started/) an adaptable system of guidelines, components, and tools that support the best practices of user interface design.
* [View Binding](https://developer.android.com/topic/libraries/view-binding) to bind UI components in layouts to source files.
* [Android KTX](https://developer.android.com/kotlin/ktx) which helps to write more concise, idiomatic Kotlin code.

## Installation
Weather App requires a minimum API level of 24. Clone the repository. You will need an API key i.e. `API_KEY` from [Open Weather](https://openweathermap.org/) to request data.
