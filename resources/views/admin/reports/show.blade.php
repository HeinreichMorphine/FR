<x-admin-layout>
    <x-slot name="header">
        <h2 class="font-semibold text-xl text-gray-800 leading-tight">
            {{ __('Report Details') }} #{{ $report->id }}
        </h2>
    </x-slot>

    <div class="py-12">
        <div class="max-w-7xl mx-auto sm:px-6 lg:px-8">
            <div class="bg-white overflow-hidden shadow-sm sm:rounded-lg">
                <div class="p-6 text-gray-900">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <h3 class="text-lg font-medium text-gray-900">Incident Information</h3>
                            <div class="mt-4">
                                <p class="text-sm text-gray-600">Type</p>
                                <p class="text-lg font-semibold">{{ $report->incident_type }}</p>
                            </div>
                            <div class="mt-4">
                                <p class="text-sm text-gray-600">Description</p>
                                <p class="text-base">{{ $report->description }}</p>
                            </div>
                            <div class="mt-4">
                                <p class="text-sm text-gray-600">Reported By</p>
                                <p class="text-base">{{ $report->user_name }} ({{ $report->user_agent }})</p>
                            </div>
                            <div class="mt-4">
                                <p class="text-sm text-gray-600">Time</p>
                                <p class="text-base">{{ $report->report_time }}</p>
                            </div>
                            <div class="mt-4">
                                <p class="text-sm text-gray-600">Verification Score</p>
                                <p class="text-base">{{ $report->verification_count }}</p>
                            </div>
                        </div>

                        <div>
                            <h3 class="text-lg font-medium text-gray-900">Location & Status</h3>
                            <div class="mt-4">
                                <p class="text-sm text-gray-600">Coordinates</p>
                                <p class="text-base">{{ $report->latitude }}, {{ $report->longitude }}</p>
                                <a href="https://www.google.com/maps/search/?api=1&query={{ $report->latitude }},{{ $report->longitude }}" target="_blank" class="text-blue-600 hover:text-blue-800 text-sm">View on Google Maps</a>
                            </div>

                            <div class="mt-8 border-t pt-4">
                                <form action="{{ route('admin.reports.updateStatus', $report) }}" method="POST">
                                    @csrf
                                    @method('PATCH')
                                    <x-input-label for="status" :value="__('Update Status')" />
                                    <div class="flex mt-1">
                                        <select id="status" name="status" class="block w-full border-gray-300 focus:border-indigo-500 focus:ring-indigo-500 rounded-md shadow-sm">
                                            <option value="Active" {{ $report->status == 'Active' ? 'selected' : '' }}>Active</option>
                                            <option value="Verified" {{ $report->status == 'Verified' ? 'selected' : '' }}>Verified (Confirmed)</option>
                                            <option value="False" {{ $report->status == 'False' ? 'selected' : '' }}>False Report</option>
                                            <option value="Resolved" {{ $report->status == 'Resolved' ? 'selected' : '' }}>Resolved</option>
                                        </select>
                                        <x-primary-button class="ml-4">
                                            {{ __('Update') }}
                                        </x-primary-button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</x-admin-layout>
