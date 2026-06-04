# Architecture

## Overview

This repo preserves the full project's high-level Java-core and Kotlin/Compose separation while reducing the public feature surface:

- `core`: platform-agnostic Java models, FEN parsing, and curated sample positions
- `app`: Compose screens, navigation, state mapping, persistence, controls, and rendering

The result is a small MVVM-style application with a clear state-driven path from persisted settings to rendered output.

## Module Boundaries

### `core`

`core` contains the public-safe Java foundation:

- model types such as `GameState`, `Piece`, `PieceType`, `Square`, and `Move`
- `FenCodec` for board-state parsing and serialization
- `ShowcaseBoardFactory` and `ShowcasePositions` for curated sample positions

Key files:

- `core/src/main/java/com/chessfusionstudio/core/model/*`
- `core/src/main/java/com/chessfusionstudio/core/io/FenCodec.java`
- `core/src/main/java/com/chessfusionstudio/core/showcase/ShowcaseBoardFactory.java`

### `app`

`app` contains the Android-specific layer:

- a small saved-state navigation host
- Start, Analyze, Settings, and About screens
- `ThemeStudioViewModel` state mapping
- `ShowcaseSettingsRepository` and `ShowcaseSettingsStore`
- custom board and ChessCancun piece rendering
- reusable Compose controls and color editing

Key files:

- `app/src/main/java/com/chessfusionstudio/showcase/ui/navigation/ShowcaseNavHost.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/analyze/ShowcaseAnalyzeScreen.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/settings/ShowcaseSettingsScreen.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/showcase/ThemeStudioViewModel.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/data/settings/ShowcaseSettingsStore.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/boardimage/ShowcaseBoardRenderer.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/components/ShowcasePieceRenderer.kt`

## State And Data Flow

1. `MainActivity` launches `ShowcaseNavHost`.
2. The navigation host renders the current destination from a saveable back stack.
3. Analyze and Settings observe `ThemeStudioViewModel.uiState`.
4. `ThemeStudioViewModel` maps persisted repository snapshots into immutable `ThemeStudioUiState`.
5. User settings update `ShowcaseSettingsStore`, which writes to `SharedPreferences` and publishes a `StateFlow`.
6. The UI redraws from the new state.
7. Analyze-only presentation state, such as board orientation and explanation visibility, uses `rememberSaveable`.

`ShowcaseSettingsRepository` keeps the ViewModel independent from Android persistence details and allows state mapping to be tested with a fake implementation.

## Preset Analyze Workflow

The public Analyze page intentionally demonstrates position review without publishing the private move-generation implementation.

- curated FEN strings are parsed into Java `GameState` objects
- reviewers can select positions directly or browse with previous and next controls
- the selected preset persists through the shared settings repository
- board flipping applies a tested 180-degree coordinate transform in the renderer
- the page explains the boundary between public showcase behavior and the full private workflow

## Rendering Design

### Board

`ShowcaseBoardRenderer`:

- resolves centered board bounds through `ShowcaseBoardGeometry`
- draws a single rounded border and the 8x8 square grid
- applies white-at-bottom or black-at-bottom coordinate mapping
- positions pieces from `GameState`

### Pieces

`ShowcasePieceRenderer` uses:

- `ShowcasePieceGlyphs` for ChessCancun piece-character mapping
- aligned glyph paths generated from `ChessCancun.ttf`
- `ShowcasePieceMaskRasterizer` to fill interior piece backgrounds

This keeps a real font-based rendering path while excluding the broader private rendering pipeline.

## Quality Evidence

The repo includes:

- Java unit tests for FEN and curated positions
- Kotlin unit tests for rendering math, controls, and ViewModel state mapping
- Android tests for settings persistence, navigation, and a rendered-board smoke check
- GitHub Actions automation for build, unit tests, Android-test compilation, and lint
