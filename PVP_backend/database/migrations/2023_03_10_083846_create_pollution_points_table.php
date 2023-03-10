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
            $table->string('longitude');
            $table->string('latitude');
            $table->string('rating');
            $table->string('type');
            $table->boolean('isActive');
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
