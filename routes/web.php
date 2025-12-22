<?php

use App\Http\Controllers\ProfileController;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('welcome');
});

Route::get('/dashboard', function () {
    return redirect('/admin');
})->middleware(['auth', 'verified'])->name('dashboard');

Route::middleware('auth')->group(function () {
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');
});

// Admin Routes
Route::middleware(['auth'])->prefix('admin')->name('admin.')->group(function () {
    Route::get('/', function () {
        return redirect()->route('admin.reports.index');
    })->name('dashboard');

    Route::resource('shelters', App\Http\Controllers\Admin\ShelterController::class);
    Route::resource('news', App\Http\Controllers\Admin\NewsController::class);
    
    Route::get('reports', [App\Http\Controllers\Admin\ReportController::class, 'index'])->name('reports.index');
    Route::get('reports/{report}', [App\Http\Controllers\Admin\ReportController::class, 'show'])->name('reports.show');
    Route::patch('reports/{report}/status', [App\Http\Controllers\Admin\ReportController::class, 'updateStatus'])->name('reports.updateStatus');
});

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\FloodController;

// Public route for login
Route::get('/login', function () {
    return redirect('/admin/login');
})->name('login');

Route::post('/auth/google', [AuthController::class, 'googleLogin']);

// Protected routes (User must be logged in)
Route::middleware('auth:sanctum')->group(function () {
    Route::get('/map-data', [FloodController::class, 'getMapData']);
    Route::post('/map-data', [FloodController::class, 'storeReport']); // Alias for user request
    Route::post('/report', [FloodController::class, 'storeReport']);
});
