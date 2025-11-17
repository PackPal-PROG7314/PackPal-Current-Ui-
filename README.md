# PackPal - Travel Planning App
<p align="center">
  <img width="208" height="65" alt="packpalsignup" src="https://github.com/user-attachments/assets/c5123b75-f2d1-4538-ad7c-691f61cfcf07" />
</p>

---

## **Overview**
PackPal is a travel planning app for Android that makes organizing and organizing trips easier. With customizable features like trip name, country, start and end dates, notes, and trip types (business, leisure, family, and adventure), users can create, modify, and delete trips using this app. A contact support page, user profile management, upcoming trip overviews, dynamic packing list generation based on weather conditions and trip duration, and real-time weather updates for trip destinations are some of the key features.

---

## **Purpose**
PackPal is a travel-packing application developed in **Android Studio** with the goal of assisting users who struggle to plan and organize what they need for a trip. The app helps travelers identify and prepare essential items required for journeys of any duration or destination. While the initial version focused mainly on generating packing lists and providing weather updates, the app has since evolved significantly throughout Part 1 and Part 2 of the project.

## Features Added

### **• Share Trip**
Allows users to export their trip details into a PDF and share it with family or friends, or simply keep it for personal reference.

### **• Local Activities**
Displays nearby attractions and activities based on the user’s trip location. This feature integrates with Google Maps, allowing users to tap an activity and be redirected for directions.

### **• Search Flights**
Enables users to search for affordable flights to any travel destination. By entering airport codes along with departure and return dates, the app retrieves and displays the cheapest available flight options.

---

## Project Objective

Our primary objective is to provide travel enthusiasts with an app they can rely on—one that delivers all the essential travel-planning features they need. This includes the ability to:

- Create and manage trips  
- Retrieve accurate weather details  
- View a personalized packing list  
- Share trip information  
- Explore local activities  
- Search for affordable flights  
By ensuring these core functions operate seamlessly, PackPal aims to offer a complete and trustworthy travel-planning experience.
---

## Design Considerations

Based on feedback from the initial rubric, the app’s interface was considered *“poorly designed.”* In response, several UI/UX enhancements were implemented to create a more visually appealing, intuitive, and cohesive user experience. These improvements were made alongside structural and functional considerations to ensure the app remains reliable, efficient, and user-friendly.

---

### **UI/UX Enhancements**

- **Custom Input Containers:** Replaced basic TextViews with custom-styled input fields to provide a cleaner and more professional appearance.
- **Thematic Background Image:** Added a background image to visually reinforce the travel theme and create a more engaging interface.
- **Improved Button Visibility:** Enhanced button size and contrast to make key actions more prominent and accessible.
- **Card-Based Trip Display:** Updated the trip list to a modern card layout for better readability and aesthetic appeal.
- **Consistent Color Scheme:** Unified the app’s color palette to ensure visual consistency across all screens.

---

### **User Interface (UI)**

- **Intuitive Navigation:** Implemented a bottom navigation bar using `BottomNavigationView` and `NavController` to provide seamless access to key sections of the app.
- **Responsive Layouts:** XML layouts (e.g., `activity_create_trips.xml`, `trip_item.xml`) use `ConstraintLayout` to ensure compatibility with various screen sizes and devices.
- **Visual Feedback:** Dynamic weather icons and user notifications help enhance clarity and engagement.

---

### **Functionality**

The functionality of PackPal has expanded significantly to support a full travel-planning experience. The app integrates multiple core systems—trip creation, weather retrieval, activity exploration, and flight searching—while maintaining performance, responsiveness, and usability.

---

#### **Trip Management & Data Handling**
- Trips are represented using a structured `Trip` data class, storing trip name, destination, dates, notes, trip types, and optional weather or coordinate data.
- Trips are stored using an in-memory `TripRepository` to provide fast access in this prototype version.
- The `Trip` model includes helper functions such as date parsing (`getStartDateAsDate()`) and formatted string output for improved readability across the app.

---

#### **Weather Integration**
- Real-time weather information is retrieved through asynchronous calls using **OkHttp**.
- Weather conditions influence the generated packing list, ensuring users prepare appropriately for their destination’s climate.
- Weather icons and dynamic UI responses enhance clarity and user feedback.


#### **Search Flights (CheapFlightsFragment)**
- Users can search for affordable flights using Amadeus API credentials.
- The feature includes:
  - Origin and destination airport code input
  - Date pickers for departure and optional return date
  - Coroutine-based API calls in `FlightViewModel` for efficient processing
  - A RecyclerView (`FlightAdapter`) that updates automatically via Flow collection
