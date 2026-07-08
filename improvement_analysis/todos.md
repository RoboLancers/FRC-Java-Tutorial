 current todos:

 - [x] Review java_ files created in the basics section and add more examples and explanations as needed.
   - All 5 pages (java_basics, java_types_variables, java_methods, java_classes, java_control_flow) have code tabs and knowledge-check quizzes.
 - [x] review Github Classroom lesson in improvement_analysis and add more examples and explanations as needed.
   - See `github-classroom-github-basics-classroom50-migration.md` — reviewed, new grading scripts/tests added. Note: not yet validated against a live classroom50 repo (see last item below).
 - [ ] add page on logging telemetry.
   - Not done. `docs/code_examples/Telemetry.java` and `RobotTelemetry.java` are orphan files — no docs page references them, not in `mkdocs.yml` nav.
 - [x] Convert yagsl_example.md to a more formal swerve drive tutorial with more explanations and examples.
   - Done — `docs/programming/yagsl_swerve_tutorial.md` (498 lines), live in nav.
    - [ ] Also create a Classroom lesson to practice creating a swerve drive subsystem and command.
      - Not done. No swerve Classroom lesson found anywhere in the repo.
- [ ] Add instructions to lessons on testing in simulation.
  - Not done in the docs site. Simulation-based JUnit tests were built for a drivetrain-talon Classroom lesson (`unit-test-plan.md`), but that lesson template was later removed from the repo (`be54171 Removed lessom template`). No simulation-testing instructions exist in `docs/` itself.
- [x] review The smartdashboard.md page and add more examples and explanations as needed.
  - Done — `docs/programming/SmartDashboard.md` (214 lines) is thorough with tips and examples.
- [x] review the basic_shooter.md page and add more examples and explanations as needed.
  - Done — `docs/examples/basic_shooter.md` (236 lines) covers SparkMax and TalonFX with tabs.
- [x] The encoder reset example code is incorrect and needs to be fixed. 
  - Fixed. `EncoderExamples.java`/`EncoderExamplesTalonFX.java` correctly use `RelativeEncoder.setPosition(0)` and `motor.setPosition(0)`, and `using_sensors.md` explains the SparkMax vs. TalonFX difference.
- [~] expand sensors.md page.
  - Ambiguous/partial. `docs/basics/sensors.md` itself is still a short 53-line intro, unchanged. The real expansion happened in a separate page, `docs/programming/using_sensors.md` (174 lines, full code examples). Confirm which file was intended.
- [ ] test out Classroom lessons.
    - [ ] load into Github.
    - [ ] complete tasks.
    - [ ] Build and run in simulation to verify expected behavior.
    - [ ] test CI pipeline and auto-grader.
    - Not done — explicitly flagged as unvalidated in `github-classroom-github-basics-classroom50-migration.md` ("could not run the PR/Issue scripts against a real classroom50-graded repo").
 