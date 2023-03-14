<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class PollutionPoint extends Model
{
    use HasFactory;

    protected $fillable = [
        'longitude',
        'latitude',
        'rating',
        'type',
        'isActive',
        'reportCount',
    ];
}
