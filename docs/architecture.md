# Architecture

## Overview

This repo keeps the same high-level separation as the private `ChessFusionStudio` project owned by Chess Fusion Studio LLC:
- `core`: platform-agnostic Java chess-domain slice
- `app`: Kotlin/Compose Android showcase layer

That split is one of the main architectural points of the showcase.

## Module Boundaries

### `core`

`core` contains the small public-safe Java slice that the Android layer builds on:
- model types such as `GameState`, `Piece`, `PieceType`, `Square`, and `Move`
- `FenCodec` for board-state parsing/serialization
- `ShowcaseBoardFactory` and `ShowcasePositions` to expose curated sample positions without publishing the full private rules engine

Key files:
- `core/src/main/java/com/chessfusionstudio/core/model/*`
- `core/src/main/java/com/chessfusionstudio/core/io/FenCodec.java`
- `core/src/main/java/com/chessfusionstudio/core/showcase/ShowcaseBoardFactory.java`

### `app`

`app` contains the Android-specific layer:
- screen composition
- persisted settings
- `ViewModel` state mapping
- custom rendering
- custom controls

Key files:
- `app/src/main/java/com/chessfusionstudio/showcase/MainActivity.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/showcase/ThemeStudioScreen.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/showcase/ThemeStudioViewModel.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/data/settings/ShowcaseSettingsStore.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/boardimage/ShowcaseBoardRenderer.kt`
- `app/src/main/java/com/chessfusionstudio/showcase/ui/components/ShowcasePieceRenderer.kt`

## Data Flow

The current flow is intentionally simple and reviewable:

1. `MainActivity` launches `ThemeStudioScreen`.
2. `ThemeStudioScreen` obtains `ThemeStudioViewModel`.
3. `ThemeStudioViewModel` exposes a mapped `ThemeStudioUiState` built from `ShowcaseSettingsStore`.
4. `ShowcaseSettingsStore` persists showcase settings in `SharedPreferences` and publishes them through a `StateFlow`.
5. The screen renders:
   - a board preview via `ShowcaseChessBoard`
   - section controls for board theme, piece theme, and position selection
   - a split custom color picker dialog when a color swatch is edited
6. `ShowcaseBoardRenderer` and `ShowcasePieceRenderer` convert `ThemeStudioUiState` into the visual preview.

## Rendering Design

The rendering path is intentionally reduced relative to the private repo.

### Board

`ShowcaseBoardRenderer`:
- computes board bounds and square geometry
- draws a border and shadow
- paints the 8x8 board squares
- positions pieces based on `GameState`

`ShowcaseBoardGeometry` contains the geometry calculations so layout math is separate from drawing.

### Pieces

`ShowcasePieceRenderer` uses:
- `ShowcasePieceMask` for reduced shape construction
- `ShowcasePieceLighting` for gradient, outline, accent, and highlight colors

This keeps the rendering visually differentiated while avoiding the broader private rendering pipeline.

## UI Primitive Reuse

The repo intentionally keeps a few reusable components as whole files instead of flattening everything into one screen:
- `AppSlider.kt`
- `AppControlPrimitives.kt`
- `DiscreteSliderMath.kt`
- `OptionMath.kt`

That is a more honest representation of how the app is structured than rewriting everything into one large showcase file.

## Persistence Design

`ShowcaseSettingsStore` is intentionally smaller than the private store.

It persists only the values needed for the public workflow:
- selected sample position
- board palette
- piece palette
- custom board colors
- custom piece colors
- piece scale

This shows the state-management pattern without dragging in unrelated product settings.
