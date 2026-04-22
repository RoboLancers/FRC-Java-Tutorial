# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Repo Is

A MkDocs-based tutorial site teaching FRC (FIRST Robotics Competition) Java programming to students. It is hosted on GitHub Pages via the `main` branch. The site is built with MkDocs Material and the `mkdocs-quiz` plugin.

## Commands

```bash
# Install dependencies (already done in devcontainer postCreate)
pip install -r requirements.txt

# Serve locally with live reload
mkdocs serve

# Build the static site to ./site/
mkdocs build
```

There are no tests. There is no linter configured — the devcontainer installs an MkDocs Material linter VS Code extension (`ytianle.mkdocs-material-linter`) but it has no CLI equivalent.

## Architecture

### Content → Code separation via snippets

Documentation pages live in [docs/](docs/) as Markdown. Java code examples live in [docs/code_examples/](docs/code_examples/) as real `.java` files. Pages embed code using the `pymdownx.snippets` `--8<--` syntax with named anchors:

```markdown
--8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:motors"
```

The anchor tags are comments in the Java files:
```java
// --8<-- [start:motors]
...
// --8<-- [end:motors]
```

This means **code examples must be valid Java** and changes to them require updating the anchor tags. When adding new code examples, add both the `.java` file and the corresponding `--8<--` references in the Markdown page.

### Navigation registration

New pages must be added to the `nav:` section of [mkdocs.yml](mkdocs.yml) to appear in the site. Pages not listed in `nav:` are built but not linked. The site uses `use_directory_urls: false`.

### FRC-specific content policy

Before writing or editing any FRC programming content (WPILib APIs, vendor libraries, motor controllers, sensors), use the documentation defined in .claude/wpilib-vendor-libraries.md. This file defines a set of commands for searching and fetching documentation from WPILib and major FRC vendors (REV, CTRE, Redux). Always search the links found there to find the most up-to-date information, and include citations in your answers. Do not rely solely on training data for vendor APIs, as they change frequently.

Always base FRC answers on these results and include citations. Do not rely solely on training data for vendor APIs (REV, CTRE, Redux, etc.).

Vendor-to-hardware mapping:
| Hardware | Vendor param |
|---|---|
| SparkMax, SparkFlex, NEO | `"rev"` |
| TalonFX, Falcon 500, Kraken, CANcoder, Pigeon | `"ctre"` |
| Canandcoder, Canandmag | `"redux"` |
| NavX, Limelight, PhotonVision | `"wpilib"` / `"photonvision"` |

Default to the **2026 season** unless otherwise specified.

### New page template

Use [template_page.md](template_page.md) as the starting point for new pages. Key conventions:

- Add contributor credit in the HTML comment at the top
- Use `***` horizontal rules between major sections
- Use `!!! tip`, `!!! note`, `!!! warning` admonitions for callouts
- Link targets should use `{target=_blank}` for external links

### Improvement planning docs

[improvement_analysis/](improvement_analysis/) contains research and planning documents (not part of the tutorial content). These are not deployed to the site and do not appear in `nav:`.