- This system provides fast, responsive, and accurate flight listings while keeping the UI fully non-blocking.


#### **Local Activities & Google Maps Integration (MapsActivity)**
- Users can explore nearby local attractions around their travel destination.
- Features include:
  - Google Maps display with zoom, compass, and toolbar controls
  - Automatic geocoding of destination names using `Geocoder`
  - Fallback coordinates for international destinations when geocoding fails
  - Clickable map interactions allowing users to select new points of interest
  - A dynamic RecyclerView list of attractions, each clickable to:
    - Add markers to the map
    - Display ratings, type, and distance
    - Smoothly animate the camera view
- Designed with coroutines for background geocoding and attraction loading to maintain UI responsiveness.


#### **Attraction System**
- Attractions are represented using an `Attraction` data class, including name, type, rating, and distance.
- `AttractionsAdapter` populates the list using Material-style list items (`simple_list_item_2`).
- When users select an attraction:
  - A new marker is added to the map
  - Camera moves to the attraction
  - A Toast provides immediate feedback
- Although mock data is used for now, the design is structured to support future integration with the Google Places API.


#### **Security & User Settings**
- User data management is handled using `SharedPreferences`, including:
  - Profile details
  - Password updates
  - Account deletion
- This lightweight approach is suitable for prototypes while remaining easy to upgrade later.


#### **Notifications**
- Achievement and update notifications use `NotificationCompat` for compatibility across Android versions.
- Notifications are triggered when users complete certain app actions, improving user engagement.


#### **Performance**
- All network and geocoding operations run asynchronously using Kotlin coroutines.
- RecyclerView adapters update efficiently using Flow and LiveData patterns.
- Modular architecture supports:
  - Easy API upgrades  
  - Migration to persistent storage (RoomDB) in future development  
  - Separation of UI and data logic through ViewModels  


### **Performance**

- **Efficiency:** Asynchronous HTTP requests prevent the UI from freezing and ensure responsive interactions.
- **Scalability:** Modular architecture allows for future expansion, including migrating to persistent storage solutions.


### **Accessibility**

- Clear labels, hints, and toast messages improve usability for users with varying levels of technical experience.

---
### **App Run**
App Setup & Run
1. Clone the project:
To clone the project you will need to run the following commands below
 <img width="783" height="25" alt="image" src="https://github.com/user-attachments/assets/292c75cf-0f45-482f-822a-eb6227b8ea62" />
<img width="288" height="36" alt="image" src="https://github.com/user-attachments/assets/8fb2610d-ad46-4438-be1e-a3cabf4a4a6d" />
 
2. Open the project in Android Studio:
-	Launch Android Studio.
-	Select Open an existing project.
-	Navigate to the folder you just cloned and click OK.
3. Configure Firebase:
-	Make sure the google-services.json is located in app/.
-	If you are going to use your own Firebase project, download a new google-services.json from the Firebase Console and replace the existing one.
4. Sync Gradle:
-	Click Sync Now when asked or go to File → Sync Project with Gradle Files.
5. Run the app:
-	Android Studio: Click the green Run button or press Shift + F10.
-	Another way is to run a command (./gradlew :app:assembleDebug) then after words install the APK from app/build/outputs/apk/debug. 
-	If the previous way doesn’t work you can navigate to build at the top and click “Generate Bundles or APKs” then click “Generate APKs” 

---

## **Utilization of GitHub and GitHub Actions**

### **GitHub Repository**
The PackPals project is hosted on GitHub with multiple commits tracking iterative development (e.g., "Added weather API integration") 
<img width="385" height="515" alt="image" src="https://github.com/user-attachments/assets/5f36f0db-6b47-4250-8e9b-886e8aaca8eb" />


---

## **Screenshots**

<p align="center">

### **Login Page**  
<img src="https://github.com/user-attachments/assets/d5f6524a-6054-476a-aad0-2bd1e358a533" width="333" height="661"/>

---

### **Home Page**  
<img src="https://github.com/user-attachments/assets/619e4fe3-2633-4783-8f02-c3a0df2be389" width="336" height="736"/>

---

### **Trip Creation Page**  
<img src="https://github.com/user-attachments/assets/bc29cc37-cad7-494a-8d64-e352681084c4" width="334" height="658"/>

---

### **View Trip Page**  
<img src="https://github.com/user-attachments/assets/952e8ae2-586e-403b-97c2-dd30f9f5c77e" width="329" height="522"/>

---

### **Attractions Page** 
<img width="329" height="736" alt="image" src="https://github.com/user-attachments/assets/c9816853-f2f1-4cc2-b316-eec83a4f1b8c" />

