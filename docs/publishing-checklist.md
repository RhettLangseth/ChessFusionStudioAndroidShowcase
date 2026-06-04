# Portfolio Release Checklist

Use this before sharing a new portfolio release.

## Code And Assets

- Confirm `local.properties` is not committed.
- Confirm no private engine binaries or `jniLibs` content were copied in.
- Confirm no PGN or other intentionally excluded private subsystems were copied in later.
- Recheck all assets you plan to publish for license and attribution requirements.

## Repo Presentation

- Capture current screenshots only after the pages are approved.
- Record a short demo only after the pages are approved.
- Re-read the README from a first-time reviewer perspective.
- Pin the repo on your GitHub profile after publishing.

## Quality Gate

Run:

```powershell
.\gradlew :core:test :app:testDebugUnitTest :app:compileDebugAndroidTestKotlin :app:lintDebug :app:assembleDebug
.\gradlew :app:connectedDebugAndroidTest
```

## Final Review

- Make sure GitHub Actions passes.
- Make sure the repo still tells the intended story: Java core to Android adaptation, custom rendering, state-driven UI, reusable controls, and curated scope.
- Remove anything you would need to verbally ask reviewers to ignore.
