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
