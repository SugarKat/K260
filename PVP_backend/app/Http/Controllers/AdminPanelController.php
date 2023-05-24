<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use App\Models\PollutionPoint;
use App\Models\GarbageDisposalPoint;


class AdminPanelController extends Controller
{
    //
    public function index()
    {
        $users = User::all();
        $users_count = $users->count();
        $users_total_walked = $users->where('distance_travelled', '>', 0)->sum('distance_travelled');

        $trash = PollutionPoint::all();
        $total_trash = $trash->count();
        $total_trash_cleaned = $trash->where('isActive', 0)->count();

        $total_trash_by_type = [
            '1' => ['Plastic', $trash->where('type', 1)->count()],
            '2' => ['Paper', $trash->where('type', 2)->count()],
            '3' => ['Glass', $trash->where('type', 3)->count()],
            '4' => ['Large trash', $trash->where('type', 4)->count()],
            '5' => ['Mixed', $trash->where('type', 5)->count()],
        ];

        $total_trash_by_size = [
            '1' => ['Small pollution', $trash->where('size', 1)->count()],
            '2' => ['Large pollution', $trash->where('size', 2)->count()],
            '3' => ['Very large pollution', $trash->where('size', 3)->count()],
        ];

        $total_trash_cleaned_by_type = [[
            '1' => ['Plastic', $trash->where('type', 1)->where('isActive', 0)->count()],
            '2' => ['Paper', $trash->where('type', 2)->where('isActive', 0)->count()],
            '3' => ['Glass', $trash->where('type', 3)->where('isActive', 0)->count()],
            '4' => ['Large trash', $trash->where('type', 4)->where('isActive', 0)->count()],
            '5' => ['Mixed', $trash->where('type', 5)->where('isActive', 0)->count()],
        ]];

        $total_trash_cleaned_by_size = [
            '1' => ['Small pollution', $trash->where('size', 1)->where('isActive', 0)->count()],
            '2' => ['Large pollution', $trash->where('size', 2)->where('isActive', 0)->count()],
            '3' => ['Very large pollution', $trash->where('size', 3)->where('isActive', 0)->count()],
        ];

        $total_trash_not_cleaned_by_type = [[
            '1' => ['Plastic', $trash->where('type', 1)->where('isActive', 1)->count()],
            '2' => ['Paper', $trash->where('type', 2)->where('isActive', 1)->count()],
            '3' => ['Glass', $trash->where('type', 3)->where('isActive', 1)->count()],
            '4' => ['Large trash', $trash->where('type', 4)->where('isActive', 1)->count()],
            '5' => ['Mixed', $trash->where('type', 5)->where('isActive', 1)->count()],
        ]];

        $total_trash_not_cleaned_by_size = [
            '1' => ['Small pollution', $trash->where('size', 1)->where('isActive', 1)->count()],
            '2' => ['Large pollution', $trash->where('size', 2)->where('isActive', 1)->count()],
            '3' => ['Very large pollution', $trash->where('size', 3)->where('isActive', 1)->count()],
        ];

        $response = [
            'user_count' => $users_count,
            'user_total_walked' => $users_total_walked,
            'total_trash'=> $total_trash,
            'total_trash_by_type' => $total_trash_by_type,
            'total_trash_by_size' => $total_trash_by_size,
            'total_trash_cleaned' => $total_trash_cleaned,
            'total_trash_cleaned_by_type' => $total_trash_cleaned_by_type,
            'total_trash_cleaned_by_size' => $total_trash_cleaned_by_size,
            'total_trash_not_cleaned_by_type' => $total_trash_not_cleaned_by_type,
            'total_trash_not_cleaned_by_size' => $total_trash_not_cleaned_by_size,
        ];

        return response($response, 200);
    }

}