---

### **Contact Us Page**  
<img src="https://github.com/user-attachments/assets/34c1bb9b-b642-4565-8f49-cac15ba20ddf" width="325" height="586"/>

---

### **Flight Search**  
<img src="https://github.com/user-attachments/assets/0610a858-295b-4458-a0b9-ce4a69faeb91" width="339" height="734"/>

---

### **Edit Profile**  
<img src="https://github.com/user-attachments/assets/64083320-bab6-46a9-97e3-18995f7a9e14" width="325" height="694"/>

</p>

---
## **YouTube Video**
https://youtu.be/cS_MyR-rH_0  
---
## **Contributors**
- [Liyema Mangcu] - ST10143385
- [Ganeef Salie] - ST10214012
- [Onello Travis Tarjanne] - ST10178800
- [Khenende Netshivhambe] - ST10379469 

---
## **References**

- Adenit Weekly (2025). Android Multi Language Support. [online] YouTube. Available at: https://youtu.be/COFtAh6NQA8?si=MiNV66mOFP6tnfq_ [Accessed 16 Nov. 2025].
- Amadeus IT Group SA. (n.d.). Connect to Amadeus travel APIs | Amadeus for Developers. [online] Available at: https://developers.amadeus.com/.
- Andy's Tech Tutorials (2022). [Tutorial] - How to Use Weather API for Beginners | Open Weather Map API. [online] YouTube. Available at: https://www.youtube.com/watch?v=MdIfZJ08g2I.
- Android Developers. (n.d.). Create a card-based layout. [online] Available at: https://developer.android.com/develop/ui/views/layout/cardview.
- Android Developers Documentation (2024–2025). Date Pickers, Adapters, etc. Coding with Dev (2023). Android DatePickerDialog Tutorial.
- CodeLabX (2024). Weather App in Android Studio (2024) | Full Step-by-Step Tutorial. [online] YouTube. Available at: https://www.youtube.com/watch?v=aL1wGcyyNJc.
- DataFyx Dom (2024). Find Cheap Flight Tickets using Python & API, beginners step by step guide. [online] YouTube. Available at: https://www.youtube.com/watch?v=p7BmdnmatgQ [Accessed 16 Nov. 2025].
- Easy Tuto (2022). BottomNavigationView with Fragments | Android Studio Tutorial | 2024. [online] YouTube. Available at: https://www.youtube.com/watch?v=OV25x3a55pk
 [Accessed 15 Oct. 2025].
- Flaticon (2025). Free vector icons.
- Foxandroid (2023). Bottom Navigation Bar - Android Studio | Fragments | Java | 2023. [online] YouTube. Available at: https://www.youtube.com/watch?v=jOFLmKMOcK0.
- GeeksforGeeks (2020). CardView in Android With Example. [online] GeeksforGeeks. Available at: https://www.geeksforgeeks.org/android/cardview-in-android-with-example/.
- GeeksforGeeks (2022). How to Implement Google Map Inside Fragment in Android? [online] GeeksforGeeks. Available at: https://www.geeksforgeeks.org/android/how-to-implement-google-map-inside-fragment-in-android/ [Accessed 16 Nov. 2025].
- OpenWeatherMap.org (2015). Weather API - OpenWeatherMap. [online] Available at: https://openweathermap.org/api.
- Panchal, K. (2025). Supporting Multiple Languages in Your Android Application. [online] Medium. Available at: https://medium.com/@khush.panchal123/supporting-multiple-languages-in-your-android-application-a08e21b1baf1 [Accessed 16 Nov. 2025].
- Philipp Lackner (2024). How to Implement Biometric Auth in Your Android App. [online] YouTube. Available at: https://www.youtube.com/watch?v=_dCRQ9wta-I.
- Reference listCodingSTUFF (2025). Maps en Android Studio - Search Videos. [online] Bing.com. Available at: https://www.bing.com/videos/riverview/relatedvideo?q=maps+en+android+studio&mid=75A05FD8BE8F98A4C15D75A05FD8BE8F98A4C15D&FORM=VIRE  [Accessed 16 Nov. 2025].
- The Zone (2021). Android x Kotlin Beginner Tutorial [2021]. YouTube playlist.
- Travelopro.com. (2019). How To Integrate Amadeus API. [online] Available at: https://www.travelopro.com/how-to-integrate-amadeus-api.php
 [Accessed 16 Nov. 2025].
- www.youtube.com. (n.d.). Firebase Google Sign-In With Jetpack Compose & Clean Architecture - Android Studio Tutorial. [online] Available at: https://www.youtube.com/watch?v=zCIfBbm06QM.

