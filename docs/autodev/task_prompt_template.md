# AutoDev Task Prompt Template

Use this template if manually giving one task to AutoDev or Codex.

```md
Read and follow AGENTS.md first.

Task ID:
<task id>

Title:
<title>

Goal:
<one clear goal>

Read first:
- AGENTS.md
- docs/product/vision.md
- docs/product/scope.md
- docs/product/roadmap.md
- <task-specific docs>

Scope:
- Implement only this task.
- Do not add camera.
- Do not add microphone.
- Do not add audio.
- Do not add voice.
- Do not add cloud AI.
- Do not add robot body.
- Do not copy EMO assets or exact visual identity.

Technical requirements:
- Kotlin
- Android native
- Jetpack Compose
- Compose Canvas for avatar rendering when relevant
- Keep modules clean
- Keep build green

Files likely touched:
- <files>

Implementation requirements:
1. <step>
2. <step>
3. <step>

Definition of Done:
- <visible/verifiable result>
- Build succeeds
- No TODO / placeholder production logic

Verification:
- <manual test>

Build command:
./gradlew.bat assembleDebug

Output required:
- Summary of changes
- Files changed
- How to verify
- Build result
- Remaining risks
- Next recommended task

Recommended reasoning/model effort:
High
```
