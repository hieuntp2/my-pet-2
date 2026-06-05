# ADR-002 — Two-Eye MVP

## Status

Accepted

## Context

A full body, mouth, sound system, or complex sprite engine can increase scope too early.

The pet must be emotionally readable from the smallest visual set.

## Decision

MVP avatar uses two expressive pixel-style eyes only.

No mouth. No nose. No full body. No commercial robot clone.

## Consequences

Positive:

- small scope
- faster implementation
- forces strong eye animation design
- easier rendering with Compose Canvas

Negative:

- fewer expression channels
- eyes must carry all emotion
- later body/accessories require careful integration
