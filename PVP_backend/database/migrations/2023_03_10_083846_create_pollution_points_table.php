<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('pollution_points', function (Blueprint $table) {
            $table->id();
            $table->timestamps();
            $table->string('name');
	        $table->string('description');
            $table->string('longitude');
            $table->string('latitude');
            $table->string('rating');
            $table->integer('type');
            $table->integer('size');
            $table->integer('isActive');
            $table->integer('reportCount');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('pollution_points');
    }
};
