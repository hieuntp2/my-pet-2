# AI Pet Animation-First Rebuild — AutoDev Pack

This pack is the source-of-truth documentation for rebuilding the AI Pet Android app from scratch.

The rebuild direction is:

```text
Animated eyes -> interaction reactions -> internal pet state -> personality -> memory -> local learning
```

The first goal is not camera, audio, voice, cloud AI, or robot body control. The first goal is: open the app and immediately feel that the pet is alive.

## AutoDev Read Order

1. `AGENTS.md`
2. `docs/product/vision.md`
3. `docs/product/scope.md`
4. `docs/product/roadmap.md`
5. `docs/active/backlog.md`
6. `docs/animation/looking_idle_blink_android_spec.md`
7. `docs/active/implementation-status.md`

## Recommended First Tasks

1. `R1` — Create Android Kotlin project skeleton
2. `R2` — Render original two-eye pixel avatar
3. `R3` — Add natural blink animation
4. `R4` — Add subtle idle breathing/floating
5. `R5` — Add emotion states and debug preview

## Originality Rule

This is an original digital pet. It may be inspired by the broad category of desktop pets and companion robots, but it must not copy EMO's exact design, animation, branding, assets, sounds, layout, or product identity.

## User-Provided Animation Resources

Put original animation assets here before asking Codex to implement an animation milestone:

```text
resources/pet/eyes/looking-idle.png
resources/pet/eyes/looking-blink.png
```

Codex should copy them to the Android runtime assets folder:

```text
app/src/main/assets/pet/eyes/
```

Keep hyphenated filenames in `assets/`, not `res/drawable`.
