# GPS Location Tracking Project

This repository contains a simple GPS tracking system built with **Spring Boot** for the backend and **Flutter** for the client. The backend exposes REST and WebSocket endpoints to record and broadcast live GPS positions, while the Flutter app collects the device's latitude and longitude, sends it to the backend, and can optionally display it on a map. The project is meant as a starting point for learning about real‑time location sharing and reactive programming.

## Features

- **REST API** - POST /api/location/update accepts a JSON body with latitude and longitude values and stores the latest position; GET /api/location/latest returns the most recently saved position. Requests are validated using Jakarta Bean Validation.
- **WebSocket (STOMP)** - whenever a new location is saved, the backend broadcasts it on the /topic/location channel using Spring's simple message broker. Clients can subscribe to this topic to receive real‑time updates.
- **Flutter client** - the mobile/desktop app uses the [geolocator](https://pub.dev/packages/geolocator) package to obtain the device's current GPS coordinates. It sends those coordinates to the backend and displays the server's response. You can run it on Android, iOS, macOS or as a web app. The app can be extended to listen to the WebSocket feed and update the UI live.
- **Optional map integration** - the app can be enhanced to show the user's position on a map. You may use:
- [google_maps_flutter](https://pub.dev/packages/google_maps_flutter) - official plugin to embed Google Maps in Flutter apps (requires an API key and pay‑as‑you‑go pricing after free monthly usage caps[\[1\]](https://developers.google.com/maps/billing-and-pricing/overview#:~:text=Free%20usage%20caps%20and%20volume,discounts)).
- [flutter_map](https://pub.dev/packages/flutter_map) - a pure Flutter map client that renders tiles from any provider (e.g., OpenStreetMap). Combining it with a tile service such as **OpenFreeMap** allows unlimited free use without API keys[\[2\]](https://medium.com/@vsvipul10/how-to-use-free-maps-for-any-app-replacing-google-maps-apis-b26f70ca5724#:~:text=OpenFreeMap%20is%20a%20game,edits%20daily%20from%20contributors%20worldwide)[\[3\]](https://openfreemap.org/#:~:text=OpenFreeMap%20lets%20you%20display%20custom,website%20and%20apps%20for%20free). Note that the official OpenStreetMap tile servers are donation‑funded and may block heavy usage[\[4\]](https://operations.osmfoundation.org/policies/tiles/#:~:text=OpenStreetMap%20,tile.openstreetmap.org).

## Prerequisites

- **Backend**
- **Java 21** (required by Spring Boot 4).
- The Gradle wrapper included in the project. You do not need to install Gradle separately.
- **Frontend**
- **Flutter 3.41.4** (or newer) installed on your machine.
- For Android/iOS deployment, Xcode or Android Studio configured with emulators or physical devices.
- **Map integration (optional)**
- For Google Maps: a billing‑enabled Google Cloud project and an API key. Google now provides free usage caps per API SKU that reset each month; requests beyond those caps incur charges[\[1\]](https://developers.google.com/maps/billing-and-pricing/overview#:~:text=Free%20usage%20caps%20and%20volume,discounts).
- For OpenStreetMap: no API key is required, but you should either self‑host map tiles or use a service like OpenFreeMap that offers unlimited access[\[2\]](https://medium.com/@vsvipul10/how-to-use-free-maps-for-any-app-replacing-google-maps-apis-b26f70ca5724#:~:text=OpenFreeMap%20is%20a%20game,edits%20daily%20from%20contributors%20worldwide)[\[3\]](https://openfreemap.org/#:~:text=OpenFreeMap%20lets%20you%20display%20custom,website%20and%20apps%20for%20free).

## Project structure

├── gps-backend/ # Spring Boot server  
│ ├── build.gradle # Gradle build file  
│ └── src/main/java/no/itnow/location  
│ ├── GpsBackendApplication.java # Application entry point  
│ ├── model/LocationRequest.java # Record defining input fields  
│ ├── model/LocationResponse.java # Record defining response fields  
│ ├── service/LocationService.java# Stores latest position  
│ ├── controller/LocationController.java # REST endpoints & WebSocket  
│ └── config/WebSocketConfig.java # STOMP broker configuration  
└── gps-frontend/ # Flutter app  
├── lib/main.dart # Application code  
├── pubspec.yaml # Flutter dependencies  
└── ... # Platform‑specific files

## Running the backend

- **Navigate to the backend directory**:

cd gps-backend

- **Start the server** using the Gradle wrapper:

./gradlew bootRun

This compiles and runs the Spring Boot application on port 8080 with the default profile. You should see log output indicating that Tomcat has started and that the WebSocket broker is running.

- **Test the API** using curl:

\# send a location  
curl -X POST <http://localhost:8080/api/location/update> \\  
\-H "Content-Type: application/json" \\  
\-d '{"latitude":59.9139,"longitude":10.7522}'  
<br/>\# get the latest location  
curl <http://localhost:8080/api/location/latest>

- **WebSocket endpoint**: clients should connect via STOMP over WebSocket to ws://localhost:8080/ws-location and subscribe to /topic/location to receive JSON messages whenever a new location is posted.

## Running the Flutter frontend

- **Navigate to the frontend directory**:

cd gps-frontend

- **Install dependencies**:

flutter pub get

- **Run the app** on your desired platform. Flutter will prompt you to select a device if multiple targets are available. For example:

flutter run

If you run the app on an Android emulator, remember that localhost from the app's perspective refers to the emulator itself. To reach your backend running on your host computer, replace <http://localhost:8080> in lib/main.dart with <http://10.0.2.2:8080>. For a physical device, use your computer's local IP (e.g. <http://192.168.1.45:8080>).

- **Grant location permissions** when prompted. The app will show your latitude and longitude and the server's JSON response. To display a live marker on a map, install a map plugin as described in the [Optional map integration](#optional-map-integration) section.

## Optional map integration

If you want to draw your location on a map, choose one of these options:

### 1 - Google Maps

- Add google_maps_flutter to pubspec.yaml and follow the plugin's setup guide. You will need to supply an API key for both Android and iOS. Google's pricing model offers a monthly free usage cap per service; usage beyond that cap is billed pay‑as‑you‑go[\[1\]](https://developers.google.com/maps/billing-and-pricing/overview#:~:text=Free%20usage%20caps%20and%20volume,discounts).
- In main.dart, include a GoogleMap widget and update the marker position whenever new coordinates are available.

### 2 - Open Street Map / OpenFreeMap

- Use flutter_map as your map widget. It supports raster and vector tiles from any URL. For a truly free solution without API keys or usage caps, configure tiles from the OpenFreeMap project[\[2\]](https://medium.com/@vsvipul10/how-to-use-free-maps-for-any-app-replacing-google-maps-apis-b26f70ca5724#:~:text=OpenFreeMap%20is%20a%20game,edits%20daily%20from%20contributors%20worldwide)[\[3\]](https://openfreemap.org/#:~:text=OpenFreeMap%20lets%20you%20display%20custom,website%20and%20apps%20for%20free). OpenFreeMap offers unlimited access to vector‑tile maps based on OpenStreetMap; the service is open source and requires no registration.
- Alternatively, host your own tile server or choose a commercial provider (Mapbox, MapTiler, etc.) that offers a generous free tier.
- Be aware that while OpenStreetMap's data is free, the official tile servers are donation‑funded and may throttle or block heavy usage. They also do not provide service‑level guarantees[\[4\]](https://operations.osmfoundation.org/policies/tiles/#:~:text=OpenStreetMap%20,tile.openstreetmap.org).

### 3 - MapLibre

MapLibre is an open‑source mapping library derived from Mapbox GL. It can be used via the [maplibre_gl](https://pub.dev/packages/maplibre_gl) Flutter plugin to render vector tiles. You can pair it with OpenFreeMap or your own tile server for a vendor‑free solution.

## Deployment & next steps

This project is intentionally simple. To build a production‑grade service you might:

- Persist historical GPS data in a database instead of keeping only the latest value.
- Use Redis or another in‑memory store for real‑time presence data.
- Add authentication and authorization to secure your endpoints.
- Rate‑limit incoming requests.
- Deploy behind a reverse proxy (e.g., Nginx Proxy Manager) and configure HTTPS.

## License

This project is provided as a learning tool. It does not include any proprietary libraries. If you choose to use Google Maps, ensure you comply with Google's terms of service. OpenStreetMap data is © OpenStreetMap contributors and is provided under the ODbL license.

[\[1\]](https://developers.google.com/maps/billing-and-pricing/overview#:~:text=Free%20usage%20caps%20and%20volume,discounts) Google Maps Platform pricing overview  |  Pricing and Billing  |  Google for Developers

<https://developers.google.com/maps/billing-and-pricing/overview>

[\[2\]](https://medium.com/@vsvipul10/how-to-use-free-maps-for-any-app-replacing-google-maps-apis-b26f70ca5724#:~:text=OpenFreeMap%20is%20a%20game,edits%20daily%20from%20contributors%20worldwide) How to Use Free Maps for Any App: Replacing Google Maps APIs | by Vipul Singh | Medium

<https://medium.com/@vsvipul10/how-to-use-free-maps-for-any-app-replacing-google-maps-apis-b26f70ca5724>

[\[3\]](https://openfreemap.org/#:~:text=OpenFreeMap%20lets%20you%20display%20custom,website%20and%20apps%20for%20free) OpenFreeMap

<https://openfreemap.org/>

[\[4\]](https://operations.osmfoundation.org/policies/tiles/#:~:text=OpenStreetMap%20,tile.openstreetmap.org) Tile Usage Policy

<https://operations.osmfoundation.org/policies/tiles/>
