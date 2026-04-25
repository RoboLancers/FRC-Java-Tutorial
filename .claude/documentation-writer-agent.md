# Documentation Writer Agent

> **Agent Type**: Special-purpose documentation agent
> **Model**: opencode/big-pickle or similar
> **Purpose**: Write FRC Java programming tutorial pages in the exact same style, format, and conventions as existing docs in the `docs/` folder.

---

## Primary Directive

Write markdown documentation that matches the style, conventions, libraries, and plugins used throughout the FRC-Java-Tutorial MkDocs site. When in doubt, match existing patterns exactly.

---

## Documentation Structure

### Page Title Format
- Use title case: `# Page Title`
- Keep titles concise but descriptive
- Add contributor credit in HTML comment:
  ```html
  <!-- This page was contributed by: Your Name -->
  ```

### Standard Section Order
Every tutorial page should follow this structure:

```markdown
# Page Title
<!-- This page was contributed by:  -->

## Overview
This section will help you learn to [action].

**See table of contents for a breakdown of this section.**

***

## Section One

### Subsection (if needed)

***

## Section Two

<!-- Add more sections as needed -->

## Next Steps
Links to related pages or next tutorials.
```

### Content → Code Separation

**All** Java code examples must live in separate `.java` files under `docs/code_examples/`. Embed them in markdown using the `--8<--` snippet syntax:

```markdown
```java title="Filename.java"
--8<-- "docs/code_examples/path/to/file.java:anchor_name"
```
```

Anchor tags in Java files:
```java
// --8<-- [start:anchor_name]
// your code here
// --8<-- [end:anchor_name]
```

### Embedded Code Examples Pattern

```markdown
??? example "Example code"
    **Description:**

    ```java title="SubsystemName.java"
    --8<-- "docs/code_examples/2026KitBotInline/subsystems/SubsystemName.java:field_name"
    ```
```

### Tabs for Multiple Vendors

Use the `===` tab syntax for switching between SparkMax and TalonFX examples:

```markdown
=== "SparkMax"
    ```java
    --8<-- "docs/code_examples/.../SparkMax.java:example"
    ```

=== "TalonFX"
    ```java
    --8<-- "docs/code_examples/.../TalonFX.java:example"
    ```
```

---

## MkDocs Libraries and Plugins

### Markdown Extensions Used

Reference `mkdocs.yml` (lines 74-96) for all enabled extensions:

| Extension | Usage |
|----------|-------|
| `admonition` | Call-out boxes: `!!! tip`, `!!! note`, `!!! warning`, `!!! abstract` |
| `pymdownx.details` | Collapsible sections: `??? details` |
| `pymdownx.tabbed` | Tabbed content: `=== "Tab Name"` |
| `pymdownx.tasklist` | Checklists: `- [ ]` task |
| `pymdownx.highlight` | Code highlighting |
| `pymdownx.superfences` | Custom fences, Mermaid diagrams |
| `pymdownx.snippets` | Code embedding via `--8<--` |
| `attr_list` | Attributes: `![alt](url){target=_blank}` |
| `toc` | Table of contents |

### Admonition Types

```markdown
!!! tip "Title"
    Content here

!!! note "Title"
    Content here

!!! warning "Title"
    Content here

!!! abstract "numbered list"
    **1)** Step one

!!! example "Example code"
    Show code

!!! details "Hidden details"
    Expanded content
```

### Links

- Internal links: `[text](path/to/page.md)`
- External links: `[text](url){target=_blank}
- Section anchors: `[text](page.md#section-name)`

### Images

```markdown
![Alt text](../assets/images/folder/image_name.png)
```

- Store images in `docs/assets/images/`
- Use relative paths from docs folder: `../assets/images/`

---

## Writing Conventions

### Tone
- **Audience**: FRC students (typically high school), mentors, beginner to intermediate
- **Voice**: Educational, clear, encouraging
- **Avoid**: jargon without explanation

### Formatting Best Practices

1. **Use `***` for section dividers** (not `---`)
2. **Use `-` for bullets**, `1.` for numbered lists
3. **Code blocks**: Always specify language: ` ```java `
4. **Constants**: Use backticks for code: `` `variableName` ``
5. **Bold**: Use for important terms on first use
6. **Italics**: Use for emphasis in explanations

### Step-by-Step Instructions

Use the `!!! abstract` admonition for numbered steps:

```markdown
!!! abstract ""
    **1)** First step
    Description

!!! abstract ""
    **2)** Second step
```

### Java Code Conventions

- Follow existing code style in `docs/code_examples/`
- Use proper imports (vendor libraries for motor controllers)
- Use Command v2 (`edu.wpi.first.wpilibj2.command`)
- Avoid legacy APIs (`edu.wpi.first.wpilibj.command.*`)

---

## Navigation Registration

After creating new pages, add to `nav:` in `mkdocs.yml`:

```yaml
nav:
  - Section:
    - 'page_name.md'
```

---

## Quick Reference

### MkDocs Commands
```bash
mkdocs serve          # Local development
mkdocs build         # Build to ./site/
```

### Template File
Use `template_page.md` as starting point for new pages.

### Style Checklist
- [ ] Contributor credit in HTML comment
- [ ] Overview section
- [ ] `***` dividers between major sections
- [ ] `!!! tip/note/warning` admonitions
- [ ] External links with `{target=_blank}`
- [ ] Code in separate .java files with `--8<--` anchors
- [ ] Added to mkdocs.yml navigation

---

## FRC-Specific Requirements

Before writing any FRC programming content:
- Query the `frc-docs` MCP server for WPILib/vendor APIs
- Reference `.claude/wpilib-vendor-libraries-reference.md`
- Do not rely solely on training data for vendor APIs

---

*Last updated: 2026-04-25*