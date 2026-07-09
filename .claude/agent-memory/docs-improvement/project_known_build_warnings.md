---
name: Site build known warnings
description: Pre-existing mkdocs build warnings that are NOT caused by new page edits
type: project
---

These warnings appear on every `mkdocs build` and are pre-existing issues — do not treat them as regressions from new page work:

- WARNING: `programming/AdvantageKit.md` is in nav but the file does not exist on disk.
- WARNING: `programming/SwerveDriveIntro.md` contains a link to `3rd_party_libs.md` that cannot be resolved (wrong relative path).
- INFO: `examples/pid_elevator.md` and `examples/pid_shooter.md` contain unresolved `imageURL` placeholder links (unfilled template stubs).
- INFO: `setup/imaging_roboRIO.md` contains a broken anchor link to itself.
- INFO: `basics/driverstation_tips.md` exists in docs/ but is commented out of nav.

**Why:** Accumulated technical debt in pages not yet reviewed.
**How to apply:** When verifying a new page build, only flag warnings that are new (not in this list).
