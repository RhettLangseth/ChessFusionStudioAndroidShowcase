# ChessFusionStudio Android Showcase

A curated public showcase derived from the private `ChessFusionStudio` project.

Both this showcase repo and the private `ChessFusionStudio` repo it was derived from were built through iterative collaboration with OpenAI Codex / agentic AI.
A core goal of this work is to demonstrate my ability to use agentic AI effectively: define scope, direct implementation, evaluate output quality, and drive the codebase toward a coherent engineering result.

This repo is intentionally narrow. It focuses on the parts that best demonstrate Android engineering judgment without exposing the full private product surface:
- Java core domain modeling carried forward from an earlier chess codebase I originally wrote myself, then adapted with AI/Codex during transfer into `ChessFusionStudio`
- Kotlin/Compose Android adaptation
- custom board and piece rendering
- reusable UI primitives
- persisted settings driving a live preview workflow

## Notice

This repository is public for portfolio review only.
No license is granted for reuse, modification, or redistribution.
See [NOTICE.md](NOTICE.md).

## What This Repo Demonstrates

- clear `core` / `app` module boundaries
- adapting a Java codebase I originally wrote into a modern Android app
- practical use of agentic AI as part of a real engineering workflow
- custom rendering work beyond stock Compose widgets
- custom controls that are reusable outside this specific screen
- a small persisted state layer wired into a `ViewModel`
- a deliberately curated public scope instead of a raw product dump

## Current Showcase Flow

The app currently exposes one focused `Theme Studio` workflow:
- preview a sample chess position
- switch between curated sample positions
- adjust board palette
- adjust piece palette
- fine-tune colors with a custom picker
- adjust piece scale with a custom slider
- see changes reflected immediately in the rendered board

## Project Structure

- `core/`
  - Java domain slice retained from the earlier codebase I originally authored
  - selected model files
  - FEN parsing
  - curated sample positions for the public showcase
- `app/`
  - Kotlin/Compose Android layer
  - reduced renderer and piece painter
  - custom slider and dropdown primitives
  - split color picker
  - persisted showcase settings and `ViewModel`
- `docs/`
  - architecture notes
  - migration story
  - public-scope rationale
- `media/`
  - placeholder location for screenshots and demo captures

## Start Here

If you are reviewing code, these are the best entry points:
- [ThemeStudioScreen.kt](app/src/main/java/com/chessfusionstudio/showcase/ui/showcase/ThemeStudioScreen.kt)
- [ThemeStudioViewModel.kt](app/src/main/java/com/chessfusionstudio/showcase/ui/showcase/ThemeStudioViewModel.kt)
- [ShowcaseSettingsStore.kt](app/src/main/java/com/chessfusionstudio/showcase/data/settings/ShowcaseSettingsStore.kt)
- [ShowcaseBoardRenderer.kt](app/src/main/java/com/chessfusionstudio/showcase/boardimage/ShowcaseBoardRenderer.kt)
- [ShowcasePieceRenderer.kt](app/src/main/java/com/chessfusionstudio/showcase/ui/components/ShowcasePieceRenderer.kt)
- [ColorPickerDialog.kt](app/src/main/java/com/chessfusionstudio/showcase/ui/components/ColorPickerDialog.kt)
- [AppSlider.kt](app/src/main/java/com/chessfusionstudio/showcase/ui/components/AppSlider.kt)
- [FenCodec.java](core/src/main/java/com/chessfusionstudio/core/io/FenCodec.java)

## Build And Verify

From the repo root:

```powershell
.\gradlew :app:compileDebugKotlin
.\gradlew :core:test :app:testDebugUnitTest
```

## Why The Repo Is Curated

The private `ChessFusionStudio` codebase contains broader product areas that are intentionally not exposed here, including PGN flows, engine communication, and larger app subsystems.

The goal of this repo is not to recreate the entire private product in public. The goal is to show the strongest engineering slices in a way that is fast for a reviewer to understand.

## Media

This environment can compile and test the app, but it is not set up to capture device/emulator screenshots directly. The repo already includes the `media/` structure so captures can be added cleanly without changing the layout later.

See [media/README.md](media/README.md).
