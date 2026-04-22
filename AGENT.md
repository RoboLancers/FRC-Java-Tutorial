# AGENT.md

This file provides guidance to AI agents when working with code in this repository.

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

Before writing or editing any FRC programming content (WPILib APIs, vendor libraries, motor controllers, sensors), query the `frc-docs` MCP server. Do not rely solely on training data for vendor APIs — they change frequently. Always base answers on MCP results and include citations.

The `frc-docs` MCP server exposes four tools:

**`search_frc_docs`** — search across WPILib, REV, CTRE, Redux, and PhotonVision documentation.
```
search_frc_docs(query="<topic>")
search_frc_docs(query="<topic>", vendors=["wpilib", "rev"], language="Java")
search_frc_docs(query="<topic>", vendors=["ctre"], version="2025", max_results=5)
```
- `vendors`: `["all"]` (default), or any of `"wpilib"`, `"rev"`, `"ctre"`, `"redux"`, `"photonvision"`
- `language`: `"Java"` (default), `"Python"`, or `"C++"`
- `version`: `"2025"` (default) or `"2026"` — default to **2026** for this project
- `max_results`: 1–25, default 10
- `auto_detect`: `true` (default) — auto-infers language and vendors from project files

**`detect_project_context`** — scan project files to identify language and vendor libraries in use.
```
detect_project_context()
detect_project_context(project_path="/workspaces/FRC-Java-Tutorial/improvement_analysis/github-classroom-drivetrain/template")
```

**`fetch_frc_doc_page`** — fetch full content of a specific documentation page by URL.
```
fetch_frc_doc_page(url="https://docs.wpilib.org/en/stable/docs/software/hardware-apis/motors/spark-max.html")
fetch_frc_doc_page(url="https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/api-usage/configuration.html")
```

**`list_frc_doc_sections`** — browse available documentation sections by vendor.
```
list_frc_doc_sections()
list_frc_doc_sections(vendors=["rev", "ctre"], language="Java")
```

Vendor-to-hardware mapping (use for the `vendors` parameter):
| Hardware | `vendors` value |
|---|---|
| SparkMax, SparkFlex, NEO | `"rev"` |
| TalonFX, Falcon 500, Kraken, CANcoder, Pigeon | `"ctre"` |
| Canandcoder, Canandmag | `"redux"` |
| NavX, Limelight, PhotonVision | `"wpilib"` / `"photonvision"` |

Default to the **2026 season** unless otherwise specified. For static vendor library reference (vendordep URLs, import packages, hardware overviews), see [.claude/wpilib-vendor-libraries-reference.md](.claude/wpilib-vendor-libraries-reference.md).

### New page template

Use [template_page.md](template_page.md) as the starting point for new pages. Key conventions:

- Add contributor credit in the HTML comment at the top
- Use `***` horizontal rules between major sections
- Use `!!! tip`, `!!! note`, `!!! warning` admonitions for callouts
- Link targets should use `{target=_blank}` for external links

### Improvement planning docs

[improvement_analysis/](improvement_analysis/) contains research and planning documents (not part of the tutorial content). These are not deployed to the site and do not appear in `nav:`.

### Project Structure
[docs](docs/) contains all the documentation files for the tutorial.
[code_examples](code_examples/) contains all the code examples for the tutorial.
[improvement_analysis](improvement_analysis/) contains research and planning documents (not part of the tutorial content). These are not deployed to the site and do not appear in `nav:`.