/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.breezing.health.widget.flake;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * This class represents a single Droidflake, with properties representing its
 * size, rotation, location, and speed.
 */
public class Flake {

    // These are the unique properties of any flake: its size, rotation, speed,
    // location, and its underlying Bitmap object
    float x, y;
    float rotation;
    float speed;
    float rotationSpeed;
    int width, height;
    Bitmap bitmap;

    // This map stores pre-scaled bitmaps according to the width. No reason to create
    // new bitmaps for sizes we've already seen.
    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    /**
     * Creates a new droidflake in the given xRange and with the given bitmap. Parameters of
     * location, size, rotation, and speed are randomly determined.
     */
    static Flake createFlake(float xRange, Bitmap originalBitmap, int canvasHeight, int canvasWidth, int index) {
        Flake flake = new Flake();
        // Size each flake with a width between 5 and 55 and a proportional height
        flake.width = (int) (originalBitmap.getWidth() * 0.8);
        flake.height = (int) (originalBitmap.getHeight() * 0.8);

        // Position the flake horizontally between the left and right of the range
        int space = (canvasWidth - flake.width * FlakeView.DEFAULT_FEATHER_COUNT) / (FlakeView.DEFAULT_FEATHER_COUNT - 1);
        flake.x = 0 - space * index - flake.width * (index + 1);
        // Position the flake vertically slightly off the top of the display
        flake.y = (canvasHeight - flake.height) / 2;

        // Each flake travels at 50-200 pixels per second
        flake.speed = 70;

        // Flakes start at -90 to 90 degrees rotation, and rotate between -45 and 45
        // degrees per second
        flake.rotation = 25 * Math.round(index * 2);
        flake.rotationSpeed = 15;

        // Get the cached bitmap for this size if it exists, otherwise create and cache one
        flake.bitmap = bitmapMap.get(flake.width);
        if (flake.bitmap == null) {
            flake.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int)flake.width, (int)flake.height, true);
            bitmapMap.put(flake.width, flake.bitmap);
        }
        return flake;
    }
}
