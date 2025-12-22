<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Shelter;
use Illuminate\Http\Request;

class ShelterApiController extends Controller
{
    /**
     * Return JSON list of all shelters.
     */
    public function index()
    {
        return response()->json(Shelter::all(), 200);
    }
}
