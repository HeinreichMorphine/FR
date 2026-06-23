# Flood Rescue (FR) - Web Server Application

This is the backend and administrator dashboard for the Flood Rescue Android Application. It manages Shelters, Reports, and News/Announcements.

---

## 🛠️ Backend Application Functionality & Architecture

The Web Server application acts as the backend REST API provider and administrative dashboard for the Flood Rescue platform. Here is a breakdown of how the application functions:

### 1. Core Database Models & Schema
*   **`Shelter`**: Represents designated evacuation/safe zones.
    *   Fields: `name`, `description`, `latitude`, `longitude`.
*   **`Report`**: Stores user-submitted flood/incident reports.
    *   Fields: `user_id` (relates to the reporter), `user_name`, `incident_type` (e.g., Flood, Road Blockage, Mudslide), `description`, `status` (defaults to `Active`, can be marked as `Verified` or `False` by admins), `verification_count`, `report_time`, `latitude`, `longitude`, and `user_agent` (captures device properties).
*   **`News`**: Contains emergency bulletins and general updates.
    *   Fields: `title`, `content`, `published_at` timestamp.
*   **`User`**: Manages credentials for admin panel users and API clients.

### 2. Dual Administrative Portals
The application is configured with two distinct admin panels for convenience and database moderation:
*   **Tailwind/Blade Custom Dashboard (`/admin`)**:
    *   Displays real-time statistics counters (Total Reports, Active Shelters, News Updates).
    *   Lists recent reports dynamically with badges showing statuses (`Active`, `Verified`, `False`).
    *   Provides dedicated subpages to edit, delete, and create shelters, news items, and view report details.
*   **Filament Admin Dashboard**:
    *   A pre-configured management panel utilizing Filament PHP packages.
    *   Configures resources for `NewsResource`, `ReportResource`, and `ShelterResource` to manage model data directly.
    *   Includes a `StatsOverview` widget showing system metrics.

### 3. REST API & Mobile Integration Endpoints
The backend exposes specific endpoints to support integration with the companion Android mobile client:
*   **Public API Routes (`routes/api.php`)**:
    *   `GET /api/shelters`: Fetches all shelters. Note that these are mapped to match the mobile app's map icon format where the `incident_type` is hardcoded to `'Shelter'` and `verification_count` is set to `100` (marking it as a trusted source).
    *   `GET /api/reports` / `POST /api/reports`: Lists submitted reports or posts a new incident report.
    *   `GET /api/news`: Fetches published announcements ordered chronologically.
*   **OAuth & Secure Routes (`routes/web.php`)**:
    *   `POST /auth/google`: Validates a Google OAuth ID Token sent by the Android client, checks/registers the user in the database, and responds with a Laravel Sanctum personal access token.
    *   `GET /map-data` / `POST /report`: API endpoints protected by `auth:sanctum` middleware. `getMapData()` returns a combined list of shelters and the 50 most recent incident reports.

---

## 🚀 For Collaborators: Installation Guide

**Important:** This repository **excludes** the `.env` configuration file and the `vendor/` dependency folder for security and efficiency. You **MUST** run `composer install` and set up your own environment file.

### 1. Prerequisites
- **Laragon** (recommended).
- **Composer** (installed globally or via Laragon).
- **SQLYog** or similar DB tool.
- **Node.js**.

### 2. Setup

#### A. Download/Clone the Project
Clone this repository to your local machine (e.g., `C:\laragon\www\FR`).

#### B. Install Dependencies
Open your terminal in the project folder and run:

```bash
composer install
npm install
```

#### C. Database Setup (SQLYog)
1.  Open **SQLYog**.
2.  Connect to your local MySQL server.
3.  Create a new Database named **`fr`**.

#### D. Verify Environment (.env)
1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```
2. Generate an application key:
   ```bash
   php artisan key:generate
   ```
3. Ensure the database settings match your local setup:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=fr      <-- Must match your SQLYog DB name
DB_USERNAME=root    <-- Your SQLYog username
DB_PASSWORD=        <-- Your SQLYog password (leave blank if none)
```

**Note:** The `.env` file is ignored by Git to protect your local credentials and secrets.

#### D. Run Migrations
To create the tables (Shelters, Reports, News, Users) in your new `fr` database:

```bash
php artisan migrate:fresh --seed
```
*This command will also create a default Admin user.*


### 3. Running the Application

You need to run two commands in parallel terminals:

**Terminal 1 (Frontend Assets):**
```bash
npm run dev
```

**Terminal 2 (Backend Server):**
```bash
php artisan serve
```

Access the Admin Panel at: [http://localhost:8000/admin](http://localhost:8000/admin)
- **Email:** `admin@admin.com`
- **Password:** `password`


### 4. API Endpoints (For Mobile App)

The mobile app connects to these endpoints:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/reports` | Submit a new incident report |
| `GET` | `/api/reports` | Get list of all incident reports |
| `GET` | `/api/news` | Get list of announcements |
| `GET` | `/api/shelters` | Get list of official shelters |


### 5. Mobile App Setup (Android)

1.  Open the Android project in Android Studio.
2.  Update `MapViewModel.kt`:
    *   Set `BASE_URL = "http://10.0.2.2/FR/public/api/"` (for Emulator)
    *   Set `BASE_URL = "http://<YOUR_LAN_IP>/FR/public/api/"` (for Physical Device)
3.  Ensure `AndroidManifest.xml` has `android:usesCleartextTraffic="true"` and Internet permissions.



### 6. Known Limitations & Future Work

*   **Mobile User Authentication**: Currently, the mobile app's "Login" and "Register" screens are **simulated**.
    *   Mobile users are **NOT** stored in the SQL database or Firebase.
    *   The app creates a temporary session using the entered name.
    *   **Future Implementation Needed**: Build API endpoints for user registration/login and implement token-based authentication on the Android client to persist user accounts.

---

## 🔑 Google OAuth (Gmail) Feature Setup

Use this section if you are working on the **Login with Google** feature or Gmail integration. Each collaborator may need their own API credentials.

1.  **Go to Google Cloud Console**: [https://console.cloud.google.com/](https://console.cloud.google.com/)
2.  **Create a Project**: Name it "Flood Rescue Dev".
3.  **Enable APIs**: Enable "Gmail API" or "Google Identity Services" as needed.
4.  **Create Credentials**:
    - Go to **APIs & Services > Credentials**.
    - Click **Create Credentials > OAuth client ID**.
    - Application Type: **Web application**.
    - **Authorized Redirect URIs**: `http://localhost:8000/api/auth/google/callback` (or similar).
5.  **Get Keys**: Copy the **Client ID** and **Client Secret**.
6.  **Update `.env`**:
    Add your keys to the `.env` file:
    ```env
    GOOGLE_CLIENT_ID=your_client_id_here
    GOOGLE_CLIENT_SECRET=your_client_secret_here
    GOOGLE_REDIRECT_URL=http://localhost:8000/api/auth/google/callback
    ```

---

## 🌐 Hosting Guide

To host this application for production usage:

1.  **Shared Hosting / VPS**: Ensure the server supports **PHP 8.2+** and **MySQL**.
2.  **Upload Files**: Upload the entire project (including `vendor`).
3.  **Point Domain**: Point your domain/subdomain to the `public/` folder.
4.  **Database**: Export your local `fr` database from SQLYog and import it to the hosting database. Update `.env` with hosting DB credentials.
5.  **Symlink**: Run `php artisan storage:link` if needed for image uploads.
