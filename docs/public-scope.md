# Public Scope

## Included

This repo intentionally includes the following public-facing slices:

- selected Java core model files
- FEN parsing and serialization
- curated sample board positions
- a reduced custom board and piece renderer
- tested board-orientation transforms
- reusable Compose controls and a custom color picker
- a persisted settings repository exposed through `StateFlow`
- a focused multi-screen Compose workflow
- unit, persistence, navigation, and rendering-smoke tests
- automated build, test, and lint checks

## Excluded

This repo intentionally excludes:

- PGN application flows and file handling
- engine communication and bundled native engine assets
- the full private move-generation and rules implementation
- move input and the broader private Analyze workflow
- the private warning and confirmation plumbing
- the larger private rendering and asset catalogs
- the legacy reference tree from the earlier codebase

## Why Analyze Uses Preset Positions

The private codebase contains a broader chess implementation than this public repo needs. Publishing that rules layer would materially increase exposure without strengthening the showcase's main Android-engineering story.

Curated positions provide a deliberate compromise:

- the Java core still parses real FEN state
- the Analyze page offers a complete position-review workflow
- persisted selection and board flipping demonstrate state and rendering behavior
- private move-generation algorithms remain private

## Why The Renderer Remains

Rendering is one of the project's most differentiated technical areas. Removing it would weaken the showcase too much.

The public renderer therefore keeps a real custom drawing path while narrowing the supported styling and excluding the broader private production pipeline.

## Review Time Goal

The repo is designed so a reviewer can understand the key engineering ideas quickly:

- one clear Analyze and Settings workflow
- a small number of important entry points
- automated quality checks
- explicit documentation of the public and private boundary
