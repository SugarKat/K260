<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class GarbageDisposalPoint extends Model
{
    use HasFactory;
    protected $fillable = [
        'name',
        'description',
        'longitude',
        'latitude',
        'type',
        'size',
    ];
}
