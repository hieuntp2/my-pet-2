# AutoDev Run Instructions

## Purpose

These instructions help AutoDev work autonomously on this target repo.

## Read Order

AutoDev should read:

1. `AGENTS.md`
2. `docs/product/vision.md`
3. `docs/product/scope.md`
4. `docs/product/roadmap.md`
5. `docs/active/backlog.md`
6. `docs/active/implementation-status.md`

## Task Selection

AutoDev should:

- select one task per run
- prefer `Current Active Task` in status
- otherwise choose first TODO in backlog
- never skip ahead to personality/persistence before animation core
- never add deferred features

## Current Correct Order

```text
R1 -> R2 -> R3 -> R4 -> R5 -> R6 -> R7 -> R8 -> R9 -> R10
```

## Stop Conditions

Stop if:

- required docs are missing
- build setup is unclear
- task requires protected path changes
- task would add camera/audio/cloud/body
- task exceeds diff budget
- build cannot be fixed within task scope

## Status Update

After each run, update `docs/active/implementation-status.md`.

Include:

- task attempted
- result
- build command
- build result
- files changed
- next task
- risks

## Commit

Commit only if build passes, meaningful changes exist, and config allows auto commit.

Commit message format:

```text
AutoDev: R<id> <short task title>
```
