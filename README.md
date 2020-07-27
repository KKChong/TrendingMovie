Trending Movies
=================

Trending Movies App is implemented based on MVVM pattern with Navigation Component, Hilt, LiveData, Rxjava3, Moshi and more...

[Video Tutorial](https://www.youtube.com/playlist?list=PLZRX7e0nb2a-wBrVjs73OCXlrEEwh0BtN)

TMDB API key
---------------
The app uses the [TMDB API](https://www.themoviedb.org/) to get the movie informations. To use the API,
you need to register an account and get the API Key

Once you have the key, add this line to the `gradle.properties` file, either in your user home
directory (usually `~/.gradle/gradle.properties` on Linux and Mac) or in the project's root folder:

```
tmdb_api=<your tmdb api key>
```