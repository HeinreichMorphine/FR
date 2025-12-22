<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::get('/shelters', [App\Http\Controllers\Api\ShelterApiController::class, 'index']);
Route::post('/reports', [App\Http\Controllers\Api\ReportApiController::class, 'store']);
Route::get('/reports', [App\Http\Controllers\Api\ReportApiController::class, 'index']);
Route::get('/news', [App\Http\Controllers\Api\NewsApiController::class, 'index']);
