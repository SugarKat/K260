<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\GarbageDisposalPoint;

class GarbageDisposalPointController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return GarbageDisposalPoint::all();
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
            'name' => 'required',
            'description' => 'max:255',
            'longitude' => 'required',
            'latitude' => 'required',
            'type' => 'required',
            'size' => 'required'
        ]);
        $garbage_disposal_point = GarbageDisposalPoint::create([
            'name' => $fields['name'],
            'description' => $fields['description'],
            'longitude' => $fields['longitude'],
            'latitude' => $fields['latitude'],
            'type' => $fields['type'],
            'size' => $fields['size']
        ]);

        return response($garbage_disposal_point, 201);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        return GarbageDisposalPoint::find($id);
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
        $garbage_disposal_point = GarbageDisposalPoint::find($id);
        $garbage_disposal_point->update($request -> all());
        return $garbage_disposal_point;
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        return GarbageDisposalPoint::destroy($id);
    }
}
