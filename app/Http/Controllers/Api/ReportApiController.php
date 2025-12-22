<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Report;
use Illuminate\Http\Request;

class ReportApiController extends Controller
{
    /**
     * Accept JSON data for new reports.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'user_name' => 'required|string|max:255',
            'incident_type' => 'required|string|max:50',
            'description' => 'required|string',
            'latitude' => 'required|numeric',
            'longitude' => 'required|numeric',
            'user_agent' => 'nullable|string|max:255',
        ]);

        // Auto-capture user agent if not provided in body, but usually from header
        $userAgent = $request->user_agent ?? $request->header('User-Agent');

        $report = Report::create([
            'user_id' => $request->user()?->id, // If authenticated
            'user_name' => $validated['user_name'],
            'incident_type' => $validated['incident_type'],
            'description' => $validated['description'],
            'latitude' => $validated['latitude'],
            'longitude' => $validated['longitude'],
            'user_agent' => $userAgent,
            'user_agent' => $userAgent,
            'report_time' => now(), // Explicitly set time so it returns in response
        ]);

        return response()->json($report, 201);
    }

    /**
     * List all reports.
     */
    public function index()
    {
        return Report::orderBy('report_time', 'desc')->get();
    }
}
