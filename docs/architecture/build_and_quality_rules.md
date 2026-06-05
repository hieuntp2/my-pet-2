# Build and Quality Rules

## Build Command

Default:

```bash
./gradlew.bat assembleDebug
```

If running in Git Bash or Linux-like shell:

```bash
./gradlew assembleDebug
```

## Done Means

A task is done only when:

- build passes
- feature is visible or verifiable
- no unrelated scope was added
- no TODO in production path
- no empty production methods
- no `throw NotImplementedException`
- status is updated if allowed

## Dependency Rules

Do not add heavy dependencies without a task requiring them.

Do not add CameraX, ML Kit, audio libraries, cloud SDKs, BLE libraries, or game engines during animation-first MVP.

## Gradle Rules

- keep configuration minimal
- do not do broad version refactors
- do not add build complexity unless required
- prefer stable Android/Compose setup

## UI Rules

- Home screen should be clean and pet-focused
- Debug controls should not pollute final pet experience
- Debug screens are allowed for verifying animation/state/personality
