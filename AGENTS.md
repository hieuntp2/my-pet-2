# AGENTS.md — AI Pet Animation-First Rebuild

## Role

You are an autonomous coding agent working on the target Android project.

Implement the project incrementally, one small task at a time, while keeping the build green.

## Product Goal

Build an original pixel-style Android digital pet that feels alive through:

- two expressive animated eyes
- natural idle behavior
- clear emotional reactions
- internal pet state
- personality traits
- local behavior selection
- gradual personality growth
- persistence later

## Core Philosophy

Believability first. Intelligence second.

The pet should feel alive before it becomes feature-rich.

The MVP is not a chatbot, voice assistant, camera AI demo, robot controller, or cloud AI wrapper. It is a small digital creature living on the Android screen.

## Technology

Use:

- Kotlin
- Android native
- Jetpack Compose
- Compose Canvas for avatar rendering
- Gradle multi-module project
- Room only when persistence tasks begin

Java is allowed only for Android/legacy interop when necessary.

## Mobile Orientation Constraint

This app is phone-landscape-only. Do not build, optimize, or test the primary gameplay/application layout for portrait phone orientation. On phones in portrait orientation, show a rotate-device message or blocker instead of attempting to support a portrait UI. The main experience should target landscape phone viewports, with safe-area handling for notches and mobile browser UI where relevant. Preserve existing asset-loading, blink runtime, and idle animation behavior when making orientation or layout changes.

## Multi-Agent Workflow Guidance

Use a multi-agent workflow whenever it can improve speed, code quality, review depth, or implementation safety. Prefer multi-agent work for non-trivial tasks, cross-file refactors, UI/runtime changes, performance-sensitive changes, architecture decisions, testing strategy, or changes that could affect asset loading, animation runtime, orientation handling, or user experience.

Suggested roles:

- Implementation agent: makes the primary code changes.
- Review agent: checks correctness, regressions, edge cases, and adherence to AGENTS.md.
- Test/QA agent: validates behavior, acceptance criteria, and possible runtime issues.
- Design/runtime agent when relevant: checks UI, animation, layout, responsiveness, and visual polish.

Do not use multi-agent coordination when the task is tiny and the coordination overhead would clearly outweigh the benefit. For small isolated edits, a single-agent workflow is acceptable.

When using multiple agents:

- Keep responsibilities clearly separated.
- Avoid conflicting edits to the same files.
- Reconcile outputs before finalizing.
- Ensure the final result is coherent, minimal, and consistent with project conventions.
- Always verify that existing asset-loading, blink behavior, idle animation, and phone-landscape behavior remain intact.

## Expected Modules

```text
app/
ui-avatar/
brain/
memory/
core-common/
```

Responsibilities:

- `app`: Android entry point, app shell, Home screen, debug screens, dependency wiring
- `ui-avatar`: pixel eye rendering and animation runtime
- `brain`: pet state, mood, personality, behavior selection, learning
- `memory`: persistence later
- `core-common`: pure utilities, time, math, random helpers

## Strictly Deferred

Do not implement these features unless a future task explicitly allows them:

- camera
- face detection
- object detection
- microphone
- sound detection
- voice recognition
- TTS
- audio playback
- cloud AI
- LLM chat
- robot body
- BLE / Wi-Fi robot control
- hardware sensors
- full assistant behavior

## Visual Identity Rules

The avatar is an original pixel-style two-eye digital pet.

Allowed:

- general cute desktop-pet feeling
- pixel-style eyes
- companion robot energy
- expressive timing
- subtle idle motion
- emotional eye poses

Not allowed:

- copying EMO's exact face design
- copying EMO's exact animation timing
- copying EMO's branding, name, sounds, icons, or assets
- making the avatar look like a commercial robot clone
- using mouth/full body in the MVP unless a later task explicitly permits it


## Asset Source Policy

Animation and pixel-art source files provided by the user must be placed under the repository-level `resources/` directory before implementation.

For the first eye animation milestone, Codex must look for source files here:

```text
resources/pet/eyes/looking-idle.png
resources/pet/eyes/looking-blink.png
```

Codex must copy these files into the Android app assets folder before building/running the app:

```text
app/src/main/assets/pet/eyes/looking-idle.png
app/src/main/assets/pet/eyes/looking-blink.png
```

Rules:

- Do not rename these files when using `assets/`.
- Do not move hyphenated animation files into `res/drawable`.
- Load these files through Android assets / AssetManager-compatible loading.
- Treat `resources/` as the editable source-of-truth for user-provided art.
- Treat `app/src/main/assets/` as the runtime location used by the Android app.
- If a required asset is missing from `resources/`, stop and report the missing file instead of inventing a placeholder production asset.

## Task Rules

For every task:

- implement only the requested task
- keep the build green
- do not add placeholder production logic
- do not leave TODOs in production code
- do not leave empty methods
- do not use `throw NotImplementedException`
- do not refactor unrelated files
- do not add unrelated dependencies
- do not edit protected requirement docs unless explicitly allowed
- update status after completing the task if allowed

## Autonomous Decision Policy

Do not ask the user during an autonomous run.

If ambiguous:

1. choose the smallest safe interpretation
2. stay within scope
3. write the decision to implementation status
4. continue only if safe

If unsafe:

1. stop
2. write blocked reason
3. do not commit

## Required Output After Each Run

Report exactly:

- Summary of changes
- Files changed
- How to verify
- Build result
- Remaining risks
- Next recommended task

Do not claim success unless the build command was run and passed.
