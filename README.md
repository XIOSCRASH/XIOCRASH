# Luma Launcher

A lightweight, native Android home-screen starter with a soft iOS-inspired visual style. It uses platform views and property animations rather than a heavyweight rendering framework, and it discovers launchable apps from the device.

## Build

Open this repository in Android Studio (or open this directory as a Gradle project) and build the `app` module. The project uses Android Gradle Plugin 8.6.1 and compile SDK 35. After installing, choose **Luma Launcher** in Android's Home app chooser to set it as the default launcher.

## Included

- Home-screen activity registered as a HOME launcher, with app discovery, launch, sorted labels, and a responsive 3/4/5-column grid.
- Short staggered row entrance animations and an iOS-inspired clock, date, and search-style surface.
- Customization for wallpaper tint, grid columns, icon scale, app labels, and ambient-clock preference.
- Optional, dim, on-demand ambient clock (tap the home clock). It exits on touch and updates only every 30 seconds.

## Always-on display note

Android's actual lock-screen Always-on Display is controlled by the operating system and device manufacturer; a regular launcher cannot keep the display on or replace the lock-screen AOD while the screen is off. Luma's ambient view is a battery-conscious clock shown while the app is foregrounded. The setting enables or disables access to that view; it does not force the screen to remain awake.

## Existing archive

The repository's original `ElegantLauncher-AndroidStudio-Project.zip` is preserved unchanged; this project is the editable source tree.
