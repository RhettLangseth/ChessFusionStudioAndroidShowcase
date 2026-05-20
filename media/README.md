# Media

This folder is reserved for recruiter-facing visuals.

Current contents:
- `screenshots/home.png`
- `screenshots/board-theme.png`
- `screenshots/light-square-picker.png`
- `screenshots/piece-theme.png`
- `demo.gif`

## Quick Capture

### Screenshot

From the repo root:

```powershell
.\media\capture-screenshot.ps1 -Name home
```

That script captures the current Android device or emulator screen into `media/screenshots/` using `adb`.

### Demo Recording

```powershell
.\media\record-demo.ps1 -Name demo -Seconds 12
```

That script records an MP4 from the connected Android device or emulator and saves it as `media/demo.mp4`.

### Demo GIF From Screenshots

```powershell
.\media\build-demo-gif.ps1
```

That script rebuilds `media/demo.gif` from the curated screenshots using only built-in Windows/.NET image APIs.

## Suggested Capture Sequence

1. Theme Studio opening view
2. board palette dropdown expanded
3. color picker open on a board color
4. piece palette and piece scale controls visible
5. one short demo recording showing live preview updates

## Notes

- `adb` is required.
- GIF is usually easier to embed in a README than MP4.
- If you keep MP4, link it from the README or convert it later for inline display.
- The GIF currently in this repo is generated from the screenshot set, which keeps the media reproducible even if emulator capture is unstable on a given machine.
