# Animation Style Guide — Pixel Two-Eye Pet

## Goal

Create a living feeling using the smallest possible visual system: two pixel-style eyes.

## Style Keywords

- pixel
- cute
- soft
- expressive
- minimal
- companion-like
- original
- alive
- readable

## Rendering Rules

Use Jetpack Compose Canvas.

The avatar should be rendered from code, not image assets, during early MVP.

Recommended approach:

- virtual pixel grid
- snap dimensions to grid-like units
- use blocky rounded rectangles
- use simple squash/stretch
- use controlled easing

## Eye Shape Variables

Each eye can vary by:

- width
- height
- openness
- x offset
- y offset
- corner softness
- highlight position
- squash amount
- optional tilt later

## Animation Principles

### Small motion matters

The pet should not move constantly in a distracting way. Subtle motion should communicate life.

### Timing creates emotion

The same eye shape can feel different depending on timing.

- fast open = surprise
- slow close = sleepy
- tiny bounce = happy
- delayed reaction = annoyed or tired

### Imperfect rhythm feels alive

Avoid perfect periodic loops. Blink intervals and idle glances should vary.

### Reaction must be readable

A reaction should be clear within 300–1200 ms.

### Never leave invalid state

Every temporary animation must return to a valid base emotion pose.

## MVP Animation List

### Blink

- quick close
- slower open
- random interval

### Idle Breathing/Floating

- subtle vertical drift
- slight scale/squash
- low amplitude

### Look Around

- eyes shift together
- occasional idle curiosity

### Tap Reaction

- quick surprise or happy bounce
- return to base

### Long Press Reaction

- affectionate, annoyed, or sleepy depending on state later

### Feed Reaction

- happy/relieved expression

### Play Reaction

- excited/wide eye expression

### Rest Reaction

- sleepy slow close

## Commercial Design Safety

Do not copy:

- EMO exact eye proportions
- EMO exact animation sequences
- EMO exact face layout
- EMO branding/assets/sounds
- any commercial robot's distinctive identity

Build an original pet in the same broad category of cute companion creatures.
