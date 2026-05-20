# Publishing Checklist

Use this before making the repo public.

## Code And Assets

- Confirm `local.properties` is not committed.
- Confirm no private engine binaries or `jniLibs` content were copied in.
- Confirm no PGN or other intentionally excluded private subsystems were copied in later.
- Recheck all assets you plan to publish for license and attribution requirements.

## Repo Presentation

- Add 3 to 5 real screenshots under `media/screenshots/`.
- Add a short demo GIF or screen recording under `media/`.
- Re-read the README from a first-time reviewer perspective.
- Pin the repo on your GitHub profile after publishing.

## Quality Gate

Run:

```powershell
.\gradlew :app:compileDebugKotlin
.\gradlew :core:test :app:testDebugUnitTest
```

## Final Review

- Make sure the repo still tells the intended story: Java core to Android adaptation, custom rendering, reusable controls, and curated scope.
- Remove anything you would need to verbally ask reviewers to ignore.
