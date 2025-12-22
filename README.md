# Flood Rescue (FR) - Web Server Application

This is the backend and administrator dashboard for the Flood Rescue Android Application. It manages Shelters, Reports, and News/Announcements.

## 🚀 For Collaborators: Installation Guide

**Important:** This repository **includes** the `.env` configuration file for convenience, but **excludes** the `vendor/` dependency folder. You **MUST** run `composer install` to set up the project.

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
The `.env` file is included. Ensure the database settings match your local setup:

```env
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=fr      <-- Must match your SQLYog DB name
DB_USERNAME=root    <-- Your SQLYog username
DB_PASSWORD=        <-- Your SQLYog password (leave blank if none)
```

**Note:** Since the `.env` file is included in the repo, be careful not to push your local secrets if you change them for production.

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

### 4. Admin Installation & Troubleshooting

#### Resolving "Route [filament.admin.pages.dashboard] not defined"
If you encounter this error (due to conflicting Filament installation):
1.  Run `composer remove filament/filament`
2.  Delete `app/Providers/Filament` folder
3.  Remove `App\Providers\Filament\AdminPanelProvider::class` from `bootstrap/providers.php`.
4.  Run `php artisan route:clear`

#### Resolving "Unable to locate a class or view for component [admin-layout]"
Run `composer dump-autoload` if you have added new class files manually, or check that `app/View/Components/AdminLayout.php` exists.

### 5. API Endpoints (For Mobile App)

The mobile app connects to these endpoints:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/reports` | Submit a new incident report |
| `GET` | `/api/reports` | Get list of all incident reports |
| `GET` | `/api/news` | Get list of announcements |
| `GET` | `/api/shelters` | Get list of official shelters |

### 6. Mobile App Setup (Android)

1.  Open the Android project in Android Studio.
2.  Update `MapViewModel.kt`:
    *   Set `BASE_URL = "http://10.0.2.2/FR/public/api/"` (for Emulator)
    *   Set `BASE_URL = "http://<YOUR_LAN_IP>/FR/public/api/"` (for Physical Device)
3.  Ensure `AndroidManifest.xml` has `android:usesCleartextTraffic="true"` and Internet permissions.


### 7. Known Limitations & Future Work

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
