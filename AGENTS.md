# AGENTS.md

MkDocs tutorial site teaching FRC Java programming. Built with MkDocs Material, hosted on GitHub Pages via `main` branch.

## Commands

```bash
pip install -r requirements.txt  # devcontainer does this
mkdocs serve                    # live reload at localhost:8000
mkdocs build                    # output to ./site/
```

No tests. No CLI linter (VS Code extension only).

## Architecture

**Content → Code separation**: Docs in `docs/` embed real Java from `docs/code_examples/` via `--8<--` anchors:

```markdown
--8<-- "docs/code_examples/2026KitBotInline/subsystems/CANDriveSubsystem.java:motors"
```

```java
// --8<-- [start:motors]
private final SparkMax leftLeader = ...
// --8<-- [end:motors]
```

When editing code examples, update both the `.java` file anchors and the Markdown references.

**Navigation**: Add new pages to `nav:` in `mkdocs.yml`. Unlisted pages build but aren't linked.

**Template**: Use `template_page.md` for new pages. Key conventions:
- Contributor credit in HTML comment at top
- `***` horizontal rules between sections
- `!!! tip/note/warning` admonitions
- External links with `{target=_blank}`

## FRC Content Policy

MUST query the `frc-docs` MCP server for WPILib/vendor APIs. Do not rely on training data.

### MCP Tools

```
search_frc_docs(query="<topic>", vendors=["rev","ctre"], version="2026")
fetch_frc_doc_page(url="https://docs.wpilib.org/en/...")
detect_project_context()
list_frc_doc_sections(vendors=["wpilib"])
```

- Default: `version="2026"`, `language="Java"`, `vendors=["all"]`
- Vendor mappings: `"rev"` (SparkMax, NEO), `"ctre"` (TalonFX, Falcon), `"redux"` (Canandcoder), `"wpilib"` (NavX, Limelight)

Static vendor ref: `.claude/wpilib-vendor-libraries-reference.md`

## Project Structure

| Directory | Purpose |
|-----------|---------|
| `docs/` | Tutorial Markdown pages |
| `docs/code_examples/` | Embedded Java files |
| `improvement_analysis/` | Research docs (not deployed) |

## External References

- YAGSL: https://docs.yagsl.com/
- MkDocs Material: https://squidfunk.github.io/mkdocs-material/reference/
- Template: `template_page.md`