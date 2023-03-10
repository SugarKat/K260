<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\PollutionPoint;
class PollutionPointController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return PollutionPoint::all();
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $fields = $request->validate([
            'longitude' => 'required',
            'latitude' => 'required',
            'rating' => 'required',
            'type' => 'required',
        ]);
        $pollution_point = PollutionPoint::create([
            'logitude' => $fields['longitude'],
            'latitude' => $fields['latitude'],
            'rating' => $fields['rating'],
            'type' => $fields['type'],
            'isActive' => true,
            'reportCount' => 0,
        ]);

        return response($pollution_point, 201);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        return PollutionPoint::find($id);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $PollutionPoint = PollutionPoint::find($id);
        $PollutionPoint->update($request -> all());
        return $PollutionPoint;
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        return PollutionPoint::destroy($id);
    }
}
