# Public Scope

## Included

This repo intentionally includes the following public-facing slices:
- selected Java core model files
- FEN parsing
- curated sample board positions
- a reduced custom board renderer
- a reduced custom piece renderer
- reusable slider and dropdown primitives
- a split custom color picker
- a reduced persisted settings layer
- a focused `Theme Studio` screen
- unit tests for the copied/reused logic

## Excluded

This repo intentionally excludes:
- PGN application flows and file handling
- engine communication and bundled native engine assets
- the full private move-generation / rules implementation
- the broader private analyze workflow
- the private warning/confirmation plumbing
- the larger private asset catalogs
- the legacy reference tree from the earlier codebase

## Why The Rules Engine Is Reduced

The original private codebase contains a broader chess implementation than this public repo needs.

For the public showcase, the goal is to demonstrate:
- domain separation
- Android adaptation
- rendering
- UI architecture

The full private rules layer would increase exposure without materially improving that story, so this repo uses curated sample positions instead.

## Why The Renderer Is Reduced Instead Of Fully Removed

Rendering is one of the most differentiated parts of the project. Removing it would weaken the showcase too much.

The compromise used here is:
- keep a real custom rendering path
- narrow it to the public `Theme Studio` flow
- avoid carrying over the full private rendering surface

That keeps the repo visually and technically interesting while still reducing exposure.

## Review Time Goal

This repo is designed so a reviewer can understand the key engineering ideas quickly:
- one focused workflow
- small number of important entry points
- explicit docs explaining what is and is not public

That is a better public artifact than a lightly trimmed copy of the full private app.
