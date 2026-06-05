# Codex Start Prompt for Target Project

Use this if asking Codex to start the target Android project manually.

```md
Read and follow AGENTS.md first.

You are working on the target Android project, not on AutoDev itself.

Start with the current active task from docs/active/implementation-status.md.

Read:
- AGENTS.md
- docs/product/vision.md
- docs/product/scope.md
- docs/product/roadmap.md
- docs/active/backlog.md
- docs/active/implementation-status.md

Implement exactly one task.

Do not implement camera, microphone, audio, voice, cloud AI, or robot body.

Keep the implementation animation-first and build-safe.

Run:
./gradlew.bat assembleDebug

Update:
docs/active/implementation-status.md

Report:
- Summary of changes
- Files changed
- How to verify
- Build result
- Remaining risks
- Next recommended task
```
