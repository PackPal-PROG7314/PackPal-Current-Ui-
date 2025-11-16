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

## **Utilization of GitHub and GitHub Actions**

### **GitHub Repository**
The PackPals project is hosted on GitHub with multiple commits tracking iterative development (e.g., "Added weather API integration which is OpenWeather API"). The repository is structured as:
- `app/src/main/java/` - Kotlin source files.
- `app/src/main/res/layout/` - XML layouts.
- `README.md` - This documentation.

- ---

## **Screenshots**
<p align="center">
  **Login Page**
  <img width="333" height="661" alt="image" src="https://github.com/user-attachments/assets/d5f6524a-6054-476a-aad0-2bd1e358a533" />

  <img width="328" height="657" alt="image" src="https://github.com/user-attachments/assets/eca76aaf-24a0-445b-9b10-32157953987c" />
</p>

---

## **Future Improvements**
- Integrate a database (e.g., Room) for persistent trip storage.
- Enhance security with encrypted password storage.
- Add offline mode for weather data caching.

---
## **YouTube Video**
https://youtu.be/LLNWOuGSlLU
---
## **Contributors**
- [Liyema Mangcu] - ST10143385
- [Ganeef Salie] - ST10214012
- [Onello Travis Tarjanne] - ST10178800
- [Khenende Netshivhambe] - ST10379469

---
## **References**

- The Zone (2021). Android x Kotlin Beginner Tutorial [2021] #0 - Project Introduction. [online] YouTube. Available at: https://www.youtube.com/watch?v=GP4p3d7_b2k&list=PLpZQVidZ65jPUF-o0LUvkY-XVAAkvL-Xb [Accessed 23 Apr. 2025].
- Coding with Dev (2023). android date picker dialog example | DatePickerDialog - Android Studio Tutorial | Kotlin. [online] YouTube. Available at: https://www.youtube.com/watch?v=DpL8DhCNKdE&list=PLv59B8ZxVvbv0k49hQxK10bT1fYEmWvMU&index=2 [Accessed 1 May 2025].
- Developer (2024). Date pickers. [online] Android Developers. Available at: https://developer.android.com/develop/ui/compose/components/datepickers [Accessed 1 May 2025].
- Developer (2025). Adapter | API reference | Android Developers. [online] Android Developers. Available at: https://developer.android.com/reference/kotlin/android/widget/Adapter [Accessed 1 May 2025].
- Foxandroid (2021). Custom ListView with item click using Kotlin in Android || ListView in Android || Kotlin. [online] YouTube. Available at: https://www.youtube.com/watch?v=KPvYXXERLjk&list=PLv59B8ZxVvbv0k49hQxK10bT1fYEmWvMU&index=1 [Accessed 1 May 2025].
- geeksforgeeks (2022). DatePicker in Android. [online] GeeksforGeeks. Available at: https://www.geeksforgeeks.org/datepicker-in-android/ [Accessed 1 May 2025].
- FineGap (2025). Available at: https://youtu.be/bhUuVb7g6qk?si=8i2Nw8w09nxzdwv4 [Accessed 1 May 2025].
- Android Knowledge. (2022). Easy Login Page in Android Studio using Kotlin - 5 Steps Only! - Android Knowledge. [online] Available at: https://androidknowledge.com/loginpage-in-android-kotlin/.
- CodeLabX (2025). Available at: https://youtu.be/8obgNNlj3Eo?si=szFq0AC1AIA9qwZ8 [Accessed 1 May 2025].
- The Code City (2025). Available at: https://youtu.be/WAejZCkLJAI?si=F3zcamsTl-dCJDBF [Accessed 1 May 2025].
- Android Knowledge (2023). ListView in Android Studio using Kotlin | Android Knowledge. [online] YouTube. Available at: https://www.youtube.com/watch?v=BntLlH4eSYA [Accessed 1 May 2025].
- ‌Android Developers. (2025). Display pop-up messages or requests for user input. [online] Available at: https://developer.android.com/develop/ui/compose/quick-guides/content/display-user-input [Accessed 10 Apr 2025].
- Shahi, S. (2023). How to create bottom navigation in Android using Kotlin- Beginners Guide. [online] Medium. Available at: https://medium.com/@shahisofiya575/how-to-create-bottom-navigation-in-android-using-material-design-9de844c484a [Accessed 10 Apr 2025].
- Philipp Lackner (2022). Local Notifications in Android - The Full Guide (Android Studio Tutorial). [online] YouTube. Available at: https://www.youtube.com/watch?v=LP623htmWcI [Accessed 26 Nov. 2024].
-  Andy's Tech Tutorials (2022). [Tutorial] - How to Use Weather API for Beginners | Open Weather Map API. [online] Youtu.be. Available at: https://youtu.be/MdIfZJ08g2I?si=gy4aTigN_vK77nzT [Accessed 28 Sep. 2025].
-  Flaticon. “Flaticon, the Largest Database of Free Vector Icons.” Flaticon, 2025, www.flaticon.com/.
