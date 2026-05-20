# Migration Story

## Context

The private `ChessFusionStudio` project grew from an earlier Java chess codebase. The engineering challenge was not just to build an Android UI, but to adapt existing chess-domain logic into a maintainable Kotlin/Java Android application.

This public repo preserves that story in a smaller form.

## What Stayed In Java

The showcase keeps the domain slice in Java to reflect the original foundation and to preserve a clean boundary between domain logic and platform code.

The Java portion includes:
- board-state model types
- piece and square representation
- FEN parsing
- curated sample-position helpers

That is deliberate. Rewriting everything into Kotlin just for the showcase would hide the migration reality instead of demonstrating it.

## What Moved Into Kotlin / Android

The Android layer is intentionally Kotlin/Compose-first:
- screen composition
- `ViewModel` state shaping
- state persistence for the showcase workflow
- custom controls
- rendering integration
- dialog flows

This reflects the actual adaptation work: the platform layer is not a thin wrapper. It is where architecture, UX, and rendering concerns are managed.

## What Was Intentionally Left Out

This public repo does not expose several larger private subsystems:
- PGN parsing/export flows at the app layer
- engine communication and bundled engine assets
- the broader analyze workflow
- the full private rules/move-generation implementation
- the larger private rendering and asset pipeline

That omission is intentional. The repo is designed to show architectural thinking and implementation quality without publishing the full private product internals.

## Why This Is A Better Public Story

For a recruiter or hiring manager, the strongest signal is not raw line count. It is whether the repo makes the underlying engineering decisions easy to understand.

This curated version highlights:
- boundary discipline between `core` and `app`
- incremental adaptation of an existing Java codebase
- custom rendering work
- reusable Compose components
- persistent state wired into a live preview workflow

That is a clearer and more defensible public artifact than releasing the entire product codebase.
