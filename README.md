# DeathBook

A secure digital death-record services prototype for Android.

## Current build

- Native Android application written in Kotlin.
- Package: `za.co.deathbook.app`
- Minimum Android version: Android 6.0 (API 23).
- Home screen with demo submission, verification information and institution information.
- No real personal information is collected or stored by this prototype.

## Important verification notice

DeathBook is **not connected to the South African Department of Home Affairs**. It does not register, certify or verify deaths. Official verification requires formal government authorisation, an approved integration and appropriate privacy and security controls.

## Build

Open this repository in Android Studio and allow Gradle to sync. Then select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

The debug APK is normally generated at:

`app/build/outputs/apk/debug/app-debug.apk`

## Planned work

1. Secure authentication and account roles.
2. Privacy and security design.
3. A controlled death-record submission workflow.
4. Audit logging and institutional access.
5. Official verification integration, only after approval and access are granted.
