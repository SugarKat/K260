<?php

namespace App\Http\Controllers;
use App\Models\User;
use Illuminate\Http\Request;
use  Illuminate\Http\Response;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller{
    public function register(Request $request) {
        $fields = $request->validate([
            'name' => 'required|string',
            'email' => 'required|string|unique:users,email',
            'password' => 'required|string|confirmed'
        ]);
        $user = User::create([
            'name' => $fields['name'],
            'email' => $fields['email'],
            'password' => bcrypt($fields['password']),
            'points' => 0,
            'distance_travelled' => 0
        ]);
        $user->assignRole('user');
        $token = $user->createToken('myapptoken')->plainTextToken;
	    $roles = $user->getRoleNames();

        $response = [
            'user' => $user,
            'token' => $token,
	    'roles' => $roles
        ];

        return response($response, 201);
    }

    public function logout(Request $request){
        auth()->user()->tokens()->delete();

        return [
            'message' => 'Logged Out'
        ];
    }

    public function login(Request $request) {
        $fields = $request->validate([
            'email' => 'required|string',
            'password' => 'required|string'
        ]);
        //Checks email
        $user = User::where('email', $fields['email'])->first();

        //Check password
        if(!$user || !Hash::check($fields['password'], $user->password)){
            return response([
                'message' => 'bad credentials'
            ], 401);
        }

        $token = $user->createToken('myapptoken')->plainTextToken;
        $roles = $user->getRoleNames();
        $response = [
            'user' => $user,
            'token' => $token,
            'roles' => $roles
        ];

        return response($response, 200);
    }

}
