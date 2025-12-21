<?php

namespace App\Filament\Resources\Locations\Schemas;

use Filament\Schemas\Schema;

class LocationForm
{
    public static function configure(Schema $schema): Schema
    {
        return $schema
            ->components([
                \Filament\Forms\Components\TextInput::make('name')
                    ->required()
                    ->maxLength(255),
                \Filament\Forms\Components\Select::make('type')
                    ->options([
                        'shelter' => 'Safe Shelter',
                        'blocked_road' => 'Blocked Road',
                        'other' => 'Other',
                    ])
                    ->required(),
                \Filament\Forms\Components\Textarea::make('description')
                    ->required()
                    ->columnSpanFull(),
                \Filament\Forms\Components\TextInput::make('latitude')
                    ->numeric()
                    ->required(),
                \Filament\Forms\Components\TextInput::make('longitude')
                    ->numeric()
                    ->required(),
            ]);
    }
}
