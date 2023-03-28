<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\EmailVerificationController;
use App\Http\Controllers\PollutionPointController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\GarbageDisposalPointController;





/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

//Public routes
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);

//Private routes (authenticated only)
Route::group(['middleware' => ['auth:sanctum']], function(){
    Route::post('/logout', [AuthController::class, 'logout']);
    Route::post('email/verify-notification', [EmailVerificationController::class, 'sendVerificationEmail']);
    Route::post('verify-email/{id}/{hash}', [EmailVerificationController::class, 'verify'])->name('verification.verify');
    Route::resource('pollution-point', PollutionPointController::class);
    Route::resource('user', UserController::class);
    Route::resource('garbage-disposal-point', GarbageDisposalPointController::class);
});

